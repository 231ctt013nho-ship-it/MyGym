package com.example.mygym.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mygym.ui.theme.GymBackground

@Composable
fun ProgressScreen(
    context: Context,
    onBackClick: () -> Unit
) {
    // Dữ liệu mẫu tiến độ tập luyện trong tuần
    val totalWorkoutDays = 5 // Số ngày tập dự kiến (T2, T3, T5, T6, T7)
    val completedWorkoutDays = 2 // Số ngày đã hoàn thành
    val overallProgress = completedWorkoutDays.toFloat() / totalWorkoutDays.toFloat()

    val daysStatus = listOf(
        Pair("Thứ 2", true),
        Pair("Thứ 3", true),
        Pair("Thứ 4", false), // Nghỉ
        Pair("Thứ 5", false),
        Pair("Thứ 6", false),
        Pair("Thứ 7", false),
        Pair("Chủ Nhật", false) // Nghỉ
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GymBackground)
            .statusBarsPadding()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Nút quay lại và tiêu đề
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clickable { onBackClick() },
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "←",
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "TIẾN ĐỘ TẬP LUYỆN",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Card tổng quan tiến độ tuần
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF242730))
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = "Mục tiêu tuần này",
                    color = Color.Gray,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "$completedWorkoutDays / $totalWorkoutDays ngày",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "${(overallProgress * 100).toInt()}%",
                        color = Color(0xFFFF9800),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                LinearProgressIndicator(
                    progress = { overallProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp)),
                    color = Color(0xFFFF9800),
                    trackColor = Color(0xFF333842)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Trạng thái các ngày trong tuần",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Danh sách trạng thái từng ngày
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            daysStatus.forEach { (day, isDone) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF1E2129))
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = day,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (isDone) Color(0xFF4CAF50) else Color.Gray)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isDone) "Đã hoàn thành" else "Chưa hoàn thành",
                            color = if (isDone) Color(0xFF4CAF50) else Color.Gray,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}