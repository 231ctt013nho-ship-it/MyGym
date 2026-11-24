package com.example.mygym.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mygym.ui.theme.GymAccent
import com.example.mygym.ui.theme.GymBackground

/**
 * Phân loại nhóm cơ và màu sắc hiển thị:
 * • Ngực/Vai/Tay: 🔵 Xanh dương (Color(0xFF2196F3))
 * • Lưng: 🔴 Đỏ (Color(0xFFF44336))
 * • Chân/Bụng: 🟢 Xanh lá (Color(0xFF4CAF50))
 * • Nghỉ/Cardio: ⚪ Xám (Color(0xFFB0BEC5))
 */
data class DaySchedulePlan(
    val dayKey: String,
    val dayTitle: String,
    val targetMuscle: String,
    val colorDot: Color,
    val isRest: Boolean = false,
    val statusText: String = "Chưa tập" // "Chưa tập", "Đang tập", "Đã xong"
)

@Composable
fun ScheduleScreen(
    context: Context,
    onBackClick: () -> Unit,
    onDayClick: (String) -> Unit = {},
    onProgressClick: () -> Unit = {}
) {
    // 1. Phân chia lịch tập chuẩn theo yêu cầu người dùng
    val scheduleList = listOf(
        DaySchedulePlan("THỨ 2", "Thứ 2", "Ngực + Vai + Tay sau", Color(0xFF2196F3), statusText = "Đang tập"),
        DaySchedulePlan("THỨ 3", "Thứ 3", "Lưng + Tay trước", Color(0xFFF44336)),
        DaySchedulePlan("THỨ 4", "Thứ 4", "Chân + Bụng", Color(0xFF4CAF50)),
        DaySchedulePlan("THỨ 5", "Thứ 5", "Vai + Ngực nhẹ + Tay sau", Color(0xFF2196F3)),
        DaySchedulePlan("THỨ 6", "Thứ 6", "Lưng + Tay trước", Color(0xFFF44336)),
        DaySchedulePlan("THỨ 7", "Thứ 7", "Nghỉ chủ động / Cardio nhẹ", Color(0xFFB0BEC5), isRest = true),
        DaySchedulePlan("CHỦ NHẬT", "Chủ Nhật", "Nghỉ hoàn toàn", Color(0xFF757575), isRest = true)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GymBackground)
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        // HEADER
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
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
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "LỊCH TẬP TUẦN NÀY",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "Lịch phân chia từ nặng → nhẹ",
                    color = Color.Gray,
                    fontSize = 11.sp
                )
            }
        }

        // CHÚ THÍCH MÀU SẮC NHÓM CƠ
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF242730))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LegendItem(color = Color(0xFF2196F3), text = "Ngực/Vai/Tay 🔵")
                LegendItem(color = Color(0xFFF44336), text = "Lưng 🔴")
                LegendItem(color = Color(0xFF4CAF50), text = "Chân/Bụng 🟢")
                LegendItem(color = Color(0xFFB0BEC5), text = "Nghỉ ⚪")
            }
        }

        // NÚT SAO CHÉP LỊCH & NÚT XEM LẠI ĐÁNH GIÁ TUẦN
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    Toast.makeText(context, "Đã sao chép lịch tập tuần trước!", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E2230)),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GymAccent.copy(alpha = 0.6f))
            ) {
                Text(text = "📋 Sao chép tuần", color = GymAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = onProgressClick,
                modifier = Modifier.weight(1.2f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = "🔍 Xem lại & Đánh giá", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        // DANH SÁCH 7 NGÀY TRONG TUẦN
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(scheduleList) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF242730))
                        .clickable {
                            onDayClick(item.dayKey)
                        }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        // Chấm tròn màu phân biệt nhóm cơ
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(item.colorDot)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = item.dayTitle,
                                    color = GymAccent,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                // Badge trạng thái
                                Text(
                                    text = item.statusText,
                                    color = if (item.statusText == "Đang tập") Color(0xFF2196F3) else Color.Gray,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(0xFF181A20))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = item.targetMuscle,
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Text(
                        text = "→",
                        color = GymAccent,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun LegendItem(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}
