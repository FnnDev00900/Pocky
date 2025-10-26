package com.fnndev.pocky.ui.screens.bank.bank_account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.LayoutDirection
import com.fnndev.pocky.ui.theme.VazirFont

@Composable
fun ChangePasswordDialog(onConfirm: (String, String) -> Unit, onDismiss: () -> Unit) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "تغییر رمز عبور", fontFamily = VazirFont) },
        text = {
            CompositionLocalProvider(value = LocalLayoutDirection provides LayoutDirection.Rtl) {
                Column(modifier = Modifier.fillMaxWidth(0.95f)) {
                    OutlinedTextField(
                        value = currentPassword,
                        onValueChange = { currentPassword = it },
                        label = {
                            Text(text = "رمز عبور فعلی", fontFamily = VazirFont)
                        },
                        singleLine = true,
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        )
                    )
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = {
                            newPassword = it
                            isError = it != confirmPassword
                        },
                        label = {
                            Text(text = "رمز عبور جدید", fontFamily = VazirFont)
                        },
                        singleLine = true,
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        ),
                        isError = isError
                    )
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = {
                            confirmPassword = it
                            isError = newPassword != it
                        },
                        label = {
                            Text(text = "تکرار رمز عبور جدید", fontFamily = VazirFont)
                        },
                        singleLine = true,
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        isError = isError,
                        supportingText = {
                            if (isError) {
                                Text("رمزهای عبور یکسان نیستند", fontFamily = VazirFont)
                            }
                        }
                    )
                }

            }
        },
        confirmButton = {
            OutlinedButton(
                onClick = {
                    if (newPassword == confirmPassword) {
                        onConfirm(currentPassword, newPassword)
                    } else {
                        isError = true
                    }
                },
                enabled = newPassword.isNotEmpty() && confirmPassword.isNotEmpty()
            ) {
                Text(text = "ذخیره تغییرات", fontFamily = VazirFont)
            }
        },

        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = "لغو", fontFamily = VazirFont)
            }
        },
    )
}
