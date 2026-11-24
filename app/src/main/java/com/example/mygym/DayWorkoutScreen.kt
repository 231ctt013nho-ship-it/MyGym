package com.example.mygym.screens

import android.content.Context
import android.widget.Toast
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
import com.example.mygym.data.AccountStorage
import com.example.mygym.data.GymDatabaseHelper
import com.example.mygym.ui.theme.GymAccent
import com.example.mygym.ui.theme.GymBackground
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class DetailWorkoutItem(
    val title: String,
    val muscleGroup: String,
    val colorBadge: Color,
    val weightKg: Float,
    val sets: Int,
    val reps: Int,
    val restSeconds: Int,
    val heavyOrder: Int,
    val isDone: Boolean = false,
    val note: String = ""
) {
    val formattedInfo: String
        get() = "$title | ${weightKg.toInt()}kg × $sets × $reps | Nghỉ ${restSeconds}s"
}

@Composable
fun DayWorkoutScreen(
    context: Context,
    dayTitle: String,
    onBackClick: () -> Unit
) {
    val dbHelper = remember { GymDatabaseHelper(context) }
    val currentUsername = AccountStorage.getUsername(context)

    // Khởi tạo các bài tập chưa được tích (Mặc định isDone = false để người dùng tự tích khi tập)
    val initialExercises = remember(dayTitle) {
        when (dayTitle) {
            "THỨ 2" -> listOf(
                DetailWorkoutItem("Đẩy ngực tạ đơn", "NGỰC", Color(0xFF2196F3), 20f, 3, 10, 90, 1, isDone = false),
                DetailWorkoutItem("Ép ngực tạ đơn", "NGỰC", Color(0xFF2196F3), 10f, 3, 12, 60, 2, isDone = false),
                DetailWorkoutItem("Đẩy vai tạ đơn", "VAI", Color(0xFF2196F3), 12f, 3, 10, 60, 3, isDone = false),
                DetailWorkoutItem("Kéo cáp tay sau", "TAY SAU", Color(0xFF2196F3), 15f, 3, 12, 60, 4, isDone = false)
            )
            "THỨ 3" -> listOf(
                DetailWorkoutItem("Kéo tạ đòn cúi người", "LƯNG", Color(0xFFF44336), 40f, 4, 10, 90, 1, isDone = false),
                DetailWorkoutItem("Kéo xô máy cáp", "LƯNG", Color(0xFFF44336), 45f, 4, 10, 90, 2, isDone = false),
                DetailWorkoutItem("Cuốn tay trước tạ đòn", "TAY TRƯỚC", Color(0xFF2196F3), 15f, 3, 12, 60, 3, isDone = false),
                DetailWorkoutItem("Cuốn tạ đơn kiểu búa", "TAY TRƯỚC", Color(0xFF2196F3), 10f, 3, 12, 60, 4, isDone = false)
            )
            "THỨ 4" -> listOf(
                DetailWorkoutItem("Squat tạ đòn", "CHÂN", Color(0xFF4CAF50), 50f, 4, 8, 120, 1, isDone = false),
                DetailWorkoutItem("Đạp chân máy nghiêng", "CHÂN", Color(0xFF4CAF50), 80f, 4, 12, 90, 2, isDone = false),
                DetailWorkoutItem("Co gối treo xà", "BỤNG", Color(0xFF4CAF50), 0f, 3, 15, 45, 3, isDone = false),
                DetailWorkoutItem("Gập bụng cáp", "BỤNG", Color(0xFF4CAF50), 20f, 3, 15, 45, 4, isDone = false)
            )
            "THỨ 5" -> listOf(
                DetailWorkoutItem("Đẩy vai tạ đòn", "VAI", Color(0xFF2196F3), 30f, 4, 10, 90, 1, isDone = false),
                DetailWorkoutItem("Đẩy ngực dốc lên", "NGỰC", Color(0xFF2196F3), 30f, 3, 12, 60, 2, isDone = false),
                DetailWorkoutItem("Dang vai tạ đơn", "VAI", Color(0xFF2196F3), 8f, 3, 15, 45, 3, isDone = false),
                DetailWorkoutItem("Duỗi tay sau tạ đơn", "TAY SAU", Color(0xFF2196F3), 10f, 3, 12, 60, 4, isDone = false)
            )
            "THỨ 6" -> listOf(
                DetailWorkoutItem("Kéo xô máy cáp rộng tay", "LƯNG", Color(0xFFF44336), 50f, 4, 10, 90, 1, isDone = false),
                DetailWorkoutItem("Kéo cáp ngồi rốn", "LƯNG", Color(0xFFF44336), 45f, 4, 10, 90, 2, isDone = false),
                DetailWorkoutItem("Cuốn thanh đòn tay trước", "TAY TRƯỚC", Color(0xFF2196F3), 20f, 3, 10, 60, 3, isDone = false),
                DetailWorkoutItem("Cuốn tay trước trên ghế dốc", "TAY TRƯỚC", Color(0xFF2196F3), 8f, 3, 12, 60, 4, isDone = false)
            )
            else -> listOf(
                DetailWorkoutItem("Đi bộ nhanh / Jogging 30p", "CARDIO", Color(0xFFB0BEC5), 0f, 1, 30, 0, 1, isDone = false)
            )
        }
    }

    var items by remember { mutableStateOf(initialExercises) }
    var sessionStatus by remember { mutableStateOf("CHƯA TẬP") }
    var startTime by remember { mutableStateOf("--:--") }
    var endTime by remember { mutableStateOf("--:--") }
    var userNote by remember { mutableStateOf("") }

    val completedCount = items.count { it.isDone }
    val totalCount = items.size
    val progress = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f
    val progressPercent = (progress * 100).toInt()

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
            modifier = Modifier.padding(bottom = 14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clickable { onBackClick() },
                contentAlignment = Alignment.CenterStart
            ) {
                Text(text = "←", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(4.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = dayTitle, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(text = "Trạng thái: $sessionStatus • Tiến độ: $progressPercent%", color = GymAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        // TIME & SAVE CARD
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF242730))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Bắt đầu: $startTime", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Kết thúc: $endTime", color = Color.Gray, fontSize = 12.sp)
                    }

                    if (sessionStatus != "ĐÃ XONG") {
                        Button(
                            onClick = {
                                val nowStr = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                                if (sessionStatus == "CHƯA TẬP") {
                                    sessionStatus = "ĐANG TẬP"
                                    startTime = nowStr
                                    Toast.makeText(context, "Đã bắt đầu buổi tập lúc $nowStr", Toast.LENGTH_SHORT).show()
                                } else {
                                    sessionStatus = "ĐÃ XONG"
                                    endTime = nowStr

                                    val completedListNames = items.filter { it.isDone }.joinToString(", ") { it.title }
                                    val uncompletedListNames = items.filter { !it.isDone }.joinToString(", ") { it.title }
                                    val uncompletedCount = items.count { !it.isDone }
                                    val totalVolume = items.filter { it.isDone }.sumOf { (it.weightKg * it.sets * it.reps).toDouble() }.toFloat()

                                    val autoNote = if (uncompletedCount > 0) {
                                        "Đã không hoàn thiện $uncompletedCount bài tập: $uncompletedListNames (Tiến độ $progressPercent%). Tuần sau cần phân bổ thời gian tập hết!"
                                    } else {
                                        "Hoàn thành 100% mục tiêu buổi tập hôm nay!"
                                    }

                                    val finalNote = if (userNote.isNotBlank()) "$userNote • $autoNote" else autoNote

                                    dbHelper.saveWorkoutLog(
                                        username = if (currentUsername.isBlank()) "user" else currentUsername,
                                        dayKey = dayTitle,
                                        dayTitle = dayTitle,
                                        targetMuscle = "Buổi tập $dayTitle",
                                        completedCount = completedCount,
                                        totalExercises = totalCount,
                                        totalVolumeKg = totalVolume,
                                        durationMinutes = 45,
                                        startTime = startTime,
                                        endTime = endTime,
                                        completedList = completedListNames.ifBlank { "Chưa xong bài nào" },
                                        uncompletedList = uncompletedListNames.ifBlank { "Không có" },
                                        notes = finalNote
                                    )
                                    Toast.makeText(context, "Đã lưu tiến độ ($progressPercent%) & đẩy lên Admin!", Toast.LENGTH_LONG).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (sessionStatus == "CHƯA TẬP") GymAccent else Color(0xFF4CAF50)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = if (sessionStatus == "CHƯA TẬP") "🟢 BẮT ĐẦU TẬP" else "🏁 LƯU TIẾN ĐỘ ($progressPercent%)",
                                color = Color.Black,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Text(text = "✓ ĐÃ HOÀN THÀNH ($progressPercent%)", color = Color(0xFF4CAF50), fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }
            }
        }

        // DANH SÁCH BÀI TẬP (MẶC ĐỊNH BỎ TÍCH ĐỂ NGƯỜI DÙNG TỰ TÍCH KHI TẬP)
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(items) { index, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (item.isDone) Color(0xFF242730) else Color(0xFF1E2028))
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = item.muscleGroup,
                                color = item.colorBadge,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFF181A20))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (item.isDone) "✓ Đã tập" else "✗ Chưa tập",
                                color = if (item.isDone) Color(0xFF4CAF50) else Color(0xFFFF5252),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = item.formattedInfo,
                            color = if (item.isDone) Color.White else Color.LightGray,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
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
                            checkedColor = Color(0xFF4CAF50),
                            uncheckedColor = Color.Gray,
                            checkmarkColor = Color.White
                        )
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = userNote,
                    onValueChange = { userNote = it },
                    placeholder = { Text("Ô ghi chú: cảm nhận buổi tập / bài tập chưa làm tốt...", color = Color.Gray, fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GymAccent
                    )
                )
            }
        }

        // PROGRESS BAR
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("TIẾN ĐỘ HOÀN THÀNH BÀI TẬP", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text("$completedCount / $totalCount bài ($progressPercent%)", color = GymAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = if (progressPercent == 100) Color(0xFF4CAF50) else GymAccent,
                trackColor = Color(0xFF333842)
            )
        }
    }
}
