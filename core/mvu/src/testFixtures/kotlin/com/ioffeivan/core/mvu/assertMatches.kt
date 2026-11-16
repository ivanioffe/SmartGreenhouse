package com.ioffeivan.core.mvu

import com.google.common.truth.Truth.assertThat

/**
 * Checks the state and the contents and order of the command and effect lists.
 * Omit an expected list to assert that it is empty.
 */
fun <State, Command, Effect> ReducerResult<State, Command, Effect>.assertMatches(
    expectedState: State,
    expectedCommands: List<Command> = emptyList(),
    expectedEffects: List<Effect> = emptyList(),
) {
    assertThat(state).isEqualTo(expectedState)
    assertThat(commands).isEqualTo(expectedCommands)
    assertThat(effects).isEqualTo(expectedEffects)
}
