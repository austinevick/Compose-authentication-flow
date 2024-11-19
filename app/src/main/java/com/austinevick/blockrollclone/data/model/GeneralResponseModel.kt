package com.austinevick.blockrollclone.data.model


import com.google.gson.annotations.SerializedName

data class GeneralResponseModel(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: GeneralResponseData? = null
)

data class GeneralResponseData(
    @SerializedName("emailverified") val emailVerified: Boolean,
    @SerializedName("has_passcode") val hasPasscode: Boolean,
    @SerializedName("username") val username: String? = null,
)

