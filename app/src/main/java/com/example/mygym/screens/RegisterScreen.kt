package com.example.mygym.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mygym.components.DarkCard
import com.example.mygym.components.ModernTextField
import com.example.mygym.components.PrimaryButton
import com.example.mygym.components.SecondaryButton
import com.example.mygym.data.GymDatabaseHelper
import com.example.mygym.ui.theme.GymAccent
import com.example.mygym.ui.theme.GymRed
import com.example.mygym.ui.theme.GymWhite

@Composable
fun RegisterScreen(
    context: Context,
    onRegisterSuccess: () -> Unit,
    onBackClick: () -> Unit
) {
    val dbHelper = remember { GymDatabaseHelper(context) }

    val username = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val message = remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(70.dp).clip(RoundedCornerShape(18.dp)).background(GymAccent.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text("MG", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = GymAccent)
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text("TẠO TÀI KHOẢN MỚI", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = GymWhite)
            Spacer(modifier = Modifier.height(16.dp))

            DarkCard {
                ModernTextField(
                    value = username.value,
                    onValueChange = { username.value = it; message.value = "" },
                    label = "Tên tài khoản"
                )
                Spacer(modifier = Modifier.height(10.dp))

                ModernTextField(
                    value = email.value,
                    onValueChange = { email.value = it; message.value = "" },
                    label = "Địa chỉ Gmail (VD: user@gmail.com)"
                )
                Spacer(modifier = Modifier.height(10.dp))

                ModernTextField(
                    value = password.value,
                    onValueChange = { password.value = it; message.value = "" },
                    label = "Mật khẩu",
                    password = true
                )
                Spacer(modifier = Modifier.height(10.dp))

                ModernTextField(
                    value = confirmPassword.value,
                    onValueChange = { confirmPassword.value = it; message.value = "" },
                    label = "Nhập lại mật khẩu",
                    password = true
                )

                if (message.value.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(message.value, fontSize = 12.sp, color = GymRed, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                PrimaryButton(
                    text = "TẠO TÀI KHOẢN",
                    onClick = {
                        val userStr = username.value.trim()
                        val emailStr = email.value.trim()
                        val passStr = password.value
                        val confirmStr = confirmPassword.value

                        if (userStr.isBlank() || emailStr.isBlank() || passStr.isBlank() || confirmStr.isBlank()) {
                            message.value = "Vui lòng nhập đầy đủ Tên, Gmail & Mật khẩu"
                        } else if (!emailStr.contains("@")) {
                            message.value = "Định dạng Gmail không hợp lệ"
                        } else if (passStr != confirmStr) {
                            message.value = "Mật khẩu xác nhận không khớp"
                        } else {
                            val success = dbHelper.registerUserWithEmail(userStr, emailStr, passStr)
                            if (!success) {
                                message.value = "Tên tài khoản hoặc Gmail đã được đăng ký"
                            } else {
                                Toast.makeText(context, "Đăng ký thành công!", Toast.LENGTH_SHORT).show()
                                onRegisterSuccess()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                SecondaryButton(
                    text = "Quay lại",
                    onClick = onBackClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
