package com.ioffeivan.core.mvu

/**
 * Collects the output of one reduction. By default, the result keeps [previousState]
 * and has empty command and effect lists.
 *
 * @property previousState The original state supplied to the reducer, even after [state] updates.
 */
class ReducerResultBuilder<State, Command, Effect> internal constructor(
    val previousState: State,
) {
    private var currentState: State = previousState
    private var commandValues: List<Command> = emptyList()
    private var effectValues: List<Effect> = emptyList()

    /**
     * Runs [block] with the current working state as its receiver and stores the returned state.
     * Consecutive calls build on each other, starting with [previousState].
     */
    fun state(block: State.() -> State) {
        currentState = currentState.block()
    }

    /** Replaces the command list with [values] in the given order. Pass no values to clear it. */
    fun commands(vararg values: Command) {
        commandValues = values.toList()
    }

    /** Replaces the effect list with [values] in the given order. Pass no values to clear it. */
    fun effects(vararg values: Effect) {
        effectValues = values.toList()
    }

    internal fun build(): ReducerResult<State, Command, Effect> =
        ReducerResult(
            state = currentState,
            commands = commandValues,
            effects = effectValues,
        )
}
