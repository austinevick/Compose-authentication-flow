package com.austinevick.blockrollclone.view.auth

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.austinevick.blockrollclone.view.auth.viewmodel.VerificationViewModel

@Composable
fun AuthNotifierScreen(
    navController: NavHostController,
    viewModel: VerificationViewModel = hiltViewModel()
) {
    val passcodeValue = viewModel.passcodeValue.collectAsState()

    Log.d("passcode value", passcodeValue.value.toString())


    if (passcodeValue.value) {
        PasscodeLoginScreen(navController)
    } else {
        LoginScreen(navController)
    }

}
