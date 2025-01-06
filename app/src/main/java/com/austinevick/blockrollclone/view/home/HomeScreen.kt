package com.austinevick.blockrollclone.view.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.fragment.compose.AndroidFragment
import androidx.hilt.navigation.compose.hiltViewModel
import com.austinevick.blockrollclone.view.auth.viewmodel.LoginViewModel
import com.austinevick.blockrollclone.view.auth.viewmodel.PasscodeViewModel

@Composable
fun HomeScreen(viewModel: PasscodeViewModel = hiltViewModel()) {



    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {

            AndroidFragment(HomeFragment::class.java)
        }
    }

}