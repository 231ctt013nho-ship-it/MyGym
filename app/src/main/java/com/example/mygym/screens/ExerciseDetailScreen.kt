package com.example.mygym.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mygym.R
import com.example.mygym.components.DarkCard
import com.example.mygym.components.DetailStatCard
import com.example.mygym.data.Exercise
import com.example.mygym.ui.theme.GymAccent
import com.example.mygym.ui.theme.GymDarkGray
import com.example.mygym.ui.theme.GymGray
import com.example.mygym.ui.theme.GymImageBackground
import com.example.mygym.ui.theme.GymWhite

@Composable
fun ExerciseDetailScreen(
    exercise: Exercise,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp, vertical = 18.dp)
    ) {

        // ==============================
        // TIÊU ĐỀ
        // ==============================

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "‹",
                fontSize = 38.sp,
                color = GymWhite,
                modifier = Modifier.clickable {
                    onBackClick()
                }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = exercise.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = GymWhite
                )

                Text(
                    text = exercise.muscle.uppercase(),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    color = GymAccent
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // ==============================
        // HÌNH ẢNH BÀI TẬP
        // ==============================

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(340.dp),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = GymImageBackground
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                Image(
                    painter = painterResource(id = exercise.imageRes),
                    contentDescription = exercise.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        // ==============================
        // THÔNG TIN CƠ BẢN
        // ==============================

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            DetailStatCard(
                label = "NHÓM CƠ",
                value = exercise.muscle.uppercase(),
                modifier = Modifier.weight(1f)
            )

            DetailStatCard(
                label = "MỨC ĐỘ",
                value = "CƠ BẢN",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        // ==============================
        // MÔ TẢ
        // ==============================

        DarkCard {

            Text(
                text = "MÔ TẢ",
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.2.sp,
                color = GymGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = exercise.description,
                fontSize = 14.sp,
                lineHeight = 21.sp,
                color = GymWhite
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        // ==============================
        // DỮ LIỆU TẬP LUYỆN
        // ==============================

        DarkCard {

            Text(
                text = "DỮ LIỆU TẬP LUYỆN",
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.2.sp,
                color = GymGray
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Chưa thiết lập số hiệp và số lần",
                fontSize = 13.sp,
                color = GymGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "—",
                fontSize = 24.sp,
                color = GymDarkGray
            )
        }
    }
}