package com.austinevick.blockrollclone.data.model.auth


import com.google.gson.annotations.SerializedName

data class PasscodeLoginModel(
    @SerializedName("id")
    val id: String,
    @SerializedName("passcode")
    val passcode: String,
    @SerializedName("uniqueData")
    val uniqueData: UniqueData
)


data class UniqueData(
    @SerializedName("MMIE") val mMIE: String,
    @SerializedName("deviceOS") val deviceOS: String,
    @SerializedName("model") val model: String,
)