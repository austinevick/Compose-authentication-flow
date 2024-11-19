package com.austinevick.blockrollclone.view.auth

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.austinevick.blockrollclone.common.Constants
import com.austinevick.blockrollclone.components.PasscodeWidget
import com.austinevick.blockrollclone.navigation.Destinations
import com.austinevick.blockrollclone.view.auth.viewmodel.PasscodeViewModel

@Composable
fun CreatePasscodeScreen(
    navController: NavHostController,
    viewModel: PasscodeViewModel = hiltViewModel()
) {
    val passcode = viewModel.passcode.collectAsStateWithLifecycle()


    LaunchedEffect(passcode.value) {
        if (passcode.value.length==viewModel.digits){
            navController.currentBackStackEntry?.savedStateHandle?.set<String>(Constants.passcodeKey,passcode.value)
           navController.navigate(Destinations.ConfirmPasscode.route)
        }
    }

    PasscodeWidget(title = "Create Passcode")


}

