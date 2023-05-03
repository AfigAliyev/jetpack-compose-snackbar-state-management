package com.maximillianleonov.snackbarapp.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import com.maximillianleonov.snackbarapp.model.SnackbarMessage
import com.maximillianleonov.snackbarapp.model.asString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * An interface that provides methods to display snackbars.
 */
@Immutable
interface SnackbarController {
    /**
     * Displays a text message with an optional action label, and an optional dismiss action.
     *
     * @param message text to be shown in the Snackbar.
     * @param actionLabel optional action label to show as button in the Snackbar.
     * @param withDismissAction a boolean to show a dismiss action in the Snackbar. This is
     * recommended to be set to true better accessibility when a Snackbar is set with a
     * [SnackbarDuration.Indefinite].
     * @param duration duration of the Snackbar.
     * @param onSnackbarResult A callback for when the snackbar is dismissed or the action is performed.
     */
    fun showMessage(
        message: String,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration = SnackbarDuration.Short,
        onSnackbarResult: (SnackbarResult) -> Unit = {}
    )

    /**
     * Displays a snackbar with custom visuals.
     *
     * @param snackbarVisuals Custom visuals for the snackbar.
     * @param onSnackbarResult A callback for when the snackbar is dismissed or the action is performed.
     */
    fun showMessage(
        snackbarVisuals: SnackbarVisuals,
        onSnackbarResult: (SnackbarResult) -> Unit = {}
    )
}

/**
 * Returns a [SnackbarController] that uses the given [snackbarHostState] and [coroutineScope] to display snackbars.
 *
 * @param snackbarHostState The [SnackbarHostState] to use.
 * @param coroutineScope The [CoroutineScope] to use.
 * @return A [SnackbarController] that uses the given [snackbarHostState] and [coroutineScope] to display snackbars.
 */
@Stable
fun SnackbarController(
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope
): SnackbarController = SnackbarControllerImpl(
    snackbarHostState = snackbarHostState,
    coroutineScope = coroutineScope
)

/**
 * Provides a [SnackbarController] to its content.
 *
 * @param snackbarHostState The [SnackbarHostState] to use.
 * @param coroutineScope The [CoroutineScope] to use.
 * @param content The content that will have access to the [SnackbarController].
 */
@Composable
fun ProvideSnackbarController(
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalSnackbarController provides SnackbarController(
            snackbarHostState = snackbarHostState,
            coroutineScope = coroutineScope
        ),
        content = content
    )
}

/**
 * Handles a [SnackbarMessage] by showing a Snackbar message or visuals using the [snackbarController].
 * The [snackbarMessage] can be either a [SnackbarMessage.Text] or a [SnackbarMessage.Visuals].
 * The [onDismissSnackbar] callback is invoked when the Snackbar is dismissed. If [snackbarMessage]
 * is null, this function returns early and does nothing.
 */
@Composable
fun SnackbarMessageHandler(
    snackbarMessage: SnackbarMessage?,
    onDismissSnackbar: () -> Unit,
    snackbarController: SnackbarController = LocalSnackbarController.current
) {
    if (snackbarMessage == null) return

    when (snackbarMessage) {
        is SnackbarMessage.Text -> {
            val message = snackbarMessage.userMessage.asString()
            val actionLabel = snackbarMessage.actionLabelMessage?.asString()

            LaunchedEffect(snackbarMessage, onDismissSnackbar) {
                snackbarController.showMessage(
                    message = message,
                    actionLabel = actionLabel,
                    withDismissAction = snackbarMessage.withDismissAction,
                    duration = snackbarMessage.duration,
                    onSnackbarResult = snackbarMessage.onSnackbarResult
                )

                onDismissSnackbar()
            }
        }

        is SnackbarMessage.Visuals -> {
            LaunchedEffect(snackbarMessage, onDismissSnackbar) {
                snackbarController.showMessage(
                    snackbarVisuals = snackbarMessage.snackbarVisuals,
                    onSnackbarResult = snackbarMessage.onSnackbarResult
                )

                onDismissSnackbar()
            }
        }
    }
}

/**
 * Implementation of the [SnackbarController] interface that uses a [SnackbarHostState] to show
 * Snackbar messages. The [coroutineScope] is used to launch coroutines for showing Snackbar messages.
 *
 * @param snackbarHostState The [SnackbarHostState] used to show Snackbar messages.
 * @param coroutineScope The [CoroutineScope] used to launch coroutines for showing Snackbar messages.
 */
@Immutable
private class SnackbarControllerImpl(
    private val snackbarHostState: SnackbarHostState,
    private val coroutineScope: CoroutineScope
) : SnackbarController {
    /**
     * Shows a Snackbar message with the given parameters and invokes the [onSnackbarResult] callback
     * on the snackbar result.
     */
    override fun showMessage(
        message: String,
        actionLabel: String?,
        withDismissAction: Boolean,
        duration: SnackbarDuration,
        onSnackbarResult: (SnackbarResult) -> Unit
    ) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = actionLabel,
                withDismissAction = withDismissAction,
                duration = duration
            ).let(onSnackbarResult)
        }
    }

    /**
     * Shows a Snackbar message with the given parameters and invokes the [onSnackbarResult] callback
     * on the snackbar result.
     */
    @OptIn(ExperimentalMaterial3Api::class)
    override fun showMessage(
        snackbarVisuals: SnackbarVisuals,
        onSnackbarResult: (SnackbarResult) -> Unit
    ) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(visuals = snackbarVisuals).let(onSnackbarResult)
        }
    }
}

/**
 * Static CompositionLocal that provides access to a [SnackbarController]. The value of the
 * [LocalSnackbarController] is set using the [CompositionLocalProvider] composable. If no
 * [SnackbarController] is provided, an error is thrown.
 */
val LocalSnackbarController = staticCompositionLocalOf<SnackbarController> {
    error("No SnackbarController provided.")
}
