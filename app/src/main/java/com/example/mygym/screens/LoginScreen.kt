package com.example.mygym.screens

import android.content.Context

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

import com.example.mygym.data.AccountStorage

import com.example.mygym.ui.theme.GymAccent
import com.example.mygym.ui.theme.GymGray
import com.example.mygym.ui.theme.GymRed
import com.example.mygym.ui.theme.GymWhite


@Composable
fun LoginScreen(
    context: Context,
    onLoginSuccess: (String) -> Unit,
    onRegisterClick: () -> Unit
) {

    // ========================================================
    // DỮ LIỆU NHẬP
    // ========================================================

    val username = remember {
        mutableStateOf(
            AccountStorage.getUsername(context)
        )
    }

    val password = remember {
        mutableStateOf("")
    }

    val message = remember {
        mutableStateOf("")
    }


    // ========================================================
    // GIAO DIỆN
    // ========================================================

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ==================================================
            // LOGO
            // ==================================================

            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(
                        RoundedCornerShape(20.dp)
                    )
                    .background(
                        GymAccent.copy(alpha = 0.15f)
                    ),

                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "MG",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = GymAccent
                )
            }


            Spacer(
                modifier = Modifier.height(18.dp)
            )


            // ==================================================
            // TÊN APP
            // ==================================================

            Text(
                text = "MYGYM",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp,
                color = GymWhite
            )


            Spacer(
                modifier = Modifier.height(4.dp)
            )


            Text(
                text = "THỂ HÌNH CÁ NHÂN",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                color = GymGray
            )


            Spacer(
                modifier = Modifier.height(30.dp)
            )


            // ==================================================
            // CARD ĐĂNG NHẬP
            // ==================================================

            DarkCard {

                Text(
                    text = "CHÀO MỪNG TRỞ LẠI",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = GymWhite
                )


                Spacer(
                    modifier = Modifier.height(6.dp)
                )


                Text(
                    text = "Đăng nhập để tiếp tục",
                    fontSize = 13.sp,
                    color = GymGray
                )


                Spacer(
                    modifier = Modifier.height(22.dp)
                )


                // ==================================================
                // TÀI KHOẢN
                // ==================================================

                ModernTextField(
                    value = username.value,

                    onValueChange = {
                        username.value = it
                        message.value = ""
                    },

                    label = "Tài khoản"
                )


                Spacer(
                    modifier = Modifier.height(13.dp)
                )


                // ==================================================
                // MẬT KHẨU
                // ==================================================

                ModernTextField(
                    value = password.value,

                    onValueChange = {
                        password.value = it
                        message.value = ""
                    },

                    label = "Mật khẩu",

                    password = true
                )


                // ==================================================
                // THÔNG BÁO LỖI
                // ==================================================

                if (message.value.isNotEmpty()) {

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        text = message.value,
                        fontSize = 12.sp,
                        color = GymRed,
                        fontWeight = FontWeight.Bold
                    )
                }


                Spacer(
                    modifier = Modifier.height(18.dp)
                )


                // ==================================================
                // NÚT ĐĂNG NHẬP
                // ==================================================

                PrimaryButton(
                    text = "ĐĂNG NHẬP",

                    onClick = {

                        // Kiểm tra bỏ trống
                        if (
                            username.value.isBlank() ||
                            password.value.isBlank()
                        ) {

                            message.value =
                                "Vui lòng nhập đầy đủ thông tin"

                        } else {

                            // Lấy mật khẩu đã lưu
                            val savedPassword =
                                AccountStorage.getPassword(
                                    context,
                                    username.value
                                )


                            // Không tìm thấy tài khoản
                            if (savedPassword == null) {

                                message.value =
                                    "Tài khoản chưa đăng ký"

                            }

                            // Sai mật khẩu
                            else if (
                                savedPassword != password.value
                            ) {

                                message.value =
                                    "Mật khẩu không đúng"

                            }

                            // Đăng nhập thành công
                            else {

                                AccountStorage.saveLogin(
                                    context,
                                    username.value
                                )

                                onLoginSuccess(
                                    username.value
                                )
                            }
                        }
                    },

                    modifier = Modifier.fillMaxWidth()
                )


                Spacer(
                    modifier = Modifier.height(12.dp)
                )


                // ==================================================
                // NÚT TẠO TÀI KHOẢN
                // ==================================================

                SecondaryButton(
                    text = "TẠO TÀI KHOẢN",

                    onClick = onRegisterClick,

                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}