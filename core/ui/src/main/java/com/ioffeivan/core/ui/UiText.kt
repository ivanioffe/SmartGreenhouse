package com.ioffeivan.core.ui

import android.content.Context
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource

/** Already resolved text, a string resource, or a plural resource for display in the UI. */
sealed class UiText {
    /** Text that is displayed as supplied. */
    data class StaticString(val value: String) : UiText()

    /**
     * Represents a string resource ID, potentially with format arguments.
     * @param id The Android string resource ID.
     * @param args Format arguments. Omit them to read the resource without formatting.
     */
    class StringResource(
        @param:StringRes val id: Int,
        vararg val args: Any,
    ) : UiText() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as StringResource

            if (id != other.id) return false
            if (!args.contentEquals(other.args)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = id
            result = 31 * result + args.contentHashCode()

            return result
        }
    }

    /**
     * Represents a plural string resource ID, enabling quantity-based selection (one, few, many, etc.)
     * with optional format arguments.
     * @param id The Android plural resource ID.
     * @param quantity The number used to select a plural form.
     * @param args Format arguments for the selected form. Include the quantity if the resource displays
     * the count. Omit arguments to read the selected form without formatting.
     */
    class PluralResource(
        @param:PluralsRes val id: Int,
        val quantity: Int,
        vararg val args: Any,
    ) : UiText() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as PluralResource

            if (id != other.id) return false
            if (quantity != other.quantity) return false
            if (!args.contentEquals(other.args)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = id
            result = 31 * result + quantity
            result = 31 * result + args.contentHashCode()

            return result
        }
    }

    /**
     * Resolves the UiText into a displayable string within a [Composable] context.
     * @return The final string resolved from its source.
     */
    @Composable
    @ReadOnlyComposable
    @Suppress("SpreadOperator") // Resources requires vararg format arguments.
    fun asString(): String {
        return when (this) {
            is StaticString -> {
                value
            }

            is StringResource -> {
                if (args.isEmpty()) {
                    stringResource(id)
                } else {
                    stringResource(id, *args)
                }
            }

            is PluralResource -> {
                if (args.isEmpty()) {
                    pluralStringResource(id, quantity)
                } else {
                    pluralStringResource(id, quantity, *args)
                }
            }
        }
    }

    /**
     * Resolves the [UiText] into a displayable string using an explicit [Context].
     * Used outside of Composition.
     * @param context The Android Context used to access resources.
     * @return The final string resolved from its source.
     */
    @Suppress("SpreadOperator") // Resources requires vararg format arguments.
    fun asString(context: Context): String {
        return when (this) {
            is StaticString -> {
                value
            }

            is StringResource -> {
                if (args.isEmpty()) {
                    context.getString(id)
                } else {
                    context.getString(id, *args)
                }
            }

            is PluralResource -> {
                if (args.isEmpty()) {
                    context.resources.getQuantityString(id, quantity)
                } else {
                    context.resources.getQuantityString(id, quantity, *args)
                }
            }
        }
    }
}
