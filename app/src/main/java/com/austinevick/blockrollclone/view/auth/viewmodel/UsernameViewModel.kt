package com.austinevick.blockrollclone.view.auth.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.austinevick.blockrollclone.common.UiState
import com.austinevick.blockrollclone.data.model.auth.UsernameModel
import com.austinevick.blockrollclone.data.source.remote.AuthRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class UsernameViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    var usernameAvailabilityState = MutableStateFlow(UiState())
        private set

    var usernameState = MutableStateFlow(UiState())
        private set


    suspend fun checkUsernameAvailability(username: String) {
        try {
            usernameAvailabilityState.value = UiState(isLoading = true)

            val response = repository.checkUsernameAvailability(username)
            if (response.code() == 200) {
                val successBody = Gson().toJsonTree(response.body()).asJsonObject
                Log.d("SuccessBody", "${successBody.get("message")}")
                usernameAvailabilityState.value =
                    UiState(
                        isLoading = false,
                        statusCode = response.code(),
                        message = successBody.get("message").toString()
                    )
            } else {
                val errorBody =
                    response.errorBody()?.charStream()?.readText()?.let { JSONObject(it) }
                if (errorBody != null) {
                    Log.d("ErrorBody", "${errorBody.get("message")}")
                }
                if (errorBody != null) {
                    usernameAvailabilityState.value =
                        UiState(
                            isLoading = false,
                            statusCode = response.code(),
                            data = errorBody,
                            message = errorBody.get("message").toString()
                        )
                }
            }
        } catch (e: Exception) {
            usernameAvailabilityState.value =
                UiState(isLoading = false, message = e.message)
            throw e
        }
    }

    suspend fun createUsername(username: String) {
        try {
            usernameState.value = UiState(isLoading = true)
            val response = repository.createUsername(UsernameModel(username))
            if (response.isSuccessful) {
                val successBody = Gson().toJsonTree(response.body()).asJsonObject
                Log.d("SuccessBody", "${successBody.get("message")}")
                usernameState.value =
                    UiState(
                        isLoading = false,
                        statusCode = response.code(),
                        message = successBody.get("message").toString()
                    )
            } else {
                val errorBody =
                    response.errorBody()?.charStream()?.readText()?.let { JSONObject(it) }

                Log.d("ErrorBody", "${errorBody?.get("message")}")
                usernameState.value =
                    UiState(
                        isLoading = false,
                        statusCode = response.code(),
                        data = errorBody,
                        message = errorBody?.get("message").toString()
                    )
            }
        } catch (e: Exception) {
            usernameState.value =
                UiState(isLoading = false, message = e.message)
        }
    }


}

