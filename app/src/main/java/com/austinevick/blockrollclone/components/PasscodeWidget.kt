package com.austinevick.blockrollclone.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.austinevick.blockrollclone.R
import com.austinevick.blockrollclone.view.auth.viewmodel.PasscodeViewModel

@Composable
fun PasscodeWidget(
    viewModel: PasscodeViewModel = hiltViewModel(),
    snackbarHost: @Composable () -> Unit = {},
    title: String,
) {
    val passcode = viewModel.passcode.collectAsStateWithLifecycle()


    Scaffold(
        snackbarHost = snackbarHost,
        containerColor = Color.White) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(20.dp))
            Row {
                repeat(4) { i ->
                    val isActive = passcode.value.length > i
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .background(Color.LightGray.copy(0.2f), CircleShape)
                            .padding(10.dp)
                            .size(40.dp)
                    ) {
                        Text(
                            if (isActive) passcode.value[i].toString() else "",
                            fontWeight = FontWeight.Bold, fontSize = 28.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
            NonLazyGrid(columns = 3, itemCount = 12) { i ->

                when (i) {
                    11 -> {
                        if (passcode.value.isNotEmpty())
                            BuildButton(i, onClick = {
                                viewModel.onButtonClick(i)
                            })
                    }

                    9 -> {}

                    10 -> {
                        BuildButton(i, onClick = {
                            viewModel.onButtonClick(i)
                        })
                    }

                    else -> {
                        BuildButton(i, onClick = {
                            viewModel.onButtonClick(i)

                        })
                    }

                }

            }

        }

    }
}

@Composable
fun BuildButton(
    i: Int,
    onClick: () -> Unit = {}
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(8.dp)
            .background(Color.LightGray.copy(0.2f), CircleShape)
            .size(70.dp)
            .clickable { onClick() }
    ) {
        when (i) {
            11 -> {
                Image(
                    painter = painterResource(
                        id =
                        R.drawable.keyboard_backspace
                    ), null
                )
            }

            10 -> {
                Text(
                    text = "0", fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            else -> {
                Text(
                    text = "${i + 1}", fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

