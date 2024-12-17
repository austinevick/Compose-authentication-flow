package com.austinevick.blockrollclone.view.auth

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.austinevick.blockrollclone.R
import com.austinevick.blockrollclone.common.Constants
import com.austinevick.blockrollclone.components.CustomButton
import com.austinevick.blockrollclone.components.CustomTextField
import com.austinevick.blockrollclone.navigation.Destinations
import com.austinevick.blockrollclone.view.auth.viewmodel.LoginData
import com.austinevick.blockrollclone.view.auth.viewmodel.LoginUiState
import com.austinevick.blockrollclone.view.auth.viewmodel.LoginViewModel
import com.google.gson.Gson
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val loginUiState = viewModel.loginUiState.value
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val snackbarHostState = remember(::SnackbarHostState)


    LaunchedEffect(key1 = uiState) {
        if (uiState.message != null)
            snackbarHostState.showSnackbar(message = uiState.message.toString())
       if (uiState.statusCode==200){
           val data = Uri.encode(Gson().toJson(loginUiState.loginData))
           navController.navigate(Destinations.EmailVerification.route+"/$data")
       }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding(),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                )
                .imePadding()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            LoginForm(
                viewModel = viewModel,
                navController = navController,
                loginUiState = loginUiState,
                onPasswordToggle = viewModel::togglePassword,
                showPassword = viewModel.showPassword,
                isEmailError = loginUiState.isValidEmail,
                emailErrorMessage = loginUiState.emailErrorMessage,
                isPasswordError = loginUiState.isValidPassword,
                passwordErrorMessage = loginUiState.passwordErrorMessage,
                onEmailValueChange = { viewModel.onEmailValueChanged(it) },
                onPasswordValueChange = { viewModel.onPasswordChanged(it) },
            )
            Spacer(modifier = Modifier.height(20.dp))

            CustomButton(
                enabled = !uiState.isLoading &&
                        !viewModel.isValidInput(loginData = loginUiState.loginData),
                isLoading = uiState.isLoading, text = "Login"

            ) {
                scope.launch {
                    focusManager.clearFocus()
                    viewModel.login()
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Don't have account?")
                Text(
                    text = "Sign up",
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .clickable { navController.navigate(Destinations.Signup.route) },
                    textDecoration = TextDecoration.Underline
                )

            }

        }
    }

}

@Composable
fun LoginForm(
    viewModel: LoginViewModel,
    navController: NavHostController,
    loginUiState: LoginUiState,
    onPasswordToggle: () -> Unit,
    showPassword: Boolean,
    emailErrorMessage: String,
    isEmailError: Boolean,
    passwordErrorMessage: String,
    isPasswordError: Boolean,
    onEmailValueChange: (LoginData) -> Unit,
    onPasswordValueChange: (LoginData) -> Unit,
) {

    Column(horizontalAlignment = Alignment.End) {
        CustomTextField(value = loginUiState.loginData.email,
            onValueChange = { onEmailValueChange(loginUiState.loginData.copy(email = it)) },
            placeholder = "Email",
            errorText = emailErrorMessage,
            isError = isEmailError,
            keyboardType = KeyboardType.Email,
            capitalization = KeyboardCapitalization.None,
            imeAction = ImeAction.Next,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null
                )
            })
        Spacer(modifier = Modifier.height(10.dp))
        CustomTextField(
            value = loginUiState.loginData.password,
            onValueChange = { onPasswordValueChange(loginUiState.loginData.copy(password = it)) },
            keyboardType = KeyboardType.Password,
            errorText = passwordErrorMessage,
            isError = isPasswordError,
            placeholder = "Password",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null
                )
            },
            visualTransformation = if (!showPassword) PasswordVisualTransformation()
            else VisualTransformation.None,
            trailingIcon = {
                IconButton(onClick = onPasswordToggle) {
                    Icon(
                        painterResource(
                            id =
                            if (!showPassword) R.drawable.visibility_off else
                                R.drawable.visibility
                        ),
                        contentDescription = null
                    )
                }
            }
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = viewModel.isChecked,
                onCheckedChange = { viewModel.toggleCheck(it) })
            Text(text = "Remember Me")

            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = {
                navController.navigate(Destinations.EmailVerification.route)
            }) {
                Text(text = "Forgot Password?")
            }
        }

    }


}