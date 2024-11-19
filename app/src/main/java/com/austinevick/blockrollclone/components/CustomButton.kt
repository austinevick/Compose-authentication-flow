package com.austinevick.blockrollclone.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
fun CustomButton(
    isLoading: Boolean = false,
    enabled: Boolean = true,
    text: String = "",
    onClick: () -> Unit
) {
    val localConfig = LocalConfiguration.current

    Button(
        enabled = enabled,
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .width(localConfig.screenWidthDp.dp)
            .height(50.dp)
    ) {
        if (isLoading) CircularProgressIndicator(
            modifier = Modifier.size(24.dp),
            strokeWidth = 3.dp,
            color = Color.White)
        else Text(text = text)
    }
}