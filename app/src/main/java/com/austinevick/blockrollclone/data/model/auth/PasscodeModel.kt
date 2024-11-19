package com.austinevick.blockrollclone.data.model.auth

import com.google.gson.annotations.SerializedName

data class PasscodeModel(
    @SerializedName("passcode") val passcode: String
)
