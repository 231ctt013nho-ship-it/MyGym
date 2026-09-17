package com.example.mygym.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.mygym.components.DashboardHeroCard
import com.example.mygym.components.DashboardSmallCard
import com.example.mygym.components.ExercisePreviewCard
import com.example.mygym.components.SectionTitle
import com.example.mygym.components.SecondaryButton

import com.example.mygym.data.exercises

import com.example.mygym.ui.theme.GymAccent
import com.example.mygym.ui.theme.GymCard
import com.example.mygym.ui.theme.GymGray
import com.example.mygym.ui.theme.GymWhite


@Composable
fun HomeScreen(
    username: String,
    onExercisesClick: () -> Unit,
    onMuscleClick: () -> Unit,
    onScheduleClick: () -> Unit,
    onProgressClick: () -> Unit,
    onLogout: () -> Unit
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 20.dp,
                vertical = 22.dp
            ),

        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // ====================================================
        // HEADER
        // ====================================================

        item {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "MYGYM",
                        fontSize = 23.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 2.sp,
                        color = GymWhite
                    )

                    Text(
                        text = "BẢNG ĐIỀU KHIỂN TẬP LUYỆN",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        color = GymGray
                    )
                }


                // Avatar
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(
                            RoundedCornerShape(14.dp)
                        )
                        .background(GymCard),

                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = username
                            .take(1)
                            .uppercase(),

                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = GymAccent
                    )
                }
            }
        }


        // ====================================================
        // LỜI CHÀO
        // ====================================================

        item {

            Column {

                Text(
                    text = "Xin chào,",
                    fontSize = 14.sp,
                    color = GymGray
                )

                Text(
                    text = username,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = GymWhite
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = "Sẵn sàng cho buổi tập hôm nay?",
                    fontSize = 13.sp,
                    color = GymGray
                )
            }
        }


        // ====================================================
        // CARD BÀI TẬP
        // ====================================================

        item {

            DashboardHeroCard(
                onClick = onExercisesClick
            )
        }


        // ====================================================
        // KHÁM PHÁ
        // ====================================================

        item {

            Text(
                text = "KHÁM PHÁ",
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.5.sp,
                color = GymGray
            )
        }


        // ====================================================
        // BÀI TẬP + NHÓM CƠ
        // ====================================================

        item {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                DashboardSmallCard(
                    title = "BÀI TẬP",
                    subtitle = "Thư viện bài tập",
                    modifier = Modifier.weight(1f),
                    onClick = onExercisesClick
                )


                DashboardSmallCard(
                    title = "NHÓM CƠ",
                    subtitle = "Chọn nhóm cơ",
                    modifier = Modifier.weight(1f),
                    onClick = onMuscleClick
                )
            }
        }


        // ====================================================
        // LỊCH TẬP + TIẾN ĐỘ
        // ====================================================

        item {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                DashboardSmallCard(
                    title = "LỊCH TẬP",
                    subtitle = "Kế hoạch tuần",
                    modifier = Modifier.weight(1f),
                    onClick = onScheduleClick
                )


                DashboardSmallCard(
                    title = "TIẾN ĐỘ",
                    subtitle = "Theo dõi kết quả",
                    modifier = Modifier.weight(1f),
                    onClick = onProgressClick
                )
            }
        }


        // ====================================================
        // THƯ VIỆN BÀI TẬP
        // ====================================================

        item {

            SectionTitle(
                title = "THƯ VIỆN BÀI TẬP",
                subtitle = "Chọn bài tập để bắt đầu"
            )
        }


        // ====================================================
        // BÀI TẬP MẪU
        // ====================================================

        item {

            ExercisePreviewCard(
                exercise = exercises[0],
                onClick = onExercisesClick
            )
        }


        // ====================================================
        // ĐĂNG XUẤT
        // ====================================================

        item {

            SecondaryButton(
                text = "ĐĂNG XUẤT",
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}