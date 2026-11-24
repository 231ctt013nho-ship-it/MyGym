package com.example.mygym.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mygym.data.GymDatabaseHelper
import com.example.mygym.ui.theme.GymAccent
import com.example.mygym.ui.theme.GymBackground

@Composable
fun ForgotPasswordScreen(
    context: Context,
    onBackClick: () -> Unit
) {
    val dbHelper = remember { GymDatabaseHelper(context) }

    var step by remember { mutableStateOf(1) } // 1: Nhập Gmail -> 2: Nhập mã OTP -> 3: Đổi Mật Khẩu
    var emailInput by remember { mutableStateOf("") }
    var generatedOtp by remember { mutableStateOf("") }
    var otpInput by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GymBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("QUÊN MẬT KHẨU", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
        Text(
            text = when (step) {
                1 -> "Bước 1: Nhập địa chỉ Gmail để nhận mã xác thực OTP"
                2 -> "Bước 2: Nhập mã OTP 6 chữ số vừa gửi đến Gmail $emailInput"
                else -> "Bước 3: Nhập mật khẩu mới cho tài khoản"
            },
            color = Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
        )

        when (step) {
            1 -> {
                OutlinedTextField(
                    value = emailInput,
                    onValueChange = { emailInput = it },
                    label = { Text("Địa chỉ Gmail đăng ký (VD: user@gmail.com)", color = Color.Gray) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GymAccent,
                        unfocusedBorderColor = Color(0xFF333333)
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        val trimmedEmail = emailInput.trim()
                        if (trimmedEmail.isBlank()) {
                            Toast.makeText(context, "Vui lòng nhập địa chỉ Gmail", Toast.LENGTH_SHORT).show()
                        } else {
                            val account = dbHelper.getUserByEmail(trimmedEmail) ?: dbHelper.getUser(trimmedEmail)
                            if (account == null) {
                                Toast.makeText(context, "Gmail/Tài khoản này chưa đăng ký hệ thống", Toast.LENGTH_SHORT).show()
                            } else {
                                val code = (100000..999999).random().toString()
                                generatedOtp = code
                                dbHelper.logPasswordReset(account.username, trimmedEmail, code, "ĐÃ GỬI MÃ OTP")
                                step = 2
                                Toast.makeText(context, "MÃ OTP XÁC THỰC LÀ: $code", Toast.LENGTH_LONG).show()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GymAccent),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("GỬI MÃ XÁC THỰC GMAIL 📧", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }

            2 -> {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF242730))
                ) {
                    Text(
                        text = "📩 Mã OTP xác thực của bạn là: $generatedOtp\n(Đã gửi tới Gmail $emailInput)",
                        color = GymAccent,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(14.dp)
                    )
                }

                OutlinedTextField(
                    value = otpInput,
                    onValueChange = { otpInput = it },
                    label = { Text("Nhập mã OTP 6 chữ số", color = Color.Gray) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GymAccent,
                        unfocusedBorderColor = Color(0xFF333333)
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (otpInput.trim() == generatedOtp) {
                            Toast.makeText(context, "Mã OTP hợp lệ! Hãy nhập mật khẩu mới.", Toast.LENGTH_SHORT).show()
                            step = 3
                        } else {
                            Toast.makeText(context, "Mã OTP không đúng, vui lòng kiểm tra lại!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GymAccent),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("XÁC NHẬN MÃ OTP", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }

            3 -> {
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("Mật khẩu mới", color = Color.Gray) },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GymAccent
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Nhập lại mật khẩu mới", color = Color.Gray) },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GymAccent
                    )
                )
                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (newPassword.isBlank() || confirmPassword.isBlank()) {
                            Toast.makeText(context, "Vui lòng nhập đầy đủ mật khẩu mới", Toast.LENGTH_SHORT).show()
                        } else if (newPassword != confirmPassword) {
                            Toast.makeText(context, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show()
                        } else {
                            val updated = dbHelper.updatePasswordByEmail(emailInput.trim(), newPassword)
                            if (updated) {
                                Toast.makeText(context, "Đã đổi mật khẩu thành công! Mời bạn đăng nhập lại.", Toast.LENGTH_LONG).show()
                                onBackClick()
                            } else {
                                Toast.makeText(context, "Cập nhật mật khẩu thất bại", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GymAccent),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("ĐỔI MẬT KHẨU MỚI", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
        ) {
            Text("Quay lại")
        }
    }
}
