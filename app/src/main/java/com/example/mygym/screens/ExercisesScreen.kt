package com.example.mygym.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items as lazyItems
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mygym.data.Exercise
import com.example.mygym.data.exercises
import com.example.mygym.data.muscleGroups
import com.example.mygym.ui.theme.GymAccent
import com.example.mygym.ui.theme.GymAccentLight
import com.example.mygym.ui.theme.GymBackground
import com.example.mygym.ui.theme.GymCard
import com.example.mygym.ui.theme.GymDivider
import com.example.mygym.ui.theme.GymGray
import com.example.mygym.ui.theme.GymImageBackground
import com.example.mygym.ui.theme.GymWhite

// ============================================================
// MÀN HÌNH DANH SÁCH BÀI TẬP
// ============================================================

@Composable
fun ExerciseListScreen(
    title: String,
    exerciseList: List<Exercise>,
    selectedMuscle: String,
    onMuscleSelected: (String) -> Unit,
    onBackClick: () -> Unit,
    onExerciseClick: (Exercise) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 14.dp,
                vertical = 18.dp
            ),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // ====================================================
        // PHẦN TIÊU ĐỀ + BỘ LỌC NHÓM CƠ
        // ====================================================

        item(
            span = {
                GridItemSpan(maxLineSpan)
            }
        ) {
            Column {

                // -------------------------------
                // Tiêu đề
                // -------------------------------

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "‹",
                        fontSize = 38.sp,
                        color = GymWhite,
                        modifier = Modifier.clickable {
                            onBackClick()
                        }
                    )

                    Spacer(
                        modifier = Modifier.width(7.dp)
                    )

                    Column {

                        Text(
                            text = title,
                            fontSize = 27.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = GymWhite
                        )

                        Text(
                            text = "${exerciseList.size} BÀI TẬP",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.3.sp,
                            color = GymGray
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(15.dp)
                )

                // -------------------------------
                // Bộ lọc nhóm cơ
                // -------------------------------

                MuscleFilterRow(
                    selected = selectedMuscle,
                    onSelected = onMuscleSelected
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )
            }
        }

        // ====================================================
        // DANH SÁCH BÀI TẬP
        // ====================================================

        gridItems(
            items = exerciseList
        ) { exercise ->

            ModernExerciseCard(
                exercise = exercise,
                onClick = {
                    onExerciseClick(exercise)
                }
            )
        }
    }
}


// ============================================================
// THANH LỌC NHÓM CƠ
// ============================================================

@Composable
fun MuscleFilterRow(
    selected: String,
    onSelected: (String) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        lazyItems(
            items = muscleGroups
        ) { group ->

            val isSelected = selected == group.name

            Card(
                modifier = Modifier
                    .height(46.dp)
                    .clickable {
                        onSelected(group.name)
                    },

                shape = RoundedCornerShape(13.dp),

                colors = CardDefaults.cardColors(
                    containerColor =
                        if (isSelected) {
                            GymAccent
                        } else {
                            GymCard
                        }
                ),

                border =
                    if (isSelected) {
                        null
                    } else {
                        BorderStroke(
                            1.dp,
                            GymDivider
                        )
                    }
            ) {

                Box(
                    modifier = Modifier
                        .padding(horizontal = 14.dp),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = group.shortName,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.7.sp,

                        color =
                            if (isSelected) {
                                Color(0xFF090C14)
                            } else {
                                GymGray
                            }
                    )
                }
            }
        }
    }
}


// ============================================================
// CARD BÀI TẬP
// ============================================================

@Composable
fun ModernExerciseCard(
    exercise: Exercise,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },

        shape = RoundedCornerShape(18.dp),

        colors = CardDefaults.cardColors(
            containerColor = GymCard
        )
    ) {

        Column {

            // =================================================
            // HÌNH ẢNH
            // =================================================

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.82f)
                    .background(GymImageBackground)
            ) {

                Image(
                    painter = painterResource(
                        id = exercise.imageRes
                    ),

                    contentDescription = exercise.name,

                    modifier = Modifier.fillMaxSize(),

                    contentScale = ContentScale.Crop
                )

                // ---------------------------------------------
                // Nhãn nhóm cơ
                // ---------------------------------------------

                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(9.dp)
                        .clip(
                            RoundedCornerShape(8.dp)
                        )
                        .background(
                            GymBackground.copy(
                                alpha = 0.82f
                            )
                        )
                        .padding(
                            horizontal = 8.dp,
                            vertical = 5.dp
                        )
                ) {

                    Text(
                        text = exercise.muscle.uppercase(),
                        fontSize = 8.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.7.sp,
                        color = GymAccentLight
                    )
                }

                // ---------------------------------------------
                // Nút lưu
                // ---------------------------------------------

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(9.dp)
                        .size(30.dp)
                        .clip(
                            RoundedCornerShape(9.dp)
                        )
                        .background(
                            GymBackground.copy(
                                alpha = 0.82f
                            )
                        ),

                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "LƯU",
                        fontSize = 7.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = GymWhite
                    )
                }
            }

            // =================================================
            // THÔNG TIN BÀI TẬP
            // =================================================

            Column(
                modifier = Modifier.padding(
                    horizontal = 12.dp,
                    vertical = 11.dp
                )
            ) {

                Text(
                    text = exercise.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = GymWhite,
                    maxLines = 2
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "XEM",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = GymAccent
                    )

                    Spacer(
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = "→",
                        fontSize = 17.sp,
                        color = GymAccent
                    )
                }
            }
        }
    }
}