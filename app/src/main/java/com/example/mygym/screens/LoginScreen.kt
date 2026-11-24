package com.example.mygym.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mygym.components.DarkCard
import com.example.mygym.components.ModernTextField
import com.example.mygym.components.PrimaryButton
import com.example.mygym.components.SecondaryButton
import com.example.mygym.data.AccountStorage
import com.example.mygym.data.GymDatabaseHelper
import com.example.mygym.ui.theme.GymAccent
import com.example.mygym.ui.theme.GymGray
import com.example.mygym.ui.theme.GymRed
import com.example.mygym.ui.theme.GymWhite

@Composable
fun LoginScreen(
    context: Context,
    onLoginSuccess: (String, String) -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit = {}
) {
    val dbHelper = remember { GymDatabaseHelper(context) }

    val usernameOrEmail = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val message = remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(GymAccent.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text("MG", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = GymAccent)
            }

            Spacer(modifier = Modifier.height(14.dp))
            Text("MY GYM", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 2.sp, color = GymWhite)
            Spacer(modifier = Modifier.height(2.dp))
            Text("THỂ HÌNH CÁ NHÂN", fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp, color = GymGray)
            Spacer(modifier = Modifier.height(24.dp))

            DarkCard {
                Text("CHÀO MỪNG TRỞ LẠI", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = GymWhite)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Đăng nhập bằng Tên hoặc Gmail", fontSize = 12.sp, color = GymGray)
                Spacer(modifier = Modifier.height(14.dp))

                // DEMO QUICK ACCESS BUTTONS
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            usernameOrEmail.value = "admin"
                            password.value = "123456"
                            val account = dbHelper.getUser("admin")
                            if (account != null) {
                                AccountStorage.saveLogin(context, account.username)
                                onLoginSuccess(account.username, account.role)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E2230)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GymAccent.copy(alpha = 0.5f))
                    ) {
                        Text("🔑 Admin", color = GymAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            usernameOrEmail.value = "user"
                            password.value = "123456"
                            val account = dbHelper.getUser("user")
                            if (account != null) {
                                AccountStorage.saveLogin(context, account.username)
                                onLoginSuccess(account.username, account.role)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E2230)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF4CAF50).copy(alpha = 0.5f))
                    ) {
                        Text("👤 User", color = Color(0xFF4CAF50), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                ModernTextField(
                    value = usernameOrEmail.value,
                    onValueChange = { usernameOrEmail.value = it; message.value = "" },
                    label = "Tên tài khoản hoặc Gmail"
                )
                Spacer(modifier = Modifier.height(12.dp))

                ModernTextField(
                    value = password.value,
                    onValueChange = { password.value = it; message.value = "" },
                    label = "Mật khẩu",
                    password = true
                )

                Box(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = "Quên mật khẩu?",
                        fontSize = 12.sp,
                        color = GymAccent,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable { onForgotPasswordClick() }
                    )
                }

                if (message.value.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(message.value, fontSize = 12.sp, color = GymRed, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                PrimaryButton(
                    text = "ĐĂNG NHẬP",
                    onClick = {
                        val inputStr = usernameOrEmail.value.trim()
                        val inputPass = password.value

                        if (inputStr.isBlank() || inputPass.isBlank()) {
                            message.value = "Vui lòng nhập Tên/Gmail và Mật khẩu"
                        } else {
                            val account = dbHelper.getUser(inputStr) ?: dbHelper.getUserByEmail(inputStr)
                            if (account == null) {
                                message.value = "Tài khoản / Gmail chưa được đăng ký"
                            } else if (account.password != inputPass) {
                                message.value = "Mật khẩu không đúng"
                            } else {
                                AccountStorage.saveLogin(context, account.username)
                                onLoginSuccess(account.username, account.role)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "Hoặc đăng nhập với",
                    fontSize = 11.sp,
                    color = GymGray,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(10.dp))

                // CÁC ICON MẠNG XÃ HỘI (GOOGLE G & FACEBOOK F TRỰC TIẾP TRONG APP)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Đăng nhập Google
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color(0xFFEA4335), CircleShape)
                            .clickable {
                                val googleUser = "google_user"
                                dbHelper.registerUserWithEmail(googleUser, "google_user@gmail.com", "123456", "USER")
                                AccountStorage.saveLogin(context, googleUser)
                                Toast.makeText(context, "Đã đăng nhập bằng Google Gmail!", Toast.LENGTH_SHORT).show()
                                onLoginSuccess(googleUser, "USER")
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("G", color = Color(0xFFEA4335), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    // Đăng nhập Facebook
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color(0xFF1877F2), CircleShape)
                            .clickable {
                                val fbUser = "facebook_user"
                                dbHelper.registerUserWithEmail(fbUser, "facebook_user@gmail.com", "123456", "USER")
                                AccountStorage.saveLogin(context, fbUser)
                                Toast.makeText(context, "Đã đăng nhập bằng Facebook!", Toast.LENGTH_SHORT).show()
                                onLoginSuccess(fbUser, "USER")
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("f", color = Color(0xFF1877F2), fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                SecondaryButton(
                    text = "TẠO TÀI KHOẢN MỚI",
                    onClick = onRegisterClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
