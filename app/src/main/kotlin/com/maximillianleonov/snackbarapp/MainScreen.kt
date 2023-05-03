package com.maximillianleonov.snackbarapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.maximillianleonov.snackbarapp.component.LocalSnackbarController
import com.maximillianleonov.snackbarapp.component.SnackbarController
import com.maximillianleonov.snackbarapp.component.SnackbarMessageHandler

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel(),
    snackbarController: SnackbarController = LocalSnackbarController.current
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MainScreen(
        modifier = modifier,
        uiState = uiState,
        onShowSnackbarFromUi = snackbarController::showMessage,
        onShowSnackbarFromViewModel = viewModel::showMessage,
        onDismissSnackbar = viewModel::dismissSnackbar
    )
}

@Composable
private fun MainScreen(
    uiState: MainUiState,
    onShowSnackbarFromUi: (String) -> Unit,
    onShowSnackbarFromViewModel: () -> Unit,
    onDismissSnackbar: () -> Unit,
    modifier: Modifier = Modifier
) {
    SnackbarMessageHandler(
        snackbarMessage = uiState.snackbarMessage,
        onDismissSnackbar = onDismissSnackbar
    )

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val message = stringResource(id = R.string.hello_world_from_ui)
        Button(onClick = { onShowSnackbarFromUi(message) }) {
            Text(text = stringResource(id = R.string.show_snackbar_from_ui))
        }

        Button(onClick = onShowSnackbarFromViewModel) {
            Text(text = stringResource(id = R.string.show_snackbar_from_view_model))
        }
    }
}
