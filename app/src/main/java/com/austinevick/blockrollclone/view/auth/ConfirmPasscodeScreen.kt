package com.austinevick.blockrollclone.view.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.austinevick.blockrollclone.components.LoadingDialog
import com.austinevick.blockrollclone.components.PasscodeWidget
import com.austinevick.blockrollclone.navigation.Destinations
import com.austinevick.blockrollclone.view.auth.viewmodel.PasscodeViewModel


@Composable
fun ConfirmPasscodeScreen(
    navController: NavHostController,
    passcode: String,
    viewModel: PasscodeViewModel = hiltViewModel()
) {
    val passcodeState = viewModel.passcode.collectAsStateWithLifecycle()
    val passcodeUiState = viewModel.passcodeState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }



    LaunchedEffect(passcodeState.value) {
        if (passcodeState.value.length == viewModel.digits) {
            // navController.navigate(Destinations.ConfirmPasscode.route)
            viewModel.createPasscode(passcode)
        }
    }

    PasscodeWidget(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        title = "Confirm Passcode $passcode"
    )

    if (passcodeUiState.value.isLoading) LoadingDialog()

}