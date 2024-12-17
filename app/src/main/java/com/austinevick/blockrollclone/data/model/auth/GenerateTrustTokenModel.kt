package com.austinevick.blockrollclone.data.model.auth

import com.google.gson.annotations.SerializedName

data class GenerateTrustTokenModel(
 @SerializedName("uniqueData") val uniqueData: UniqueData
)

data class TrustTokenResponseModel(
 @SerializedName("code") val code: String,
 @SerializedName("message") val message: String,
 @SerializedName("status") val status: String,
 @SerializedName("data") val data: TrustTokenResponseData
)

data class TrustTokenResponseData(
 @SerializedName("id") val token: String
)