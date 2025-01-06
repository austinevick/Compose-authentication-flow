package com.austinevick.blockrollclone.view.auth

import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.austinevick.blockrollclone.R
import com.austinevick.blockrollclone.components.CustomButton
import com.austinevick.blockrollclone.components.LoadingDialog
import com.austinevick.blockrollclone.components.NonLazyGrid
import com.austinevick.blockrollclone.navigation.Destinations
import com.austinevick.blockrollclone.view.auth.viewmodel.PasscodeViewModel
import kotlinx.coroutines.launch

@Composable
fun PasscodeLoginScreen(
    navController: NavHostController,
    viewModel: PasscodeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val passcode = remember { mutableStateOf("") }
    val digits = 4

    val state = viewModel.passcodeState.collectAsState()

    LaunchedEffect(key1 = state.value) {
        if (state.value.message != null) {
            Toast.makeText(context, state.value.message, Toast.LENGTH_SHORT)
                .show()
        }
    }


//    fun showBiometricPrompt() {
//        val info = BiometricPrompt.PromptInfo.Builder()
//            .setTitle("Biometric Authentication")
//            .setSubtitle("Log in using your biometric credential")
//            .setNegativeButtonText("Cancel")
//            .build()
//
//
//        val biometricPrompt = BiometricPrompt(context, ContextCompat.getMainExecutor(context),
//            object : BiometricPrompt.AuthenticationCallback() {
//                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
//                    super.onAuthenticationError(errorCode, errString)
//                }
//
//                override fun onAuthenticationFailed() {
//                    super.onAuthenticationFailed()
//                }
//
//                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
//                    super.onAuthenticationSucceeded(result)
//                }
//
//            }
//        )
//        biometricPrompt.authenticate(info)
//    }





    Scaffold(containerColor = Color.White) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(innerPadding)
        ) {
            Image(
                painter = painterResource(id = R.drawable.avatar),
                contentDescription = null,
                Modifier
                    .size(130.dp)
                    .padding(top = 30.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Welcome Back!", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(30.dp))

            Text(text = "Enter your passcode to continue")
            Spacer(modifier = Modifier.height(16.dp))

            Row {
                repeat(4) { i ->
                    val isActive = passcode.value.length > i
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .background(Color.LightGray.copy(0.2f), CircleShape)
                            .padding(10.dp)
                            .size(40.dp)
                    ) {
                        if (isActive) Image(painterResource(R.drawable.circle), null)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            NonLazyGrid(columns = 3, itemCount = 12) { i ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(8.dp)
                        .background(Color.LightGray.copy(0.2f), CircleShape)
                        .size(70.dp)
                        .clickable(
                            onClick = {
                                when (i) {
                                    11 -> {
                                        if (passcode.value.isNotBlank())
                                            passcode.value =
                                                passcode.value.substring(
                                                    0,
                                                    passcode.value.length - 1
                                                )
                                    }

                                    10 -> {
                                        if (passcode.value.length == digits) return@clickable
                                        passcode.value += "0"
                                    }

                                    9 -> {}
                                    else -> {
                                        if (passcode.value.length == digits) return@clickable
                                        passcode.value += (i + 1).toString()
                                    }
                                }
                                if (passcode.value.length == digits) {
                                    scope.launch {
                                        viewModel.loginWithPasscode(passcode.value)
                                    }
                                }
                            })
                ) {
                    when (i) {
                        11 -> {
                            Image(
                                painter = painterResource(
                                    id =
                                    R.drawable.keyboard_backspace
                                ), null
                            )
                        }

                        9 -> {
                            Image(
                                painter = painterResource(
                                    id =
                                    R.drawable.fingerprint
                                ), null
                            )
                        }

                        10 -> {
                            Text(
                                text = "0", fontSize = 30.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        else -> {
                            Text(
                                text = "${i + 1}", fontSize = 30.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            CustomButton(text = "Forgot Passcode") {
                navController.navigate(Destinations.Home.route)
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        if (state.value.isLoading) LoadingDialog()
    }
}