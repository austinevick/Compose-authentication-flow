package com.austinevick.blockrollclone.view.auth.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.austinevick.blockrollclone.common.UiState
import com.austinevick.blockrollclone.data.model.GeneralResponseModel
import com.austinevick.blockrollclone.data.model.auth.PasscodeModel
import com.austinevick.blockrollclone.data.source.remote.repository.AuthRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class PasscodeViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    val digits = 4
    var passcode = MutableStateFlow("")
        private set
    var passcodeState = MutableStateFlow(UiState())
        private set

    fun onButtonClick(i: Int) {
        when (i) {
            11 -> {
                // Delete passcode starting from the last digit
                if (passcode.value.isNotBlank())
                    passcode.value = passcode.value.substring(0, passcode.value.length - 1)
            }

            10 -> {
                if (passcode.value.length == digits) return
                passcode.value += (0).toString()
            }

            else -> {
                if (passcode.value.length == digits) return
                passcode.value += (i + 1).toString()
            }
        }
    }

    suspend fun createPasscode(passcode: String) {
        try {
            passcodeState.update { UiState(isLoading = true) }
            val response = authRepository.createPasscode(PasscodeModel(passcode))
            if (response.isSuccessful) {
                val successBody =
                    Gson().fromJson(response.body().toString(), GeneralResponseModel::class.java)
                Log.d("SuccessBody",successBody.toString())
            } else {
                val errorBody = response.errorBody()?.charStream()?.readText()
                val error = Gson().fromJson(errorBody.toString(), GeneralResponseModel::class.java)
                Log.d("ErrorBody",error.message)
                passcodeState.update { UiState(
                    statusCode = error.code,
                    message = error.message
                ) }
            }
        } catch (e: Exception) {
            passcodeState.update {
                UiState(isLoading = false, message = e.message)
            }
        }
    }
}

