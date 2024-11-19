package com.austinevick.blockrollclone.data.model.auth


import com.google.gson.annotations.SerializedName

data class ValidateEmailModel(
    @SerializedName("code")
    val code: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)