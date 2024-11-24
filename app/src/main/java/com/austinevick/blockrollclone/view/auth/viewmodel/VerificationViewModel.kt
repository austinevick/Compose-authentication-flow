package com.austinevick.blockrollclone.view.auth.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.austinevick.blockrollclone.common.UiState
import com.austinevick.blockrollclone.data.model.GeneralResponseModel
import com.austinevick.blockrollclone.data.model.auth.ValidateEmailModel
import com.austinevick.blockrollclone.data.source.DataStorePreferences
import com.austinevick.blockrollclone.data.source.DataStorePreferences.Companion.hasPasscode
import com.austinevick.blockrollclone.data.source.DataStorePreferences.Companion.username
import com.austinevick.blockrollclone.data.source.remote.repository.AuthRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class VerificationViewModel @Inject constructor(
   private val dataStore: DataStorePreferences,
    private val authRepository: AuthRepository
) : ViewModel() {

    private var _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    var passcodeValue = MutableStateFlow(false)
        private set
init {
    getUserPasscodeValue()
}

    private fun getUserPasscodeValue() {
        viewModelScope.launch {
       passcodeValue.value = dataStore.getPreference(hasPasscode, false)
        }
    }

    suspend fun verifyEmail(model: ValidateEmailModel) {
        try {
            _uiState.update { UiState(isLoading = true) }
            val response = authRepository.validateEmail(model)
            if (response.code() == 200) {
                val successBody = Gson().toJsonTree(response.body()).asJsonObject
                val data = Gson().fromJson(successBody,GeneralResponseModel::class.java)

                dataStore.putPreference(hasPasscode, data.data?.hasPasscode!!)
                dataStore.putPreference(username, data.data.username!!)
                Log.d("success", data.toString())

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    message = data.message,
                    data = data,
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
