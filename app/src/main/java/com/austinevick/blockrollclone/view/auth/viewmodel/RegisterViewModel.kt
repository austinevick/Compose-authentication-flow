package com.austinevick.blockrollclone.view.auth.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.austinevick.blockrollclone.common.UiState
import com.austinevick.blockrollclone.common.isValidEmail
import com.austinevick.blockrollclone.common.isValidPassword
import com.austinevick.blockrollclone.common.validateEmail
import com.austinevick.blockrollclone.common.validateField
import com.austinevick.blockrollclone.data.model.auth.RegisterModel
import com.austinevick.blockrollclone.data.source.remote.AuthRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var uiState = MutableStateFlow(UiState())
        private set

    var signupUiState = MutableStateFlow(SignupUiState())
        private set

    var showPassword by mutableStateOf(false)
        private set

    fun togglePassword() {
        showPassword = !showPassword
    }


    suspend fun signup() {
        try {
            uiState.value = UiState(isLoading = true)
            val model = RegisterModel(
                email = signupUiState.value.signupDetails.email,
                password = signupUiState.value.signupDetails.password,
                phoneNumber = signupUiState.value.signupDetails.phoneNumber,
                referrerUsername = ""
            )
            val response = authRepository.signup(model)
            if (response.code() == 200) {
                val successBody = Gson().toJsonTree(response.body()).asJsonObject
                Log.d("Auth success", "${successBody.get("code")}")
                uiState.value =
                    UiState(
                        isLoading = false,
                        statusCode = response.code(),
                        data = successBody,
                        message = successBody.get("message").toString()
                    )
            } else {
                val errorBody =
                    response.errorBody()?.charStream()?.readText()?.let { JSONObject(it) }

                if (errorBody != null) {
                    uiState.value =
                        UiState(
                            isLoading = false,
                            data = errorBody,
                            statusCode = response.code(),
                            message = errorBody.get("message").toString()
                        )
                }
            }
        } catch (e: Exception) {
            uiState.value =
                UiState(isLoading = false, message = e.message)
            throw e
        }
    }

    fun onEmailValueChanged(
        signupDetails: SignupDetails =
            signupUiState.value.signupDetails
    ) {
        signupUiState.value = SignupUiState(
            signupDetails = signupDetails,
            isValidEmail = signupDetails.email.isBlank() && !isValidEmail(signupDetails.email),
            emailErrorMessage = validateEmail(signupDetails.email)
        )
    }

    fun onPhoneNumberChanged(
        signupDetails: SignupDetails =
            signupUiState.value.signupDetails
    ) {
        signupUiState.value = SignupUiState(
            signupDetails = signupDetails,
            isValidPhoneNumber = signupDetails.phoneNumber.trim().isBlank(),
            phoneErrorMessage = validateField(signupDetails.phoneNumber)
        )
    }

    fun onPasswordChanged(
        signupDetails: SignupDetails =
            signupUiState.value.signupDetails
    ) {
        signupUiState.value = SignupUiState(
            signupDetails = signupDetails,
            isValidPassword = signupDetails.password.trim().isBlank(),
            passwordErrorMessage = validateField(signupDetails.password)
        )
    }

    fun isValidInput(
        signupDetails: SignupDetails =
            signupUiState.value.signupDetails
    ): Boolean {
        return signupDetails.email.isBlank()
                || !isValidEmail(signupDetails.email)
                || signupDetails.password.isBlank()
                || signupDetails.phoneNumber.isBlank()
                || !isValidPassword(signupDetails.password)
    }


}

data class SignupUiState(
    val signupDetails: SignupDetails = SignupDetails(),
    val emailErrorMessage: String = "",
    var isValidEmail: Boolean = false,
    var isValidPhoneNumber: Boolean = false,
    var phoneErrorMessage: String = "",
    var passwordErrorMessage: String = "",
    var isValidPassword: Boolean = false,
)

data class SignupDetails(
    val email: String = "",
    val password: String = "",
    val phoneNumber: String = "",
    val referrerUsername: String = ""
)
