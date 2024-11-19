package com.austinevick.blockrollclone.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    readOnly: Boolean = false,
    maxLines: Int = 1,
    singleLine: Boolean = true,
    isError: Boolean = false,
    errorText: String = "",
    errorTextColor: Color = Color.Red,
    imeAction: ImeAction =ImeAction.Unspecified,
    textStyle: TextStyle = TextStyle.Default,
    keyboardType: KeyboardType = KeyboardType.Text,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.Sentences,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Column {
        OutlinedTextField(
            value = value, onValueChange = onValueChange,
            placeholder = { Text(text = placeholder, color = Color.LightGray) },
            shape = RoundedCornerShape(8.dp),
            readOnly = readOnly,
            textStyle = textStyle.copy(color = Color.Black,
                fontWeight = FontWeight.Medium),
            keyboardOptions = KeyboardOptions(
                capitalization = capitalization,
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            visualTransformation = visualTransformation,
            singleLine = singleLine, maxLines = maxLines,
            isError = isError,
            modifier = modifier.fillMaxWidth(),
            trailingIcon = trailingIcon,
            leadingIcon =leadingIcon,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Gray,
                unfocusedIndicatorColor = Color.Gray,
                unfocusedContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
            )
        )
        if (isError) Text(text = errorText,
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 16.dp),
            style = TextStyle(platformStyle =
            PlatformTextStyle(includeFontPadding = false)),
            color = errorTextColor)
    }

}