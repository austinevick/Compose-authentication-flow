package com.austinevick.blockrollclone.view.auth.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.austinevick.blockrollclone.common.UiState
import com.austinevick.blockrollclone.common.isValidEmail
import com.austinevick.blockrollclone.common.validateEmail
import com.austinevick.blockrollclone.common.validateField
import com.austinevick.blockrollclone.data.model.auth.LoginModel
import com.austinevick.blockrollclone.data.source.remote.repository.AuthRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var uiState = MutableStateFlow(UiState())
        private set

    var loginUiState = mutableStateOf(LoginUiState())
        private set

    var showPassword by mutableStateOf(false)
        private set

    fun togglePassword() {
        showPassword = !showPassword
    }

    suspend fun login() {
        try {
            uiState.update { UiState(isLoading = true)}
            val model = LoginModel(
                email = loginUiState.value.loginData.email,
                password = loginUiState.value.loginData.password
            )
            val response = authRepository.login(model)
            if (response.code() == 200) {
                val successBody = Gson().toJsonTree(response.body()).asJsonObject
                Log.d("Auth success", "login: ${successBody.get("message")}")
                uiState.value =
                    UiState(
                        isLoading = false,
                        statusCode = response.code(),
                        message = successBody.get("message").toString())
            } else {
                val errorBody =
                    response.errorBody()?.charStream()?.readText()?.let { JSONObject(it) }
                if (errorBody != null) {
                    Log.d("Auth error", "login: ${errorBody.get("message")}")
                }
                if (errorBody != null) {
                    uiState.value =
                        UiState(
                            isLoading = false,
                            statusCode = response.code(),
                            data = errorBody,
                            message = errorBody.get("message").toString())
                }
            }
        } catch (e: Exception) {
            uiState.value =
                UiState(
                    isLoading = false,
                    message = e.message)
            throw e
        }
    }

    fun onEmailValueChanged(loginData: LoginData = loginUiState.value.loginData) {
        loginUiState.value = LoginUiState(
            loginData = loginData,
            isValidEmail = loginData.email.isBlank() && !isValidEmail(loginData.email),
            emailErrorMessage = validateEmail(loginData.email)
        )
    }

    fun onPasswordChanged(loginData: LoginData = loginUiState.value.loginData){
        loginUiState.value = LoginUiState(
            loginData = loginData,
            isValidPassword = loginData.password.trim().isBlank(),
            passwordErrorMessage = validateField(loginData.password)
        )
    }
    fun isValidInput(loginData: LoginData = loginUiState.value.loginData): Boolean {
        return loginData.email.isBlank() || !isValidEmail(loginData.email) || loginData.password.isBlank()
    }

}

data class LoginUiState(
    var loginData: LoginData = LoginData(),
    var isValidEmail: Boolean = false,
    var isValidPassword: Boolean = false,
    var emailErrorMessage: String = "",
    var passwordErrorMessage: String = ""
)

data class LoginData(
    var email: String = "",
    var password: String = "")
