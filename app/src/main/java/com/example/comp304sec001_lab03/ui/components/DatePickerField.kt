package com.example.comp304sec001_lab03.ui.components

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import java.util.*

@Composable
fun DatePickerField(
    label: String = "Release Date (YYYY-MM-DD)",
    dateText: String,
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, day ->
            val formatted = String.format("%04d-%02d-%02d", year, month + 1, day)
            onDateSelected(formatted)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    OutlinedTextField(
        value = dateText,
        onValueChange = { },
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { datePickerDialog.show() },
        enabled = false,
        readOnly = true
    )
}
