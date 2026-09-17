package com.example.mygym.screens

import android.content.Context

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.mygym.components.DarkCard
import com.example.mygym.components.DetailStatCard
import com.example.mygym.components.SecondaryButton

import com.example.mygym.data.AccountStorage

import com.example.mygym.ui.theme.GymAccent
import com.example.mygym.ui.theme.GymCard
import com.example.mygym.ui.theme.GymGray
import com.example.mygym.ui.theme.GymWhite
import com.example.mygym.ui.theme.GymDivider


@Composable
fun ProgressScreen(
    context: Context,
    onBackClick: () -> Unit
) {

    // =========================================================
    // LẤY DANH SÁCH NGÀY ĐÃ HOÀN THÀNH
    // =========================================================

    val completedDays = remember {
        mutableStateOf(
            AccountStorage.getCompletedDays(context)
        )
    }


    // =========================================================
    // TỔNG SỐ NGÀY TẬP
    // =========================================================

    val totalWorkoutDays =
        weeklyWorkoutPlan.count {
            it.exercises.isNotEmpty()
        }


    // =========================================================
    // SỐ NGÀY ĐÃ HOÀN THÀNH
    // =========================================================

    val completedCount =
        completedDays.value
            .size
            .coerceAtMost(totalWorkoutDays)


    // =========================================================
    // TÍNH PHẦN TRĂM TIẾN ĐỘ
    // =========================================================

    val progress =
        if (totalWorkoutDays > 0) {
            completedCount.toFloat() / totalWorkoutDays.toFloat()
        } else {
            0f
        }


    val progressPercent =
        (progress * 100).toInt()


    // =========================================================
    // GIAO DIỆN
    // =========================================================

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 20.dp,
                vertical = 22.dp
            ),

        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // =====================================================
        // TIÊU ĐỀ
        // =====================================================

        item {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "‹",
                    fontSize = 38.sp,
                    color = GymWhite,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .then(
                            Modifier
                        )
                )

                Column {

                    Text(
                        text = "TIẾN ĐỘ",
                        fontSize = 27.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = GymWhite
                    )

                    Text(
                        text = "THEO DÕI KẾT QUẢ TẬP LUYỆN",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.3.sp,
                        color = GymGray
                    )
                }
            }
        }


        // =====================================================
        // CARD TIẾN ĐỘ
        // =====================================================

        item {

            Card(
                modifier = Modifier.fillMaxWidth(),

                colors = CardDefaults.cardColors(
                    containerColor = GymCard
                ),

                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    GymDivider
                ),

                shape = androidx.compose.foundation.shape.RoundedCornerShape(
                    20.dp
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {

                    Text(
                        text = "TIẾN ĐỘ TUẦN",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.2.sp,
                        color = GymGray
                    )

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Bottom
                    ) {

                        Text(
                            text = "$progressPercent%",
                            fontSize = 40.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = GymAccent
                        )

                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )

                        Text(
                            text = "hoàn thành",
                            fontSize = 13.sp,
                            color = GymGray,
                            modifier = Modifier.padding(
                                bottom = 7.dp
                            )
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(14.dp)
                    )

                    // Thanh tiến độ
                    androidx.compose.material3.LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp),

                        color = GymAccent,

                        trackColor = GymDivider
                    )

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )

                    Text(
                        text = "$completedCount / $totalWorkoutDays ngày tập đã hoàn thành",
                        fontSize = 12.sp,
                        color = GymGray
                    )
                }
            }
        }


        // =====================================================
        // THỐNG KÊ
        // =====================================================

        item {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                DetailStatCard(
                    label = "NGÀY HOÀN THÀNH",
                    value = completedCount.toString(),
                    modifier = Modifier.weight(1f)
                )

                DetailStatCard(
                    label = "TỔNG NGÀY TẬP",
                    value = totalWorkoutDays.toString(),
                    modifier = Modifier.weight(1f)
                )
            }
        }


        // =====================================================
        // THÔNG TIN
        // =====================================================

        item {

            DarkCard {

                Text(
                    text = "THỐNG KÊ TẬP LUYỆN",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.2.sp,
                    color = GymGray
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Text(
                    text = if (completedCount == 0) {
                        "Bạn chưa hoàn thành buổi tập nào."
                    } else {
                        "Bạn đã hoàn thành $completedCount buổi tập. Hãy tiếp tục duy trì lịch tập!"
                    },

                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    color = GymWhite
                )
            }
        }


        // =====================================================
        // NÚT QUAY LẠI
        // =====================================================

        item {

            SecondaryButton(
                text = "QUAY LẠI",
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}