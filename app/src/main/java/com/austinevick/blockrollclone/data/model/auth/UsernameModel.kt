package com.austinevick.blockrollclone.data.model.auth

import com.google.gson.annotations.SerializedName

data class UsernameModel(
    @SerializedName("username") val username: String
)
