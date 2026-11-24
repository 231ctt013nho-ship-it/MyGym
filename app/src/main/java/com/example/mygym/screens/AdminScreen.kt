package com.example.mygym.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.mygym.R
import com.example.mygym.data.GymDatabaseHelper
import com.example.mygym.ui.theme.GymAccent
import com.example.mygym.ui.theme.GymBackground

@Composable
fun AdminScreen(
    context: Context,
    onLogout: () -> Unit
) {
    val dbHelper = remember { GymDatabaseHelper(context) }

    var exercises by remember { mutableStateOf(dbHelper.getAllExercises()) }
    var workoutLogs by remember { mutableStateOf(dbHelper.getAllWorkoutLogs()) }
    var usersList by remember { mutableStateOf(dbHelper.getAllUsers()) }
    var passwordResetLogs by remember { mutableStateOf(dbHelper.getAllPasswordResets()) }

    var selectedTab by remember { mutableStateOf("EXERCISES") } // "EXERCISES", "LOGS", "SECURITY"

    var name by remember { mutableStateOf("") }
    var muscle by remember { mutableStateOf("CHEST") }
    var sets by remember { mutableStateOf("4 hiệp") }
    var reps by remember { mutableStateOf("10-12 lần") }
    val muscleOptions = listOf("CHEST", "BACK", "LEGS", "BICEPS", "TRICEPS")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GymBackground)
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        // HEADER ADMIN
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("QUẢN TRỊ HỆ THỐNG MYGYM", color = GymAccent, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                Text("Admin System • ${usersList.size} Thành viên • ${workoutLogs.size} Buổi tập", color = Color.Gray, fontSize = 11.sp)
            }
            Button(
                onClick = onLogout,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Đăng xuất", color = Color.White, fontSize = 11.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // THANH CHUYỂN TAB ADMIN (Bài tập DB / Nhật ký người dùng / Mật khẩu & Gmail OTP)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Button(
                onClick = { selectedTab = "EXERCISES" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedTab == "EXERCISES") GymAccent else Color(0xFF242730)
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text("🏋️ BÀI TẬP", color = if (selectedTab == "EXERCISES") Color.Black else Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = {
                    workoutLogs = dbHelper.getAllWorkoutLogs()
                    selectedTab = "LOGS"
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedTab == "LOGS") GymAccent else Color(0xFF242730)
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text("📊 NHẬT KÝ", color = if (selectedTab == "LOGS") Color.Black else Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = {
                    usersList = dbHelper.getAllUsers()
                    passwordResetLogs = dbHelper.getAllPasswordResets()
                    selectedTab = "SECURITY"
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedTab == "SECURITY") GymAccent else Color(0xFF242730)
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1.2f)
            ) {
                Text("🔒 BẢO MẬT/OTP", color = if (selectedTab == "SECURITY") Color.Black else Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        when (selectedTab) {
            "EXERCISES" -> {
                // FORM THÊM BÀI TẬP MỚI VÀO DATABASE
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF242730))
                        .padding(14.dp)
                ) {
                    Text("THÊM BÀI TẬP MỚI VÀO CSDL", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = { Text("Tên bài tập (VD: Đẩy vai tạ đơn)", color = Color.Gray, fontSize = 12.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = GymAccent
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        muscleOptions.forEach { item ->
                            val isSelected = muscle == item
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isSelected) GymAccent else Color(0xFF181A20))
                                    .clickable { muscle = item }
                                    .padding(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = item,
                                    color = if (isSelected) Color.Black else Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = sets,
                            onValueChange = { sets = it },
                            placeholder = { Text("Số hiệp", color = Color.Gray, fontSize = 12.sp) },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                        )
                        OutlinedTextField(
                            value = reps,
                            onValueChange = { reps = it },
                            placeholder = { Text("Số lần", color = Color.Gray, fontSize = 12.sp) },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            if (name.isBlank()) {
                                Toast.makeText(context, "Vui lòng nhập tên bài tập", Toast.LENGTH_SHORT).show()
                            } else {
                                dbHelper.insertExercise(
                                    name = name.trim(),
                                    muscle = muscle,
                                    sets = sets,
                                    reps = reps,
                                    description = "Thực hiện đúng kỹ thuật, kiểm soát nhịp thở đều đặn.",
                                    imageRes = R.drawable.bench_press
                                )
                                exercises = dbHelper.getAllExercises()
                                name = ""
                                Toast.makeText(context, "Đã thêm vào CSDL hệ thống!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = GymAccent),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("+ LƯU VÀO DATABASE", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Text("BÀI TẬP TRONG DATABASE (${exercises.size})", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(exercises) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF1E2129))
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.name, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                Text("${item.muscle} • ${item.sets} x ${item.reps}", color = GymAccent, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }

            "LOGS" -> {
                // HIỂN THỊ TẤT CẢ BUỔI TẬP CỦA NGƯỜI DÙNG
                Text("NHẬT KÝ TẬP THÀNH VIÊN ĐẨY LÊN ADMIN", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                if (workoutLogs.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(30.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Chưa có buổi tập nào trong hệ thống.", color = Color.Gray, fontSize = 13.sp)
                    }
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(workoutLogs) { log ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFF242730))
                                    .padding(14.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "👤 ${log.username.uppercase()} • ${log.dayTitle}",
                                        color = GymAccent,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = log.dateStr, color = Color.Gray, fontSize = 11.sp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "🗑️ Xóa",
                                            color = Color(0xFFFF5252),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(Color(0xFF2C1E20))
                                                .clickable {
                                                    dbHelper.deleteWorkoutLog(log.id)
                                                    workoutLogs = dbHelper.getAllWorkoutLogs()
                                                    Toast.makeText(context, "Đã xóa nhật ký tập thành công!", Toast.LENGTH_SHORT).show()
                                                }
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Nhóm cơ: ${log.targetMuscle}", color = Color.White, fontSize = 12.sp)
                                Text(
                                    text = "Hoàn thành: ${log.completedCount}/${log.totalExercises} bài (${log.progressPercent}%) • Tạ: ${log.totalVolumeKg.toInt()} kg",
                                    color = Color(0xFF4CAF50),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Ghi chú: ${log.notes}", color = Color.LightGray, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }

            "SECURITY" -> {
                // ADMIN QUẢN LÝ MẬT KHẨU TÀI KHOẢN VÀ GỬI MÃ OTP QUA GMAIL
                Text("QUẢN LÝ MẬT KHẨU TÀI KHOẢN & MÃ OTP GMAIL", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(10.dp))

                // DANH SÁCH THÀNH VIÊN VÀ NÚT GỬI MÃ OTP QUA GMAIL
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    item {
                        Text("1. DANH SÁCH TÀI KHOẢN VÀ GỬI MÃ OTP GMAIL", color = GymAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                    }

                    items(usersList) { user ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF242730))
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = "👤 ${user.username} (${user.role})", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    Text(text = "📧 ${user.email.ifBlank { "Chưa cập nhật Gmail" }}", color = Color.Gray, fontSize = 11.sp)
                                }

                                Button(
                                    onClick = {
                                        val targetEmail = if (user.email.isNotBlank()) user.email else "${user.username}@gmail.com"
                                        val newOtp = (100000..999999).random().toString()
                                        dbHelper.logPasswordReset(
                                            username = user.username,
                                            email = targetEmail,
                                            otpCode = newOtp,
                                            status = "ADMIN ĐÃ GỬI MÃ OTP"
                                        )
                                        passwordResetLogs = dbHelper.getAllPasswordResets()
                                        Toast.makeText(context, "Đã gửi mã OTP $newOtp qua Gmail $targetEmail!", Toast.LENGTH_LONG).show()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = GymAccent),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text("📧 Gửi OTP Gmail", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(14.dp))
                        Text("2. NHẬT KÝ KIỂM SOÁT ĐỔI MẬT KHẨU QUA GMAIL", color = GymAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                    }

                    if (passwordResetLogs.isEmpty()) {
                        item {
                            Text("Chưa có lịch sử cấp mã OTP hoặc đổi mật khẩu.", color = Color.Gray, fontSize = 11.sp)
                        }
                    } else {
                        items(passwordResetLogs) { resetEntry ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF1E2129))
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = "👤 ${resetEntry.username} • 📧 ${resetEntry.email}", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                    Text(text = "Mã OTP: ${resetEntry.otpCode} • Trạng thái: ${resetEntry.status}", color = GymAccent, fontSize = 11.sp)
                                }
                                Text(text = resetEntry.createdAt, color = Color.Gray, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
