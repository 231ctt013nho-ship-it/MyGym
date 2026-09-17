package com.example.mygym.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.mygym.data.Exercise
import com.example.mygym.ui.theme.GymAccent
import com.example.mygym.ui.theme.GymCard
import com.example.mygym.ui.theme.GymDivider
import com.example.mygym.ui.theme.GymGray
import com.example.mygym.ui.theme.GymImageBackground
import com.example.mygym.ui.theme.GymWhite


// =====================================================
// HERO CARD
// =====================================================

@Composable
fun DashboardHeroCard(
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(175.dp)
            .clickable {
                onClick()
            },

        shape = RoundedCornerShape(24.dp),

        colors = CardDefaults.cardColors(
            containerColor = GymAccent
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(22.dp),

            verticalArrangement =
                Arrangement.SpaceBetween
        ) {

            Column {

                Text(
                    text = "TẬP LUYỆN THÔNG MINH",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.5.sp,
                    color = Color(0xFF111521)
                )

                Spacer(
                    Modifier.height(6.dp)
                )

                Text(
                    text = "Khám phá bài tập",
                    fontSize = 27.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF080B12)
                )

                Text(
                    text = "Khám phá thư viện bài tập",
                    fontSize = 13.sp,
                    color = Color(0xFF30394D)
                )
            }

            Text(
                text = "XEM THƯ VIỆN  →",
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp,
                color = Color(0xFF080B12)
            )
        }
    }
}


// =====================================================
// SMALL DASHBOARD CARD
// =====================================================

@Composable
fun DashboardSmallCard(
    title: String,
    subtitle: String,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(110.dp)
            .clickable {
                onClick()
            },

        shape = RoundedCornerShape(19.dp),

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
                .fillMaxSize()
                .padding(16.dp),

            verticalArrangement =
                Arrangement.SpaceBetween
        ) {

            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                color = GymWhite
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = GymGray,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "→",
                    fontSize = 20.sp,
                    color = GymAccent
                )
            }
        }
    }
}


// =====================================================
// SECTION TITLE
// =====================================================

@Composable
fun SectionTitle(
    title: String,
    subtitle: String
) {
    Column {

        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.sp,
            color = GymWhite
        )

        Spacer(
            Modifier.height(3.dp)
        )

        Text(
            text = subtitle,
            fontSize = 12.sp,
            color = GymGray
        )
    }
}


// =====================================================
// EXERCISE PREVIEW
// =====================================================

@Composable
fun ExercisePreviewCard(
    exercise: Exercise,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clickable {
                onClick()
            },

        shape = RoundedCornerShape(20.dp),

        colors = CardDefaults.cardColors(
            containerColor = GymCard
        )
    ) {

        Row(
            modifier = Modifier.fillMaxSize()
        ) {

            Image(
                painter = painterResource(
                    id = exercise.imageRes
                ),

                contentDescription = exercise.name,

                modifier = Modifier
                    .width(145.dp)
                    .fillMaxSize()
                    .background(
                        GymImageBackground
                    ),

                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(15.dp),

                verticalArrangement =
                    Arrangement.Center
            ) {

                Text(
                    text = exercise.muscle.uppercase(),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    color = GymAccent
                )

                Spacer(
                    Modifier.height(5.dp)
                )

                Text(
                    text = exercise.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = GymWhite,
                    maxLines = 2
                )

                Spacer(
                    Modifier.height(7.dp)
                )

                Text(
                    text = "XEM CHI TIẾT  →",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp,
                    color = GymGray
                )
            }
        }
    }
}