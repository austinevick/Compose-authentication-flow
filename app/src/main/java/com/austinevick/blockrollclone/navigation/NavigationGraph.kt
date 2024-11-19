package com.austinevick.blockrollclone.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.austinevick.blockrollclone.common.Constants
import com.austinevick.blockrollclone.view.auth.AuthNotifierScreen
import com.austinevick.blockrollclone.view.auth.ConfirmPasscodeScreen
import com.austinevick.blockrollclone.view.auth.CreatePasscodeScreen
import com.austinevick.blockrollclone.view.auth.EmailVerificationScreen
import com.austinevick.blockrollclone.view.auth.LoginScreen
import com.austinevick.blockrollclone.view.auth.PasscodeLoginScreen
import com.austinevick.blockrollclone.view.auth.RegisterScreen
import com.austinevick.blockrollclone.view.auth.UsernameScreen
import com.austinevick.blockrollclone.view.auth.viewmodel.LoginData
import com.austinevick.blockrollclone.view.home.HomeScreen
import com.google.gson.Gson

@Composable
fun NavigationGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Destinations.AuthStateNotifier.route
    ) {

        composable(Destinations.AuthStateNotifier.route) {
            AuthNotifierScreen(navController)
        }

        composable(Destinations.Login.route) {
            LoginScreen(navController)
        }
        composable(Destinations.Signup.route) {
            RegisterScreen(navController)
        }
        composable(
            Destinations.EmailVerification.route + "/{${Constants.emailAndPasswordKey}}",
            arguments = listOf(navArgument(Constants.emailAndPasswordKey)
            { type = NavType.StringType })
        ) { entry ->
            val jsonData = entry.arguments?.getString(Constants.emailAndPasswordKey)
            val loginData = Gson().fromJson(jsonData, LoginData::class.java)
            EmailVerificationScreen(navController, loginData)
        }
        composable(Destinations.CreateUsername.route) {
            UsernameScreen(navController)
        }
        composable(Destinations.PasscodeLogin.route) {
            PasscodeLoginScreen(navController)
        }
        composable(Destinations.CreatePasscode.route) {
            CreatePasscodeScreen(navController)
        }
        composable(Destinations.ConfirmPasscode.route) {
            val passcode = navController.previousBackStackEntry?.savedStateHandle
                ?.get<String>(Constants.passcodeKey)
            ConfirmPasscodeScreen(navController = navController, passcode = passcode.toString())
        }
        composable(Destinations.Home.route) {
            HomeScreen()
        }

    }
}