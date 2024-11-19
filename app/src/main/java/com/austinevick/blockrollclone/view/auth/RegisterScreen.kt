package com.austinevick.blockrollclone.view.auth

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.austinevick.blockrollclone.R
import com.austinevick.blockrollclone.common.isValidPassword
import com.austinevick.blockrollclone.components.BlockrollTopAppBar
import com.austinevick.blockrollclone.components.CustomButton
import com.austinevick.blockrollclone.components.CustomTextField
import com.austinevick.blockrollclone.navigation.Destinations
import com.austinevick.blockrollclone.view.auth.viewmodel.RegisterViewModel
import com.austinevick.blockrollclone.view.auth.viewmodel.SignupDetails
import com.austinevick.blockrollclone.view.auth.viewmodel.SignupUiState
import com.google.gson.Gson
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavHostController,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val signupUiState by viewModel.signupUiState.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }


    LaunchedEffect(key1 = uiState) {
        if (uiState.message != null) {
            snackbarHostState.showSnackbar(message = uiState.message.toString())
            if (uiState.message == "User already exists"||uiState.statusCode==200) {
                val details = SignupDetails(
                    signupUiState.signupDetails.email,
                    signupUiState.signupDetails.password
                )
                val json = Uri.encode(Gson().toJson(details))
                navController.navigate(Destinations.EmailVerification.route + "/$json")
            }
        }

    }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = Color.White,
        topBar = {
            BlockrollTopAppBar(navController = navController)
        }
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
            SignupForm(
                signupUiState = signupUiState,
                onPasswordToggle = viewModel::togglePassword,
                showPassword = viewModel.showPassword,
                emailErrorMessage = signupUiState.emailErrorMessage,
                isEmailError = signupUiState.isValidEmail,
                passwordErrorMessage = signupUiState.passwordErrorMessage,
                isPasswordError = signupUiState.isValidPassword,
                onEmailValueChange = { viewModel.onEmailValueChanged(it) },
                onPasswordValueChange = { viewModel.onPasswordChanged(it) },
                onPhoneValueChange = { viewModel.onPhoneNumberChanged(it) },
                phoneErrorMessage = signupUiState.phoneErrorMessage,
                isPhoneError = signupUiState.isValidPhoneNumber,
            )
            Spacer(modifier = Modifier.height(16.dp))
            CustomButton(
                text = "Register",
                isLoading = uiState.isLoading,
                enabled = !uiState.isLoading && !viewModel.isValidInput(
                    signupDetails =
                    signupUiState.signupDetails
                ),
            ) {
                scope.launch {
                    focusManager.clearFocus()
                    viewModel.signup()
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SignupForm(
    signupUiState: SignupUiState,
    onPasswordToggle: () -> Unit,
    showPassword: Boolean,
    emailErrorMessage: String,
    isEmailError: Boolean,
    passwordErrorMessage: String,
    isPasswordError: Boolean,
    phoneErrorMessage: String,
    isPhoneError: Boolean,
    onEmailValueChange: (SignupDetails) -> Unit = {},
    onPasswordValueChange: (SignupDetails) -> Unit = {},
    onPhoneValueChange: (SignupDetails) -> Unit = {}
) {
    CustomTextField(
        value = signupUiState.signupDetails.email,
        errorText = emailErrorMessage,
        isError = isEmailError,
        keyboardType = KeyboardType.Email,
        capitalization = KeyboardCapitalization.None,
        imeAction = ImeAction.Next,
        onValueChange = {
            onEmailValueChange(
                signupUiState
                    .signupDetails.copy(email = it)
            )
        },
        placeholder = "Email",
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = null
            )
        }
    )
    Spacer(modifier = Modifier.height(10.dp))
    CustomTextField(
        value = signupUiState.signupDetails.phoneNumber,
        errorText = phoneErrorMessage,
        isError = isPhoneError,
        keyboardType = KeyboardType.Phone,
        imeAction = ImeAction.Next,
        onValueChange = {
            onPhoneValueChange(
                signupUiState
                    .signupDetails.copy(phoneNumber = it)
            )
        },
        placeholder = "Phone number",
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Phone,
                contentDescription = null
            )
        }
    )
    Spacer(modifier = Modifier.height(10.dp))
    CustomTextField(
        value = signupUiState.signupDetails.password,
        errorText = passwordErrorMessage,
        isError = isPasswordError,
        onValueChange = {
            onPasswordValueChange(
                signupUiState
                    .signupDetails.copy(password = it)
            )
        },
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
                        id = if (!showPassword) R.drawable.visibility_off else
                            R.drawable.visibility
                    ),
                    contentDescription = null
                )
            }
        }
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
        "Password must be a minimum of 8 characters, with letters, numbers, and a symbol.",
        fontSize = 12.sp,
        style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
        color = if (isValidPassword(signupUiState.signupDetails.password))
            Color.Green else Color.Red
    )
}