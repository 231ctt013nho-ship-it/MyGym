package com.example.mygym.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.mygym.ui.theme.GymAccent
import com.example.mygym.ui.theme.GymGray
import com.example.mygym.ui.theme.GymRed
import com.example.mygym.ui.theme.GymWhite

@Composable
fun LoginScreen(
    context: Context,
    onLoginSuccess: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit = {}
) {
    // DỮ LIỆU NHẬP
    val username = remember {
        mutableStateOf(AccountStorage.getUsername(context))
    }
    val password = remember {
        mutableStateOf("")
    }
    val message = remember {
        mutableStateOf("")
    }

    // GIAO DIỆN
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
            // LOGO "MG"
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(GymAccent.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "MG",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = GymAccent
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // TÊN APP
            Text(
                text = "MY GYM",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp,
                color = GymWhite
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "THỂ HÌNH CÁ NHÂN",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                color = GymGray
            )

            Spacer(modifier = Modifier.height(24.dp))

            // CARD ĐĂNG NHẬP
            DarkCard {
                Text(
                    text = "CHÀO MỪNG TRỞ LẠI",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = GymWhite
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Đăng nhập để tiếp tục",
                    fontSize = 12.sp,
                    color = GymGray
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Ô NHẬP TÀI KHOẢN
                ModernTextField(
                    value = username.value,
                    onValueChange = {
                        username.value = it
                        message.value = ""
                    },
                    label = "Tài khoản"
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Ô NHẬP MẬT KHẨU
                ModernTextField(
                    value = password.value,
                    onValueChange = {
                        password.value = it
                        message.value = ""
                    },
                    label = "Mật khẩu",
                    password = true
                )

                // NÚT "QUÊN MẬT KHẨU?"
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
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

                // HIỂN THỊ THÔNG BÁO LỖI NẾU CÓ
                if (message.value.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = message.value,
                        fontSize = 12.sp,
                        color = GymRed,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // NÚT ĐĂNG NHẬP
                PrimaryButton(
                    text = "ĐĂNG NHẬP",
                    onClick = {
                        if (username.value.isBlank() || password.value.isBlank()) {
                            message.value = "Vui lòng nhập đầy đủ thông tin"
                        } else {
                            val savedPassword = AccountStorage.getPassword(
                                context,
                                username.value
                            )
                            if (savedPassword == null) {
                                message.value = "Tài khoản chưa đăng ký"
                            } else if (savedPassword != password.value) {
                                message.value = "Mật khẩu không đúng"
                            } else {
                                AccountStorage.saveLogin(
                                    context,
                                    username.value
                                )
                                onLoginSuccess(username.value)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // PHẦN "HOẶC ĐĂNG NHẬP VỚI" THEO BẢN VẼ FIGMA
                Text(
                    text = "Hoặc đăng nhập với",
                    fontSize = 11.sp,
                    color = GymGray,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // CÁC ICON MẠNG XÃ HỘI (GOOGLE / FACEBOOK)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Google icon box
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color(0xFF3A3F4D), CircleShape)
                            .clickable { /* Xử lý đăng nhập Google nếu cần */ },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "G",
                            color = Color(0xFFEA4335),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(18.dp))

                    // Facebook icon box
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color(0xFF3A3F4D), CircleShape)
                            .clickable { /* Xử lý đăng nhập Facebook nếu cần */ },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "f",
                            color = Color(0xFF1877F2),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // NÚT TẠO TÀI KHOẢN
                SecondaryButton(
                    text = "TẠO TÀI KHOẢN",
                    onClick = onRegisterClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}