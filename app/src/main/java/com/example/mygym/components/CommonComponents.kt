package com.example.mygym.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.mygym.ui.theme.GymAccent
import com.example.mygym.ui.theme.GymCard
import com.example.mygym.ui.theme.GymDivider
import com.example.mygym.ui.theme.GymGray
import com.example.mygym.ui.theme.GymWhite


// =====================================================
// DETAIL STAT CARD
// =====================================================

@Composable
fun DetailStatCard(
    label: String,
    value: String,
    modifier: Modifier
) {
    Card(
        modifier = modifier.height(85.dp),

        shape = RoundedCornerShape(17.dp),

        colors = CardDefaults.cardColors(
            containerColor = GymCard
        ),

        border = BorderStroke(
            1.dp,
            GymDivider
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),

            verticalArrangement =
                Arrangement.Center
        ) {

            Text(
                text = label,
                fontSize = 9.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp,
                color = GymGray
            )

            Spacer(
                Modifier.height(4.dp)
            )

            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = GymWhite
            )
        }
    }
}


// =====================================================
// MODERN TEXT FIELD
// =====================================================

@Composable
fun ModernTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    password: Boolean = false
) {
    OutlinedTextField(
        value = value,

        onValueChange = onValueChange,

        label = {
            Text(
                text = label
            )
        },

        modifier = Modifier.fillMaxWidth(),

        singleLine = true,

        visualTransformation =
            if (password) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },

        shape = RoundedCornerShape(14.dp),

        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = GymAccent,
            unfocusedBorderColor = GymDivider,

            focusedLabelColor = GymAccent,
            unfocusedLabelColor = GymGray,

            focusedTextColor = GymWhite,
            unfocusedTextColor = GymWhite,

            cursorColor = GymAccent
        )
    )
}


// =====================================================
// DARK CARD
// =====================================================

@Composable
fun DarkCard(
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(23.dp),

        colors = CardDefaults.cardColors(
            containerColor = GymCard
        ),

        border = BorderStroke(
            1.dp,
            GymDivider
        )
    ) {

        Column(
            modifier = Modifier.padding(21.dp)
        ) {

            content()
        }
    }
}