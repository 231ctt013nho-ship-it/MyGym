package com.example.mygym

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

// =====================================================
// CHỨC NĂNG TÍNH CHỈ SỐ BMI
// =====================================================
@Composable
fun BmiCalculatorScreen(onBackClick: () -> Unit) {
    var heightState by remember { mutableStateOf("") }
    var weightState by remember { mutableStateOf("") }
    var bmiResult by remember { mutableStateOf<Double?>(null) }
    var bmiCategory by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GymBackground)
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("←", fontSize = 24.sp, color = GymWhite, modifier = Modifier.clickable { onBackClick() })
            Spacer(Modifier.width(16.dp))
            Text("TÍNH CHỈ SỐ BMI", fontSize = 20.sp, color = GymWhite, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(20.dp))

        DarkCard {
            ModernTextField(
                value = heightState,
                onValueChange = { heightState = it },
                label = "Chiều cao (cm)"
            )
            Spacer(Modifier.height(12.dp))
            ModernTextField(
                value = weightState,
                onValueChange = { weightState = it },
                label = "Cân nặng (kg)"
            )

            Spacer(Modifier.height(20.dp))

            PrimaryButton(
                text = "TÍNH BMI",
                onClick = {
                    val h = heightState.toDoubleOrNull()?.div(100)
                    val w = weightState.toDoubleOrNull()
                    if (h != null && w != null && h > 0) {
                        val bmi = w / (h * h)
                        bmiResult = bmi
                        bmiCategory = when {
                            bmi < 18.5 -> "Gầy (Cần bổ sung dinh dưỡng)"
                            bmi < 24.9 -> "Bình thường (Cơ thể cân đối)"
                            bmi < 29.9 -> "Thừa cân (Nên tập Cardio thêm)"
                            else -> "Béo phì (Cần chế độ tập nghiêm ngặt)"
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        bmiResult?.let { bmi ->
            Spacer(Modifier.height(20.dp))
            DarkCard {
                Text("KẾT QUẢ CỦA BẠN", fontSize = 12.sp, color = GymAccent, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text(
                    text = String.format("%.1f", bmi),
                    fontSize = 36.sp,
                    color = GymWhite,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = bmiCategory,
                    fontSize = 15.sp,
                    color = if (bmi in 18.5..24.9) Color.Green else GymRed,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// =====================================================
// CHỨC NĂNG ĐẾM GIỜ NGHỈ GIỮA HIỆP (TIMER)
// =====================================================
@Composable
fun TimerScreen(onBackClick: () -> Unit) {
    var timeLeft by remember { mutableStateOf(60) }
    var isRunning by remember { mutableStateOf(false) }

    LaunchedEffect(isRunning, timeLeft) {
        if (isRunning && timeLeft > 0) {
            delay(1000L)
            timeLeft--
        } else if (timeLeft == 0) {
            isRunning = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GymBackground)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("←", fontSize = 24.sp, color = GymWhite, modifier = Modifier.clickable { onBackClick() })
            Spacer(Modifier.width(16.dp))
            Text("BẤM GIỜ NGHỈ HIỆP", fontSize = 20.sp, color = GymWhite, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(40.dp))

        DarkCard {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${timeLeft / 60}:${String.format("%02d", timeLeft % 60)}",
                    fontSize = 52.sp,
                    color = GymAccent,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(Modifier.height(20.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SecondaryButton(text = "30s", onClick = { timeLeft = 30; isRunning = false }, modifier = Modifier.weight(1f))
                    SecondaryButton(text = "60s", onClick = { timeLeft = 60; isRunning = false }, modifier = Modifier.weight(1f))
                    SecondaryButton(text = "90s", onClick = { timeLeft = 90; isRunning = false }, modifier = Modifier.weight(1f))
                }

                Spacer(Modifier.height(20.dp))

                PrimaryButton(
                    text = if (isRunning) "TẠM DỪNG" else "BẮT ĐẦU NGHỈ",
                    onClick = { isRunning = !isRunning },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}