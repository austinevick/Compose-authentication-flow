package com.austinevick.blockrollclone.view.auth.viewmodel

import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import com.austinevick.blockrollclone.common.UiState
import com.austinevick.blockrollclone.data.model.GeneralResponseModel
import com.austinevick.blockrollclone.data.model.auth.GenerateTrustTokenModel
import com.austinevick.blockrollclone.data.model.auth.PasscodeLoginModel
import com.austinevick.blockrollclone.data.model.auth.PasscodeModel
import com.austinevick.blockrollclone.data.model.auth.TrustTokenResponseModel
import com.austinevick.blockrollclone.data.model.auth.UniqueData
import com.austinevick.blockrollclone.data.source.local.DataStore
import com.austinevick.blockrollclone.data.source.local.DataStore.Companion.token
import com.austinevick.blockrollclone.data.source.local.DataStore.Companion.trustToken
import com.austinevick.blockrollclone.data.source.remote.AuthRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class PasscodeViewModel @Inject constructor(
    private val dataStore: DataStore,
    private val authRepository: AuthRepository
) : ViewModel() {

    val digits = 4
    var passcode = MutableStateFlow("")
        private set
    var passcodeState = MutableStateFlow(UiState())
        private set


    private fun getTrustToken(): String {
        return runBlocking {
            dataStore.getPreference(trustToken, "")
        }
    }


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


    suspend fun generateTrustToken() {
        try {
            val uniqueData = UniqueData(mMIE = Build.ID, deviceOS = "Android", model = Build.MODEL)
            val model = GenerateTrustTokenModel(uniqueData)
            val response = authRepository.generateTrustToken(model)
            if (response.isSuccessful) {
                val json = Gson().toJson(response.body())
                val successBody =
                    Gson().fromJson(json, TrustTokenResponseModel::class.java)
                dataStore.putPreference(trustToken, successBody.data.token)
                Log.d("Response", successBody.toString())
            }
            Log.i("UniqueData", uniqueData.toString())
        } catch (e: Exception) {
            Log.d("Error", e.message.toString())
        }
    }

    suspend fun createPasscode(passcode: String) {
        try {
            passcodeState.update { UiState(isLoading = true) }
            val response = authRepository.createPasscode(PasscodeModel(passcode))
            if (response.isSuccessful) {
                val successBody =
                    Gson().fromJson(response.body().toString(), GeneralResponseModel::class.java)
                Log.d("SuccessBody", successBody.toString())
            } else {
                val errorBody = response.errorBody()?.charStream()?.readText()
                val error = Gson().fromJson(errorBody.toString(), GeneralResponseModel::class.java)
                Log.d("ErrorBody", error.message)
                passcodeState.update {
                    UiState(
                        statusCode = error.code,
                        message = error.message
                    )
                }
            }
        } catch (e: Exception) {
            passcodeState.update {
                UiState(isLoading = false, message = e.message)
            }
        }
    }

    suspend fun loginWithPasscode(passcode: String) {
        try {
            passcodeState.update { UiState(isLoading = true) }
            val uniqueData = UniqueData(mMIE = Build.ID, deviceOS = "Android", model = Build.MODEL)
            val model = PasscodeLoginModel(
                id = getTrustToken(),
                passcode = passcode,
                uniqueData = uniqueData
            )
            val response = authRepository.passcodeLogin(model)
            Log.d("UniqueData", model.toString())
            if (response.isSuccessful) {
                passcodeState.update {
                    UiState(statusCode = response.code())
                }
                dataStore.putPreference(token, response.body()?.data?.token.toString())
                Log.d("SuccessBody", response.body().toString())
            } else {
                val errorBody = response.errorBody()?.charStream()?.readText()
                val error = Gson().fromJson(errorBody.toString(), GeneralResponseModel::class.java)
                Log.d("ErrorBody", error.message)
                passcodeState.update {
                    UiState(
                        isLoading = false,
                        statusCode = error.code,
                        message = error.message
                    )
                }
            }
        } catch (e: HttpException) {
            Log.d("ErrorBody", e.response()?.errorBody().toString())
            passcodeState.update {
                UiState(isLoading = false)
            }
        }
    }


    suspend fun loginWithBiometric() {
        try {
            passcodeState.update { UiState(isLoading = true) }
            val uniqueData = UniqueData(mMIE = Build.ID, deviceOS = "Android", model = Build.MODEL)
            val model = PasscodeLoginModel(
                id = dataStore.getPreference(trustToken, ""),
                passcode = passcode.value,
                uniqueData = uniqueData
            )
            val response = authRepository.biometricLogin(model)
            if (response.isSuccessful) {
                passcodeState.update {
                    UiState(
                        statusCode = response.code()
                    )
                }
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.charStream()?.readText()
            val error = Gson().fromJson(errorBody.toString(), GeneralResponseModel::class.java)
            Log.d("ErrorBody", error.message)
            passcodeState.update {
                UiState(
                    isLoading = false,
                    statusCode = error.code,
                    message = error.message
                )
            }
        }
    }


}

