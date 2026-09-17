package com.example.mygym.screens

import android.content.Context

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.mygym.components.DarkCard
import com.example.mygym.components.ModernTextField
import com.example.mygym.components.PrimaryButton
import com.example.mygym.components.SecondaryButton

import com.example.mygym.data.AccountStorage

import com.example.mygym.ui.theme.GymAccent
import com.example.mygym.ui.theme.GymRed
import com.example.mygym.ui.theme.GymWhite


@Composable
fun RegisterScreen(
    context: Context,
    onRegisterSuccess: () -> Unit,
    onBackClick: () -> Unit
) {

    // ========================================================
    // DỮ LIỆU NHẬP
    // ========================================================

    val username = remember {
        mutableStateOf("")
    }

    val password = remember {
        mutableStateOf("")
    }

    val confirmPassword = remember {
        mutableStateOf("")
    }

    val message = remember {
        mutableStateOf("")
    }


    // ========================================================
    // GIAO DIỆN
    // ========================================================

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        horizontalAlignment = Alignment.CenterHorizontally,

        verticalArrangement = Arrangement.Center
    ) {

        item {

            // ==================================================
            // LOGO
            // ==================================================

            Box(
                modifier = Modifier
                    .size(65.dp)
                    .clip(
                        RoundedCornerShape(18.dp)
                    )
                    .background(
                        GymAccent.copy(alpha = 0.15f)
                    ),

                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "MG",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = GymAccent
                )
            }


            Spacer(
                modifier = Modifier.height(15.dp)
            )


            // ==================================================
            // TIÊU ĐỀ
            // ==================================================

            Text(
                text = "TẠO TÀI KHOẢN",
                fontSize = 27.sp,
                fontWeight = FontWeight.ExtraBold,
                color = GymWhite,
                textAlign = TextAlign.Center
            )


            Spacer(
                modifier = Modifier.height(25.dp)
            )


            // ==================================================
            // CARD ĐĂNG KÝ
            // ==================================================

            DarkCard {

                // ----------------------------------------------
                // TÊN TÀI KHOẢN
                // ----------------------------------------------

                ModernTextField(
                    value = username.value,

                    onValueChange = {
                        username.value = it
                        message.value = ""
                    },

                    label = "Tên tài khoản"
                )


                Spacer(
                    modifier = Modifier.height(13.dp)
                )


                // ----------------------------------------------
                // MẬT KHẨU
                // ----------------------------------------------

                ModernTextField(
                    value = password.value,

                    onValueChange = {
                        password.value = it
                        message.value = ""
                    },

                    label = "Mật khẩu",

                    password = true
                )


                Spacer(
                    modifier = Modifier.height(13.dp)
                )


                // ----------------------------------------------
                // NHẬP LẠI MẬT KHẨU
                // ----------------------------------------------

                ModernTextField(
                    value = confirmPassword.value,

                    onValueChange = {
                        confirmPassword.value = it
                        message.value = ""
                    },

                    label = "Nhập lại mật khẩu",

                    password = true
                )


                // =================================================
                // THÔNG BÁO
                // =================================================

                if (message.value.isNotEmpty()) {

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        text = message.value,
                        color = GymRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }


                Spacer(
                    modifier = Modifier.height(18.dp)
                )


                // =================================================
                // NÚT TẠO TÀI KHOẢN
                // =================================================

                PrimaryButton(
                    text = "TẠO TÀI KHOẢN",

                    onClick = {

                        // -----------------------------------------
                        // KIỂM TRA BỎ TRỐNG
                        // -----------------------------------------

                        if (
                            username.value.isBlank() ||
                            password.value.isBlank() ||
                            confirmPassword.value.isBlank()
                        ) {

                            message.value =
                                "Vui lòng nhập đầy đủ thông tin"
                        }

                        // -----------------------------------------
                        // KIỂM TRA MẬT KHẨU
                        // -----------------------------------------

                        else if (
                            password.value != confirmPassword.value
                        ) {

                            message.value =
                                "Mật khẩu không khớp"
                        }

                        // -----------------------------------------
                        // KIỂM TRA TÀI KHOẢN ĐÃ TỒN TẠI
                        // -----------------------------------------

                        else if (
                            AccountStorage.accountExists(
                                context,
                                username.value
                            )
                        ) {

                            message.value =
                                "Tài khoản đã tồn tại"
                        }

                        // -----------------------------------------
                        // ĐĂNG KÝ THÀNH CÔNG
                        // -----------------------------------------

                        else {

                            AccountStorage.register(
                                context,
                                username.value,
                                password.value
                            )

                            onRegisterSuccess()
                        }
                    },

                    modifier = Modifier.fillMaxWidth()
                )


                Spacer(
                    modifier = Modifier.height(10.dp)
                )


                // =================================================
                // NÚT QUAY LẠI
                // =================================================

                SecondaryButton(
                    text = "QUAY LẠI",

                    onClick = onBackClick,

                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}