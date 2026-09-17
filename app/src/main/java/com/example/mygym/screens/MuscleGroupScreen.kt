package com.example.mygym.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.mygym.data.muscleGroups
import com.example.mygym.ui.theme.GymAccent
import com.example.mygym.ui.theme.GymCard
import com.example.mygym.ui.theme.GymDivider
import com.example.mygym.ui.theme.GymGray
import com.example.mygym.ui.theme.GymWhite

@Composable
fun MuscleGroupScreen(
    onBackClick: () -> Unit,
    onMuscleSelected: (String) -> Unit
) {
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
                        text = "Nhóm cơ",
                        fontSize = 27.sp,
                        fontWeight =
                            FontWeight.ExtraBold,
                        color = GymWhite
                    )

                    Text(
                        text =
                            "CHỌN NHÓM CƠ MUỐN TẬP LUYỆN",

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
        // DANH SÁCH NHÓM CƠ
        // ==============================

        items(
            items = muscleGroups
        ) { group ->

            // Không hiển thị "Tất cả"
            // trong màn hình chọn nhóm cơ.
            if (group.name != "Tất cả") {

                MuscleGroupModernCard(
                    name = group.name,
                    shortName = group.shortName,

                    onClick = {
                        onMuscleSelected(
                            group.name
                        )
                    }
                )
            }
        }

        // ==============================
        // QUAY LẠI
        // ==============================

        item {

            Spacer(
                Modifier.height(4.dp)
            )

            Card(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            onBackClick()
                        },

                shape =
                    RoundedCornerShape(16.dp),

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

                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Text(
                        text = "QUAY LẠI",
                        fontSize = 11.sp,
                        fontWeight =
                            FontWeight.ExtraBold,
                        letterSpacing = 1.sp,
                        color = GymGray
                    )
                }
            }
        }
    }
}


// =====================================================
// CARD NHÓM CƠ
// =====================================================

@Composable
fun MuscleGroupModernCard(
    name: String,
    shortName: String,
    onClick: () -> Unit
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable {
                    onClick()
                },

        shape =
            RoundedCornerShape(20.dp),

        colors =
            CardDefaults.cardColors(
                containerColor = GymCard
            ),

        border =
            BorderStroke(
                1.dp,
                GymDivider
            )
    ) {

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(18.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            // ==============================
            // TÊN VIẾT TẮT
            // ==============================

            Card(
                shape =
                    RoundedCornerShape(14.dp),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            GymAccent.copy(
                                alpha = 0.15f
                            )
                    )
            ) {

                Box(
                    modifier =
                        Modifier
                            .size(54.dp),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Text(
                        text = shortName,
                        fontSize = 10.sp,
                        fontWeight =
                            FontWeight.ExtraBold,
                        color = GymAccent
                    )
                }
            }

            Spacer(
                Modifier.width(15.dp)
            )

            // ==============================
            // TÊN NHÓM CƠ
            // ==============================

            Column(
                modifier =
                    Modifier.weight(1f)
            ) {

                Text(
                    text = name,
                    fontSize = 18.sp,
                    fontWeight =
                        FontWeight.ExtraBold,
                    color = GymWhite
                )

                Spacer(
                    Modifier.height(3.dp)
                )

                Text(
                    text =
                        "Xem các bài tập $name",

                    fontSize = 12.sp,
                    color = GymGray
                )
            }

            // ==============================
            // MŨI TÊN
            // ==============================

            Text(
                text = "→",
                fontSize = 22.sp,
                fontWeight =
                    FontWeight.Bold,
                color = GymAccent
            )
        }
    }
}