package com.zenith26.fest.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zenith26.fest.ui.components.getCategoryStyle
import com.zenith26.fest.ui.theme.*

fun getCategoryBarColor(category: String): Color {
    return when (category.lowercase()) {
        "technical" -> Sky500
        "cultural" -> Fuchsia500
        "sports" -> Emerald500
        "gaming" -> Amber500
        "workshop" -> Violet500
        else -> Violet500
    }
}

@Composable
fun CategoryBreakdownSection(
    eventsByCategory: Map<String, Int>,
    totalEvents: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Slate900.copy(alpha = 0.90f), RoundedCornerShape(16.dp))
            .border(1.dp, Slate800, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "Events Breakdown by Category",
                color = PureWhite,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "Live count of events per track from GET /api/stats",
                color = Slate400,
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (eventsByCategory.isEmpty()) {
                Text(
                    text = "No category statistics available.",
                    color = Slate400,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    eventsByCategory.forEach { (category, count) ->
                        val style = getCategoryStyle(category)
                        val barColor = getCategoryBarColor(category)
                        val pct = if (totalEvents > 0) ((count.toFloat() / totalEvents) * 100).toInt() else 0

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Slate950.copy(alpha = 0.70f), RoundedCornerShape(12.dp))
                                .border(1.dp, Slate800, RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .background(style.backgroundColor, RoundedCornerShape(50))
                                            .border(1.dp, style.borderColor, RoundedCornerShape(50))
                                            .padding(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "${style.icon} $category",
                                            color = style.textColor,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Text(
                                        text = "$count",
                                        color = PureWhite,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Progress bar
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .background(Slate800, RoundedCornerShape(50))
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth((pct / 100f).coerceIn(0f, 1f))
                                            .height(6.dp)
                                            .background(barColor, RoundedCornerShape(50))
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "$pct% of fest schedule",
                                    color = Slate400,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
