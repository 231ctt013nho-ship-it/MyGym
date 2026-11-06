package com.example.mygym.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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

data class DaySchedulePlan(
    val dayName: String,
    val targetMuscle: String,
    val isRest: Boolean = false
)

@Composable
fun ScheduleScreen(
    context: Context,
    onBackClick: () -> Unit,
    onDayClick: (String) -> Unit = {},
    onProgressClick: () -> Unit = {}
) {
    val scheduleList = listOf(
        DaySchedulePlan("Thứ 2", "Ngực + Tay sau"),
        DaySchedulePlan("Thứ 3", "Lưng + Tay trước"),
        DaySchedulePlan("Thứ 4", "Nghỉ", isRest = true),
        DaySchedulePlan("Thứ 5", "Chân + Bụng"),
        DaySchedulePlan("Thứ 6", "Vai + Tay sau"),
        DaySchedulePlan("Thứ 7", "Ngực + Lưng"),
        DaySchedulePlan("CHỦ NHẬT", "Nghỉ", isRest = true)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GymBackground)
            .statusBarsPadding()
            .padding(16.dp)
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
                text = "LỊCH TẬP",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Danh sách các ngày
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(scheduleList) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF242730))
                        .clickable {
                            if (!item.isRest) {
                                onDayClick(item.dayName.uppercase())
                            }
                        }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Chấm tròn trạng thái (Xanh lá: tập / Đỏ cam: nghỉ)
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(if (item.isRest) Color(0xFFFF5252) else Color(0xFF4CAF50))
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = item.dayName,
                                color = Color(0xFFFF9800),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = item.targetMuscle,
                                color = Color.White,
                                fontSize = 13.sp
                            )
                        }
                    }

                    if (!item.isRest) {
                        Text(
                            text = "→",
                            color = Color(0xFFFF9800),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}