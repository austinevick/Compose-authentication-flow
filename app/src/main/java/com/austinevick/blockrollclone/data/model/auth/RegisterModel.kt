package com.austinevick.blockrollclone.data.model.auth

import com.google.gson.annotations.SerializedName

data class RegisterModel(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("phoneNumber") val phoneNumber: String,
    @SerializedName("referrerUsername") val referrerUsername: String,
)
