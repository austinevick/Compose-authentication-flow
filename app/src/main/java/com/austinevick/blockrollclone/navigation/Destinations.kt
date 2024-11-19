package com.austinevick.blockrollclone.navigation

enum class Destinations(val route: String) {
    AuthStateNotifier("auth_state_notifier"),
    Login("login"),
    Signup("signup"),
    EmailVerification("email_verification"),
    CreateUsername("username"),
    PasscodeLogin("passcode_login"),
    CreatePasscode("create_passcode"),
    ConfirmPasscode("confirm_passcode"),
    Home("home"),
}

