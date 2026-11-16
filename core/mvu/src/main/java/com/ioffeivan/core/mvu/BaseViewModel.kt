package com.ioffeivan.core.mvu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Runs [reducer] in [viewModelScope] and exposes its state and effects to the UI.
 *
 * Events and messages are reduced one at a time against the latest internal state.
 * For each result, the ViewModel updates that state, invokes [handleCommand] for each command,
 * and queues the effects. Commands and effects are dispatched in their respective list order.
 *
 * @param initialState The state before the first event or message is processed.
 */
abstract class BaseViewModel<
    State : Reducer.State,
    Event : Reducer.Event,
    Message : Reducer.Message,
    Command : Reducer.Command,
    Effect : Reducer.Effect,
>(
    initialState: State,
    private val reducer: Reducer<State, Event, Message, Command, Effect>,
) : ViewModel() {
    private val mutableState = MutableStateFlow(initialState)

    /** Read-only reducer state for subclasses that derive their own public [state] flow. */
    @Suppress("VariableNaming")
    protected val _state: StateFlow<State> = mutableState.asStateFlow()

    /** State exposed to the UI. By default, this is the reducer's state. */
    open val state: StateFlow<State> = _state

    private val _effect = Channel<Effect>(capacity = Channel.UNLIMITED)

    /**
     * One-time UI effects buffered until a collector receives them.
     * Each effect goes to one collector and is consumed when received.
     */
    val effect = _effect.receiveAsFlow()

    private val reductionMutex = Mutex()

    /** Submits a UI [event] for reduction. */
    fun onEvent(event: Event) {
        processReduction { currentState ->
            reducer.reduceEvent(currentState, event)
        }
    }

    /** Submits an internal [message], such as the outcome of a command, for reduction. */
    protected fun onMessage(message: Message) {
        processReduction { currentState ->
            reducer.reduceMessage(currentState, message)
        }
    }

    /**
     * Dispatches [command] while the current reduction holds the lock.
     * Return promptly. Launch suspending work in [viewModelScope] and send its outcome through
     * [onMessage]. Asynchronous work may finish in a different order than it was dispatched.
     */
    protected abstract fun handleCommand(command: Command)

    private fun processReduction(
        reduce: (State) -> ReducerResult<State, Command, Effect>,
    ) {
        viewModelScope.launch {
            reductionMutex.withLock {
                val (newState, commands, effects) = reduce(mutableState.value)

                mutableState.value = newState
                commands.forEach(::handleCommand)
                effects.forEach { _effect.send(it) }
            }
        }
    }
}
