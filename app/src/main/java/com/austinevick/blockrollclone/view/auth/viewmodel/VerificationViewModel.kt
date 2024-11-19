package com.austinevick.blockrollclone.view.auth.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.austinevick.blockrollclone.common.UiState
import com.austinevick.blockrollclone.data.model.auth.ValidateEmailModel
import com.austinevick.blockrollclone.data.repository.AuthRepository
import com.austinevick.blockrollclone.preferences.PreferenceManager
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class VerificationViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager,
    private val authRepository: AuthRepository
) : ViewModel() {

    private var _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    var passcodeValue = MutableStateFlow(false)
        private set
init {
    getUserPasscodeValue()
}

    fun getUserPasscodeValue() {
        passcodeValue.value = preferenceManager.getUserPasscodeValue()
    }

    suspend fun verifyEmail(model: ValidateEmailModel) {
        try {
            _uiState.update { UiState(isLoading = true) }
            val response = authRepository.validateEmail(model)
            if (response.code() == 200) {
                val successBody = Gson().toJsonTree(response.body()).asJsonObject
                val data = successBody.get("data").asJsonObject
                val hasPasscode = data.get("has_passcode").asBoolean
                val username = data.get("username").toString()
                preferenceManager.saveUserPasscodeValue(hasPasscode)
                preferenceManager.saveUsername(username)
                Log.d("success", data.toString())

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    message = successBody.get("message").toString(),
                    data = successBody,
                    statusCode = response.code()
                )
            } else {
                val errorBody = JSONObject(response.errorBody()!!.charStream().readText())
                Log.d("errorBody", errorBody.toString())
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    data = errorBody,
                    message = errorBody.getString("message"),
                    statusCode = response.code()
                )
            }
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                message = e.message.toString()
            )
        }
    }
}
