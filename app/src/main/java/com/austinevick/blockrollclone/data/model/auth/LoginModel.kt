package com.austinevick.blockrollclone.data.model.auth

import com.google.gson.annotations.SerializedName

data class LoginModel(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)