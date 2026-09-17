package com.example.mygym.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mygym.components.DarkCard
import com.example.mygym.components.PrimaryButton
import com.example.mygym.components.SecondaryButton
import com.example.mygym.data.AccountStorage
import com.example.mygym.ui.theme.GymAccent
import com.example.mygym.ui.theme.GymBackground
import com.example.mygym.ui.theme.GymCard
import com.example.mygym.ui.theme.GymDivider
import com.example.mygym.ui.theme.GymGray
import com.example.mygym.ui.theme.GymRed
import com.example.mygym.ui.theme.GymWhite

@Composable
fun ProfileScreen(
    context: Context,
    onBackClick: () -> Unit,
    onLogout: () -> Unit
) {
    val username = AccountStorage.getUsername(context)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GymBackground)
            .padding(horizontal = 20.dp)
    ) {

        // =========================================================
        // HEADER
        // =========================================================

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            SecondaryButton(
                text = "←",
                onClick = onBackClick,
                modifier = Modifier.size(48.dp)
            )

            Text(
                text = "Hồ sơ",
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                color = GymWhite,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // =========================================================
        // AVATAR + USERNAME
        // =========================================================

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(GymAccent),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (username.isNotEmpty()) {
                        username.first().uppercase()
                    } else {
                        "U"
                    },
                    color = Color(0xFF0A0D15),
                    fontSize = 38.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = if (username.isNotEmpty()) username else "Người dùng",
                color = GymWhite,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Thành viên MyGym",
                color = GymGray,
                fontSize = 13.sp
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // =========================================================
        // THÔNG TIN TÀI KHOẢN
        // =========================================================

        Text(
            text = "THÔNG TIN TÀI KHOẢN",
            color = GymGray,
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        DarkCard {

            ProfileInfoRow(
                label = "Tên tài khoản",
                value = if (username.isNotEmpty()) username else "Chưa cập nhật"
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 15.dp),
                color = GymDivider
            )

            ProfileInfoRow(
                label = "Trạng thái",
                value = "Đang hoạt động",
                valueColor = GymAccent
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 15.dp),
                color = GymDivider
            )

            ProfileInfoRow(
                label = "Ứng dụng",
                value = "MyGym"
            )
        }

        Spacer(modifier = Modifier.height(22.dp))

        // =========================================================
        // MỤC TIÊU TẬP LUYỆN
        // =========================================================

        Text(
            text = "MỤC TIÊU TẬP LUYỆN",
            color = GymGray,
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = GymCard
            )
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = "🏋️  Xây dựng cơ bắp",
                    color = GymWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(7.dp))

                Text(
                    text = "Duy trì lịch tập đều đặn và cải thiện sức mạnh.",
                    color = GymGray,
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // =========================================================
        // ĐĂNG XUẤT
        // =========================================================

        PrimaryButton(
            text = "Đăng xuất",
            onClick = {
                AccountStorage.logout(context)
                onLogout()
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "MyGym • Dark Premium",
            modifier = Modifier.fillMaxWidth(),
            color = GymGray,
            fontSize = 11.sp,
            textAlign = TextAlign.Center
        )
    }
}


// =============================================================
// PROFILE INFO ROW
// =============================================================

@Composable
private fun ProfileInfoRow(
    label: String,
    value: String,
    valueColor: Color = GymWhite
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = label,
            color = GymGray,
            fontSize = 13.sp
        )

        Text(
            text = value,
            color = valueColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}