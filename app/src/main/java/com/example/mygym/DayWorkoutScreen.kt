package com.example.mygym.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mygym.ui.theme.GymBackground

data class DayExerciseItem(
    val title: String,
    val subtitle: String,
    val isDone: Boolean = false
)

@Composable
fun DayWorkoutScreen(
    dayTitle: String,
    onBackClick: () -> Unit
) {
    val initialItems = remember(dayTitle) {
        when (dayTitle) {
            "THỨ 2" -> listOf(
                DayExerciseItem("Đẩy ngực tạ đơn", "20KG X3", true),
                DayExerciseItem("Ép ngực tạ đơn", "10KG X3", false),
                DayExerciseItem("Đẩy tạ sau", "7.5KG X3", false),
                DayExerciseItem("Dưới tay sau", "6.5KG X3", false)
            )
            "THỨ 3" -> listOf(
                DayExerciseItem("Kéo xô với máy cable", "Số hiệp: 4 hiệp x 10 lần", true),
                DayExerciseItem("Ép ngực tạ đơn", "Số hiệp: 4 hiệp x 8 lần", true),
                DayExerciseItem("Cuốn tay trước với thanh đòn", "Số hiệp: 4 hiệp x 10-12 lần", false),
                DayExerciseItem("Cuốn tạ đơn kiểu búa", "Số hiệp: 3 hiệp x 12 lần", false)
            )
            else -> listOf(
                DayExerciseItem("Gánh đòn gánh tập đùi", "Số hiệp: 4 hiệp x 8-10 lần", true),
                DayExerciseItem("Đạp đùi với máy", "Số hiệp: 4 hiệp x 10-12 lần", false),
                DayExerciseItem("Đu xà nhấc chân / Co gối", "Số hiệp: 3 hiệp x 12-15 lần", false),
                DayExerciseItem("Gập bụng với dây cáp", "Số hiệp: 3 hiệp x 15 lần", false)
            )
        }
    }

    var items by remember { mutableStateOf(initialItems) }
    val completedCount = items.count { it.isDone }
    val progress = if (items.isNotEmpty()) completedCount.toFloat() / items.size else 0f

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
            Text(
                text = dayTitle,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Danh sách bài tập
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(items) { index, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF242730))
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.title,
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item.subtitle,
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                    }

                    Checkbox(
                        checked = item.isDone,
                        onCheckedChange = { checked ->
                            items = items.toMutableList().also { list ->
                                list[index] = list[index].copy(isDone = checked)
                            }
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFF26A69A),
                            uncheckedColor = Color.Gray,
                            checkmarkColor = Color.White
                        )
                    )
                }
            }
        }

        // Tiến độ
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("TIẾN ĐỘ", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text("${(progress * 100).toInt()}%", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = Color(0xFF26A69A),
                trackColor = Color(0xFF333842)
            )
        }
    }
}