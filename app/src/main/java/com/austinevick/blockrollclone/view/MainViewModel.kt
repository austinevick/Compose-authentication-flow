package com.austinevick.blockrollclone.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {

    private val _counterState = MutableStateFlow(0)
    val counterState = _counterState

    fun incrementCounter() {
        _counterState.value++
    }

    fun decrementCounter() {
        _counterState.value--
    }
}