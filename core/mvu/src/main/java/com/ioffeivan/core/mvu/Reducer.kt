package com.ioffeivan.core.mvu

/**
 * Defines how an [Event] or [Message] transforms the current [State] into a [ReducerResult].
 *
 * Implement the handlers as pure transformations of the input and state. Use [Command] to
 * describe work for the ViewModel and [Effect] to describe one-time UI actions.
 * Each reduction uses a fresh [ReducerResultBuilder].
 */
abstract class Reducer<
    State : Reducer.State,
    Event : Reducer.Event,
    Message : Reducer.Message,
    Command : Reducer.Command,
    Effect : Reducer.Effect,
> {
    /** A snapshot of the feature's state. Treat it as immutable. */
    interface State

    /** Input from the UI. */
    interface Event

    /** Internal input, such as the outcome of a command. */
    interface Message

    /** Work for the ViewModel to perform. */
    interface Command

    /** A one-time UI action, such as navigation or showing a snackbar. */
    interface Effect

    /** Reduces [event] against [currentState]. */
    fun reduceEvent(
        currentState: State,
        event: Event,
    ): ReducerResult<State, Command, Effect> =
        ReducerResultBuilder<State, Command, Effect>(currentState)
            .apply { reduceEvent(event) }
            .build()

    /** Reduces [message] against [currentState]. */
    fun reduceMessage(
        currentState: State,
        message: Message,
    ): ReducerResult<State, Command, Effect> =
        ReducerResultBuilder<State, Command, Effect>(currentState)
            .apply { reduceMessage(message) }
            .build()

    /** Describes the result of handling [event] using the builder. */
    protected abstract fun ReducerResultBuilder<State, Command, Effect>.reduceEvent(event: Event)

    /** Describes the result of handling [message] using the builder. */
    protected abstract fun ReducerResultBuilder<State, Command, Effect>.reduceMessage(message: Message)
}

/** The state, ordered commands, and ordered effects produced by one reduction. */
@ConsistentCopyVisibility
data class ReducerResult<State, Command, Effect> internal constructor(
    val state: State,
    val commands: List<Command> = emptyList(),
    val effects: List<Effect> = emptyList(),
)
