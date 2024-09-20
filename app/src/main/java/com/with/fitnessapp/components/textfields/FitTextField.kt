package com.with.fitnessApp.components.textfields

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FitTextField(value: String,
                onValueChange:  (String) -> Unit,
                label: @Composable() (() -> Unit)? = null,
                placeholder: @Composable() (() -> Unit)? = null,
                prefix: @Composable() (() -> Unit)? = null,
                suffix: @Composable() (() -> Unit)? = null,
                enable: Boolean = true,
                readOnly: Boolean = false,
                isError: Boolean = false,
){
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth()
                            .padding(16.dp),
        value = value,
        onValueChange = onValueChange,
        label = label,
        prefix = prefix,
        suffix = suffix,
        enabled = enable,
        readOnly = readOnly,
        isError = isError,
        singleLine = true
    )
}