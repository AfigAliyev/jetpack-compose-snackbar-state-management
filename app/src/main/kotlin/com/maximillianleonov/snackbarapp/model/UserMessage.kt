package com.maximillianleonov.snackbarapp.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/**
 * A sealed interface for defining user messages that can be displayed in the UI.
 */
sealed interface UserMessage {
    /**
     * A data class that represents a user message as a simple text string.
     *
     * @property value The text string value of the user message.
     */
    data class Text(val value: String) : UserMessage

    /**
     * A class that represents a user message as a string resource with optional format arguments.
     *
     * @property resId The resource ID of the string resource for the user message.
     * @property formatArgs Optional format arguments for the string resource.
     */
    class StringResource(@StringRes val resId: Int, vararg val formatArgs: Any) : UserMessage

    companion object {
        /**
         * Returns a [UserMessage.Text] object with the given text [value].
         *
         * @param value The text string value of the user message.
         * @return A new instance of [UserMessage.Text] with the given text [value].
         */
        fun from(value: String) = Text(value = value)

        /**
         * Returns a [UserMessage.StringResource] object with the given string resource [resId] and
         * optional format arguments.
         *
         * @param resId The resource ID of the string resource for the user message.
         * @param formatArgs Optional format arguments for the string resource.
         * @return A new instance of [UserMessage.StringResource] with the given string resource
         * [resId] and optional format arguments.
         */
        fun from(@StringRes resId: Int, vararg formatArgs: Any) =
            StringResource(resId = resId, formatArgs = formatArgs)
    }
}

/**
 * Returns a [String] representation of this [UserMessage] object.
 */
@Composable
fun UserMessage.asString() = when (this) {
    is UserMessage.Text -> value
    is UserMessage.StringResource -> stringResource(id = resId, formatArgs = formatArgs)
}
