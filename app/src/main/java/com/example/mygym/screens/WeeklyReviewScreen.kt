package com.example.mygym.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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

/**
 * Màn hình Xem lại Tập Luyện Cấu Trúc Đa Tầng (Tháng -> Tuần -> Ngày -> Bài Đã Tập / Chưa Tập)
 */
@Composable
fun WeeklyReviewScreen(
    context: Context,
    onBackClick: () -> Unit
) {
    val dbHelper = remember { GymDatabaseHelper(context) }
    val currentUsername = AccountStorage.getUsername(context)
    val targetUser = if (currentUsername.isBlank()) "user" else currentUsername

    var userLogs by remember { mutableStateOf(dbHelper.getUserWorkoutLogs(targetUser)) }

    // 1. CHỌN THÁNG (Bấm vào Tháng sẽ hiện ra các Tuần)
    val monthOptions = listOf("Tháng 10/2026", "Tháng 11/2026", "Tháng 12/2026")
    var selectedMonth by remember { mutableStateOf("Tháng 11/2026") }

    // 2. CHỌN TUẦN (Bấm vào Tuần sẽ hiện các Ngày Thứ 2 đến Chủ Nhật)
    val weeksList = listOf("Tuần 1", "Tuần 2", "Tuần 3", "Tuần 4")
    var selectedWeekIndex by remember { mutableStateOf(0) } // 0: Tuần 1, 1: Tuần 2, 2: Tuần 3, 3: Tuần 4

    // 3. CHỌN NGÀY (Bấm vào Thứ 2 sẽ hiện các Bài đã tập & Chưa tập)
    val daysList = listOf(
        Pair("THỨ 2", "Thứ 2 • Ngực/Vai/Tay"),
        Pair("THỨ 3", "Thứ 3 • Lưng/Tay trước"),
        Pair("THỨ 4", "Thứ 4 • Chân/Bụng"),
        Pair("THỨ 5", "Thứ 5 • Vai/Ngực/Tay"),
        Pair("THỨ 6", "Thứ 6 • Lưng/Tay trước"),
        Pair("THỨ 7", "Thứ 7 • Cardio nhẹ"),
        Pair("CHỦ NHẬT", "Chủ Nhật • Nghỉ hoàn toàn")
    )
    var selectedDayKey by remember { mutableStateOf("THỨ 2") }

    // Lấy thông tin bản ghi tập thực tế từ CSDL
    val currentLog = remember(selectedDayKey, userLogs) {
        userLogs.find { it.dayKey.equals(selectedDayKey, ignoreCase = true) }
    }

    var editNotes by remember(selectedDayKey, currentLog) { mutableStateOf(currentLog?.notes ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GymBackground)
            .statusBarsPadding()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
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
                Text(text = "←", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(4.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "XEM LẠI THEO THÁNG & TUẦN",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "Tháng → Tuần → Ngày → Bài đã tập & Chưa tập",
                    color = Color.Gray,
                    fontSize = 11.sp
                )
            }
        }

        // BƯỚC 1: BẤM CHỌN THÁNG (BẤM VÀO THÁNG SẼ HIỆN RA CÁC TUẦN)
        Text(text = "1️⃣ CHỌN THÁNG TẬP LUYỆN", color = GymAccent, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            monthOptions.forEach { month ->
                val isSelected = selectedMonth == month
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) GymAccent else Color(0xFF242730))
                        .clickable { selectedMonth = month }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = month,
                        color = if (isSelected) Color.Black else Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // BƯỚC 2: BẤM CHỌN TUẦN TRONG THÁNG (BẤM VÀO TUẦN SẼ HIỆN CÁC NGÀY THỨ 2 -> CN)
        Text(
            text = "2️⃣ BẤM CHỌN TUẦN CỦA $selectedMonth",
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF242730))
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    weeksList.forEachIndexed { idx, weekName ->
                        val isSelected = selectedWeekIndex == idx
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) GymAccent else Color(0xFF181A20))
                                .clickable { selectedWeekIndex = idx }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = weekName,
                                color = if (isSelected) Color.Black else Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }
        }

        // BƯỚC 3: BẤM CHỌN NGÀY TẬP (TỪ THỨ 2 ĐẾN CHỦ NHẬT CỦA TUẦN ĐÃ CHỌN)
        val currentWeekName = weeksList[selectedWeekIndex]
        Text(
            text = "3️⃣ CÁC NGÀY TRONG $currentWeekName ($selectedMonth)",
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            itemsIndexed(daysList) { _, pair ->
                val dayKey = pair.first
                val isSelected = selectedDayKey == dayKey
                val hasLog = userLogs.any { it.dayKey.equals(dayKey, ignoreCase = true) }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) Color(0xFF2196F3) else Color(0xFF242730))
                        .clickable { selectedDayKey = dayKey }
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = dayKey,
                            color = if (isSelected) Color.Black else GymAccent,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (hasLog) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "•", color = Color(0xFF4CAF50), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // BƯỚC 4: BẤM VÀO THỨ 2 SẼ HIỆN RA CÁC BÀI ĐÃ TẬP VÀ CHƯA TẬP
        Text(
            text = "4️⃣ BÀI TẬP $selectedDayKey ($currentWeekName - $selectedMonth)",
            color = Color(0xFF2196F3),
            fontSize = 13.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (currentLog == null) {
            // CHƯA CÓ DỮ LIỆU TẬP CHO NGÀY NÀY
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2230)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF333842))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "📭 CHƯA CÓ DỮ LIỆU TẬP $selectedDayKey", color = Color.Gray, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Bạn chưa lưu tiến độ tập luyện ngày $selectedDayKey ($currentWeekName - $selectedMonth). Khi bạn thực hiện bài tập và bấm 'Lưu tiến độ' trong Lịch tập, các bài đã tập và chưa tập sẽ tự động hiện tại đây!",
                        color = Color.LightGray,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        } else {
            // ĐÃ CÓ DỮ LIỆU: BẤM VÀO THỨ 2 HIỆN CÁC BÀI ĐÃ TẬP VÀ CHƯA TẬP
            val completedCount = currentLog.completedCount
            val totalCount = currentLog.totalExercises
            val progressPercent = currentLog.progressPercent
            val completedListStr = currentLog.completedList
            val uncompletedListStr = currentLog.uncompletedList

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2230)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2196F3).copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "📋 $selectedDayKey • $currentWeekName",
                            color = Color(0xFF2196F3),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "Tiến độ: $progressPercent% ($completedCount/$totalCount bài)",
                            color = if (progressPercent == 100) Color(0xFF4CAF50) else GymAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFF181A20))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { progressPercent.toFloat() / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = if (progressPercent == 100) Color(0xFF4CAF50) else GymAccent,
                        trackColor = Color(0xFF333842)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // BÀI MÌNH ĐÃ TẬP
                    Text(
                        text = "✓ CÁC BÀI BẠN ĐÃ TẬP:",
                        color = Color(0xFF4CAF50),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    if (completedListStr.isNotBlank() && completedListStr != "Chưa xong bài nào") {
                        completedListStr.split(", ").forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF181A20))
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "✓ ", color = Color(0xFF4CAF50), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text(text = item, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    } else {
                        Text(text = "Chưa hoàn thành bài nào", color = Color.Gray, fontSize = 11.sp)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // BÀI MÌNH CHƯA TẬP / NGHỈ TẬP
                    val uncompletedCount = totalCount - completedCount
                    Text(
                        text = "✗ CÁC BÀI CHƯA TẬP / NGHỈ TẬP ($uncompletedCount BÀI):",
                        color = Color(0xFFFF5252),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    if (uncompletedCount > 0 && uncompletedListStr.isNotBlank() && uncompletedListStr != "Không có") {
                        uncompletedListStr.split(", ").forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF2C1E20))
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "✗ Đã bỏ qua bài: ", color = Color(0xFFFF5252), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text(text = item, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF181A20))
                                .padding(horizontal = 10.dp, vertical = 8.dp)
                        ) {
                            Text(text = "🎉 Đã tập hoàn thiện tất cả các bài tập!", color = Color(0xFF4CAF50), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // GHI CHÚ BUỔI TẬP VÀ ĐÁNH GIÁ
                    Text(text = "📝 GHI CHÚ ĐÁNH GIÁ $selectedDayKey ($currentWeekName):", color = GymAccent, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = editNotes,
                        onValueChange = { editNotes = it },
                        label = { Text("Ghi chú đánh giá tiến độ", color = GymAccent, fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = GymAccent
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            dbHelper.updateWorkoutLogNotes(currentLog.id, editNotes)
                            userLogs = dbHelper.getUserWorkoutLogs(targetUser)
                            Toast.makeText(context, "Đã lưu ghi chú ngày $selectedDayKey vào Database!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = GymAccent),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "💾 LƯU ĐÁNH GIÁ VÀO DATABASE", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
