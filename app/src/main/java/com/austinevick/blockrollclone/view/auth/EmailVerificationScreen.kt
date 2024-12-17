package com.austinevick.blockrollclone.view.auth

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.austinevick.blockrollclone.components.CustomButton
import com.austinevick.blockrollclone.data.model.GeneralResponseModel
import com.austinevick.blockrollclone.data.model.auth.ValidateEmailModel
import com.austinevick.blockrollclone.navigation.Destinations
import com.austinevick.blockrollclone.view.auth.viewmodel.LoginData
import com.austinevick.blockrollclone.view.auth.viewmodel.VerificationViewModel
import kotlinx.coroutines.launch

@Composable
fun EmailVerificationScreen(
    navController: NavHostController,
    loginData: LoginData,
    viewModel: VerificationViewModel = hiltViewModel()
) {

    Log.d("loginData", loginData.toString())

    val otpLength = 6

    val otpValues =
        remember { mutableStateListOf(*Array(otpLength) { "" }) }
    val focusRequesters = List(otpLength) { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState) {
        focusRequesters.first().requestFocus()
        if (uiState.message != null) {
            snackbarHostState.showSnackbar(uiState.message!!)
        }

        if (uiState.data != null){
            val data = uiState.data as GeneralResponseModel

            if (data.data?.username?.isEmpty()==true){
                navController.navigate(Destinations.CreateUsername.route)
            }else if(data.data?.hasPasscode==false){
                navController.navigate(Destinations.CreatePasscode.route)
            } else{
                navController.navigate(Destinations.Home.route){
                    popUpTo(Destinations.Login.route){
                        inclusive=true
                    }
            }
        }}
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(25.dp))
            Text("Please check your email and verify your account")
            Spacer(modifier = Modifier.height(30.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                otpValues.forEachIndexed { index, value ->

                    OutlinedTextField(
                        value = value,
                        onValueChange = { newValue ->
                            if (newValue.length <= 1) {
                                otpValues[index] = newValue
                                if (newValue.isNotEmpty()) {
                                    if (index < otpLength - 1) {
                                        focusRequesters[index + 1].requestFocus()
                                    } else {
                                        keyboardController?.hide()
                                    }
                                }
                            } else {
                                if (index < otpLength - 1) focusRequesters[index + 1].requestFocus()
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        textStyle = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            platformStyle = PlatformTextStyle(includeFontPadding = false)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .size(50.dp)
                            .focusRequester(focusRequesters[index])
                            .onKeyEvent { keyEvent ->
                                if (keyEvent.key == Key.Backspace) {
                                    if (otpValues[index].isEmpty() && index > 0) {
                                        otpValues[index] = ""
                                        focusRequesters[index - 1].requestFocus()
                                    } else {
                                        otpValues[index] = ""
                                    }
                                    true
                                } else {
                                    false
                                }
                            },
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            CustomButton(
                text = "Submit",
                isLoading = uiState.isLoading,
                enabled = !uiState.isLoading && otpValues.all { it.isNotEmpty() },
            ) {
                scope.launch {
                    val model = ValidateEmailModel(
                        email = loginData.email,
                        password = loginData.password, code = otpValues.joinToString("")
                    )
                    viewModel.verifyEmail(model)
                }
            }

        }
    }

}
