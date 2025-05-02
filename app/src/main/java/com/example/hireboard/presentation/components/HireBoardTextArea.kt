package com.example.hireboard.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HireBoardTextArea(
    value: MutableState<TextFieldValue>,
    onValueChange: (TextFieldValue) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isRequired: Boolean = false,
    backgroundColor: Color = MaterialTheme.colorScheme.surface
) {
    Column {
        OutlinedTextField(
            value = value.value,
            onValueChange = onValueChange,
            label = {
                Text(
                    text = "$label${if (isRequired) " *" else ""}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isRequired) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                )
            },
            singleLine = false,
            shape = MaterialTheme.shapes.medium,
            colors = MaterialTheme.colorScheme.let { colors ->
                OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.primary,
                    unfocusedBorderColor = colors.onSurfaceVariant,
                    cursorColor = colors.primary,
                    focusedLabelColor = colors.primary,
                    unfocusedLabelColor = colors.onSurfaceVariant,
                    focusedTextColor = colors.onSurface,
                    unfocusedTextColor = colors.onSurface,
                    focusedContainerColor = backgroundColor,
                    unfocusedContainerColor = backgroundColor
                )
            },
            modifier = modifier
                .fillMaxWidth()
                .height(120.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HireBoardTextAreaPreview() {
    // Remember the state for the preview
    val state = remember { mutableStateOf(TextFieldValue("")) }

    Surface(color = MaterialTheme.colorScheme.background) {
        HireBoardTextArea(
            value = state,
            onValueChange = { state.value = it },
            label = "Введите текст",
            isRequired = false
        )
    }
}