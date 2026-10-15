package com.zenith26.fest.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zenith26.fest.ui.theme.*

@Composable
fun SeatBadge(
    availableSeats: Int,
    capacity: Int,
    modifier: Modifier = Modifier
) {
    val isFull = availableSeats <= 0

    if (isFull) {
        Box(
            modifier = modifier
                .background(Rose500.copy(alpha = 0.20f), RoundedCornerShape(50))
                .border(1.dp, Rose500.copy(alpha = 0.40f), RoundedCornerShape(50))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = "🚫 FULL",
                color = Rose300,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    } else {
        Box(
            modifier = modifier
                .background(Emerald500.copy(alpha = 0.15f), RoundedCornerShape(50))
                .border(1.dp, Emerald500.copy(alpha = 0.30f), RoundedCornerShape(50))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = "💺 $availableSeats / $capacity seats left",
                color = Emerald300,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
