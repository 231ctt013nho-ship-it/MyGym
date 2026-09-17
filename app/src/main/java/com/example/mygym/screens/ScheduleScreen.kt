package com.example.mygym.screens

import android.content.Context

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.mygym.components.PrimaryButton
import com.example.mygym.components.SecondaryButton
import com.example.mygym.data.AccountStorage
import com.example.mygym.ui.theme.GymAccent
import com.example.mygym.ui.theme.GymCard
import com.example.mygym.ui.theme.GymDivider
import com.example.mygym.ui.theme.GymGray
import com.example.mygym.ui.theme.GymWhite

/**
 * Model lưu thông tin một ngày tập.
 *
 * day:
 * Thứ trong tuần.
 *
 * title:
 * Nội dung buổi tập.
 *
 * exercises:
 * Danh sách bài tập trong ngày.
 */
data class WorkoutDay(
    val day: String,
    val title: String,
    val exercises: List<String>
)

/**
 * Lịch tập trong tuần.
 */
val weeklyWorkoutPlan = listOf(
    WorkoutDay(
        "THỨ 2",
        "Ngực & Tay sau",
        listOf(
            "Đẩy ngực",
            "Đẩy ngực dốc",
            "Kéo cáp tay sau"
        )
    ),

    WorkoutDay(
        "THỨ 3",
        "Lưng & Tay trước",
        listOf(
            "Kéo xô",
            "Kéo tạ đòn",
            "Cuốn tay trước"
        )
    ),

    WorkoutDay(
        "THỨ 4",
        "Nghỉ phục hồi",
        emptyList()
    ),

    WorkoutDay(
        "THỨ 5",
        "Chân",
        listOf(
            "Squat",
            "Đạp chân"
        )
    ),

    WorkoutDay(
        "THỨ 6",
        "Ngực & Tay",
        listOf(
            "Ép ngực tạ đơn",
            "Cuốn tay trước",
            "Kéo cáp tay sau"
        )
    ),

    WorkoutDay(
        "THỨ 7",
        "Toàn thân",
        listOf(
            "Squat",
            "Đẩy ngực",
            "Kéo xô"
        )
    ),

    WorkoutDay(
        "CHỦ NHẬT",
        "Nghỉ & phục hồi",
        emptyList()
    )
)

@Composable
fun ScheduleScreen(
    context: Context,
    onBackClick: () -> Unit,
    onProgressClick: () -> Unit
) {
    // Lấy danh sách các ngày đã hoàn thành
    // từ bộ nhớ của Android.
    val completedDays = remember {
        mutableStateOf(
            AccountStorage.getCompletedDays(context)
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),

        verticalArrangement =
            Arrangement.spacedBy(12.dp)
    ) {

        // ==============================
        // TIÊU ĐỀ
        // ==============================

        item {
            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text = "‹",
                    fontSize = 38.sp,
                    color = GymWhite,

                    modifier =
                        Modifier.clickable {
                            onBackClick()
                        }
                )

                Spacer(
                    Modifier.width(8.dp)
                )

                Column {

                    Text(
                        text = "Lịch tập",
                        fontSize = 27.sp,
                        fontWeight =
                            FontWeight.ExtraBold,
                        color = GymWhite
                    )

                    Text(
                        text =
                            "KẾ HOẠCH TẬP LUYỆN TRONG TUẦN",
                        fontSize = 10.sp,
                        fontWeight =
                            FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        color = GymGray
                    )
                }
            }
        }

        // ==============================
        // MỤC TIÊU TUẦN
        // ==============================

        item {
            Card(
                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(20.dp),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            GymAccent
                    )
            ) {

                Column(
                    modifier =
                        Modifier.padding(18.dp)
                ) {

                    Text(
                        text = "MỤC TIÊU TUẦN",
                        fontSize = 10.sp,
                        fontWeight =
                            FontWeight.ExtraBold,
                        letterSpacing = 1.2.sp,
                        color =
                            Color(0xFF101522)
                    )

                    Spacer(
                        Modifier.height(5.dp)
                    )

                    Text(
                        text =
                            "${completedDays.value.size} / " +
                                    "${weeklyWorkoutPlan.count { it.exercises.isNotEmpty() }} " +
                                    "buổi đã hoàn thành",

                        fontSize = 21.sp,
                        fontWeight =
                            FontWeight.ExtraBold,

                        color =
                            Color(0xFF080B12)
                    )
                }
            }
        }

        // ==============================
        // DANH SÁCH LỊCH TẬP
        // ==============================

        items(weeklyWorkoutPlan) { day ->

            val isRest =
                day.exercises.isEmpty()

            val isDone =
                completedDays.value.contains(
                    day.day
                )

            Card(
                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(20.dp),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            GymCard
                    ),

                border =
                    BorderStroke(
                        1.dp,
                        GymDivider
                    )
            ) {

                Column(
                    modifier =
                        Modifier.padding(17.dp)
                ) {

                    Row(
                        modifier =
                            Modifier.fillMaxWidth(),

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Column(
                            modifier =
                                Modifier.weight(1f)
                        ) {

                            Text(
                                text = day.day,
                                fontSize = 10.sp,
                                fontWeight =
                                    FontWeight.ExtraBold,
                                letterSpacing = 1.sp,
                                color = GymAccent
                            )

                            Spacer(
                                Modifier.height(3.dp)
                            )

                            Text(
                                text = day.title,
                                fontSize = 18.sp,
                                fontWeight =
                                    FontWeight.ExtraBold,
                                color = GymWhite
                            )
                        }

                        // Ngày nghỉ không có
                        // nút hoàn thành.
                        if (!isRest) {

                            SecondaryButton(
                                text =
                                    if (isDone)
                                        "ĐÃ XONG"
                                    else
                                        "HOÀN THÀNH",

                                onClick = {

                                    val newSet =
                                        completedDays
                                            .value
                                            .toMutableSet()

                                    if (isDone) {
                                        newSet.remove(
                                            day.day
                                        )
                                    } else {
                                        newSet.add(
                                            day.day
                                        )
                                    }

                                    completedDays.value =
                                        newSet

                                    // Lưu lại trạng thái
                                    // vào SharedPreferences.
                                    AccountStorage
                                        .saveCompletedDays(
                                            context,
                                            newSet
                                        )
                                }
                            )
                        }
                    }

                    if (!isRest) {

                        Spacer(
                            Modifier.height(12.dp)
                        )

                        day.exercises
                            .forEachIndexed {
                                    index,
                                    exercise ->

                                Text(
                                    text =
                                        "${index + 1}. $exercise",

                                    fontSize = 13.sp,
                                    color = GymGray,

                                    modifier =
                                        Modifier.padding(
                                            vertical = 3.dp
                                        )
                                )
                            }

                    } else {

                        Spacer(
                            Modifier.height(7.dp)
                        )

                        Text(
                            text =
                                "Ngày phục hồi — " +
                                        "nghỉ ngơi và bổ sung năng lượng.",

                            fontSize = 13.sp,
                            color = GymGray
                        )
                    }
                }
            }
        }

        // ==============================
        // XEM TIẾN ĐỘ
        // ==============================

        item {

            PrimaryButton(
                text = "XEM TIẾN ĐỘ",

                onClick =
                    onProgressClick,

                modifier =
                    Modifier.fillMaxWidth()
            )
        }
    }
}