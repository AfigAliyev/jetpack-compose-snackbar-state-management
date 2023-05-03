package com.maximillianleonov.snackbarapp

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import androidx.lifecycle.ViewModel
import com.maximillianleonov.snackbarapp.model.SnackbarMessage
import com.maximillianleonov.snackbarapp.model.UserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    fun showMessage() = _uiState.update {
        it.copy(
            snackbarMessage = SnackbarMessage.from(
                userMessage = UserMessage.from(resId = R.string.hello_world_from_view_model),
                actionLabelMessage = UserMessage.from(resId = R.string.action),
                withDismissAction = true,
                duration = SnackbarDuration.Indefinite,
                onSnackbarResult = ::handleSnackbarResult
            )
        )
    }

    fun dismissSnackbar() = _uiState.update { it.copy(snackbarMessage = null) }

    private fun handleSnackbarResult(snackbarResult: SnackbarResult) {
        _uiState.update {
            it.copy(
                snackbarMessage = SnackbarMessage.from(
                    userMessage = UserMessage.from(
                        resId = when (snackbarResult) {
                            SnackbarResult.Dismissed -> R.string.dismissed
                            SnackbarResult.ActionPerformed -> R.string.action_performed
                        }
                    )
                )
            )
        }
    }
}
