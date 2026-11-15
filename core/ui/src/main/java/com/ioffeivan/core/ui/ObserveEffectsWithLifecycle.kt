package com.ioffeivan.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

/**
 * Collects [effects] while this call is in the composition and [lifecycleOwner]'s lifecycle
 * is at or above [minActiveState].
 *
 * Each call to [onEffect] returns before the next effect is handled. Use `launch` in the
 * handler's [CoroutineScope] for work that can run alongside later effects. The collection
 * and its child coroutines are cancelled when the lifecycle drops below [minActiveState]
 * or this call leaves the composition.
 *
 * Changes to [onEffect] take effect on the next value without restarting the collection.
 *
 * @param effects The flow of UI effects.
 * @param onEffect The suspending handler for each effect, with the active collection's scope as its receiver.
 * @param lifecycleOwner The owner whose lifecycle controls collection.
 * @param minActiveState The minimum lifecycle state for collection.
 */
@Composable
fun <T> ObserveEffectsWithLifecycle(
    effects: Flow<T>,
    onEffect: suspend CoroutineScope.(T) -> Unit,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
) {
    val lifecycle = lifecycleOwner.lifecycle
    val currentOnEffect by rememberUpdatedState(onEffect)

    LaunchedEffect(effects, lifecycle, minActiveState) {
        lifecycle.repeatOnLifecycle(minActiveState) {
            effects.collect { effect -> currentOnEffect(effect) }
        }
    }
}
