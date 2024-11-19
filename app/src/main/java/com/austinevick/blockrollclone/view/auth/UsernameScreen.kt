package com.austinevick.blockrollclone.view.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.austinevick.blockrollclone.components.CustomButton
import com.austinevick.blockrollclone.components.CustomTextField
import com.austinevick.blockrollclone.R
import com.austinevick.blockrollclone.navigation.Destinations
import com.austinevick.blockrollclone.view.auth.viewmodel.UsernameViewModel
import kotlinx.coroutines.launch

@Composable
fun UsernameScreen(
    navController: NavHostController,
    viewModel: UsernameViewModel = hiltViewModel()
) {

    val usernameAvailabilityState = viewModel.usernameAvailabilityState.collectAsState()
    val usernameState = viewModel.usernameState.collectAsState()
    val username = remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()


    LaunchedEffect(usernameState.value) {
        if (usernameState.value.statusCode == 200) {
            navController.navigate(Destinations.CreatePasscode.route)
        }
    }

    Scaffold(
        containerColor = Color.White,
        snackbarHost =
        { SnackbarHost(snackbarHostState) }) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .imePadding()
                .padding(innerPadding)
        ) {
            Text("Create your username")
            Spacer(modifier = Modifier.height(6.dp))
            CustomTextField(
                value = username.value,
                placeholder = "Enter username",
                onValueChange = {
                    username.value = it
                    scope.launch {
                        viewModel.checkUsernameAvailability(it)
                    }
                },
                leadingIcon = {
                    Image(painterResource(R.drawable.alternate_email), "")
                },
                trailingIcon = {
                    if (usernameAvailabilityState.value.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 3.dp
                        )
                    }
                },
                isError = username.value.isNotBlank(),
                errorTextColor = if (usernameAvailabilityState
                        .value.statusCode == 200
                ) Color.Green else Color.Red,
                errorText = usernameAvailabilityState.value.message ?: ""
            )
            Spacer(modifier = Modifier.height(25.dp))

            CustomButton(
                isLoading = usernameState.value.isLoading,
                enabled = usernameAvailabilityState.value.statusCode == 200,
                text = "Submit"
            ) {
                scope.launch {
                    if (usernameAvailabilityState.value.statusCode == 200) {
                        viewModel.createUsername(username.value)
                    }
                }
            }
        }
    }
}