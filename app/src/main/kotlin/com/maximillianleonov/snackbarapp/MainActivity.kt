package com.maximillianleonov.snackbarapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.maximillianleonov.snackbarapp.component.ProvideSnackbarController
import com.maximillianleonov.snackbarapp.theme.SnackbarAppTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SnackbarAppTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                val coroutineScope = rememberCoroutineScope()

                ProvideSnackbarController(
                    snackbarHostState = snackbarHostState,
                    coroutineScope = coroutineScope
                ) {
                    Scaffold(
                        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                    ) { innerPadding ->
                        MainScreen(modifier = Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}
