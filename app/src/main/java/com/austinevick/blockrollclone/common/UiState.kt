package com.austinevick.blockrollclone.common


data class UiState(
    val statusCode: Int? = null,
    val isLoading: Boolean = false,
    val message: String? = null,
    val data: Any? = null
)