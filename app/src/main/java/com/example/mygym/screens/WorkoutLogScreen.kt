package com.example.mygym.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mygym.components.SecondaryButton
import com.example.mygym.data.AccountStorage
import com.example.mygym.ui.theme.GymAccent
import com.example.mygym.ui.theme.GymBackground
import com.example.mygym.ui.theme.GymCard
import com.example.mygym.ui.theme.GymDivider
import com.example.mygym.ui.theme.GymGray
import com.example.mygym.ui.theme.GymWhite

@Composable
fun WorkoutLogScreen(
    context: Context,
    onBackClick: () -> Unit
) {
    val completedDays = remember {
        AccountStorage.getCompletedDays(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GymBackground)
            .padding(horizontal = 20.dp)
    ) {

        // =========================================================
        // HEADER
        // =========================================================

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            SecondaryButton(
                text = "←",
                onClick = onBackClick,
                modifier = Modifier.size(48.dp)
            )

            Text(
                text = "Lịch sử tập",
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                color = GymWhite,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // =========================================================
        // TIÊU ĐỀ
        // =========================================================

        Text(
            text = "LỊCH SỬ LUYỆN TẬP",
            color = GymGray,
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        // =========================================================
        // THỐNG KÊ
        // =========================================================

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = GymCard
            )
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                WorkoutLogStat(
                    value = completedDays.size.toString(),
                    label = "BUỔI ĐÃ TẬP"
                )

                WorkoutLogStat(
                    value = "MyGym",
                    label = "ỨNG DỤNG"
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // =========================================================
        // DANH SÁCH
        // =========================================================

        if (completedDays.isEmpty()) {

            EmptyWorkoutLog()

        } else {

            Text(
                text = "CÁC BUỔI ĐÃ HOÀN THÀNH",
                color = GymGray,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                items(
                    items = completedDays.toList()
                ) { day ->

                    WorkoutLogCard(
                        day = day
                    )
                }
            }
        }
    }
}


// =============================================================
// STAT
// =============================================================

@Composable
private fun WorkoutLogStat(
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = value,
            color = GymAccent,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            color = GymGray,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp
        )
    }
}


// =============================================================
// WORKOUT LOG CARD
// =============================================================

@Composable
private fun WorkoutLogCard(
    day: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(
            containerColor = GymCard
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            GymDivider
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(17.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Card(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = GymAccent
                )
            ) {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = "✓",
                        color = Color(0xFF0A0D15),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.size(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = day,
                    color = GymWhite,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Đã hoàn thành buổi tập",
                    color = GymGray,
                    fontSize = 12.sp
                )
            }

            Text(
                text = "HOÀN THÀNH",
                color = GymAccent,
                fontSize = 9.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}


// =============================================================
// EMPTY STATE
// =============================================================

@Composable
private fun EmptyWorkoutLog() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = GymCard
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            GymDivider
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(35.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "🏋️",
                fontSize = 42.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Chưa có lịch sử tập",
                color = GymWhite,
                fontSize = 17.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(7.dp))

            Text(
                text = "Hoàn thành một buổi tập để lịch sử xuất hiện ở đây.",
                modifier = Modifier.fillMaxWidth(),
                color = GymGray,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}