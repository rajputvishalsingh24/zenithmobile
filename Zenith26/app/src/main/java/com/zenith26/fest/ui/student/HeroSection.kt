package com.zenith26.fest.ui.student

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zenith26.fest.ui.theme.*

@Composable
fun HeroSection(
    onExploreClicked: () -> Unit = {},
    onAdminStatsClicked: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Violet900.copy(alpha = 0.40f),
                        Slate950,
                        Slate950
                    )
                )
            )
            .border(1.dp, Slate800.copy(alpha = 0.80f), RoundedCornerShape(0.dp))
            .padding(horizontal = 16.dp, vertical = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Event Dates Pill
            Box(
                modifier = Modifier
                    .background(Violet500.copy(alpha = 0.10f), RoundedCornerShape(50))
                    .border(1.dp, Violet500.copy(alpha = 0.30f), RoundedCornerShape(50))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "🗓️ November 14–15, 2026 • Main Campus Arena",
                    color = Violet300,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Main Hero Headline
            Text(
                text = "Where Code, Culture & Adrenaline Collide at Zenith '26",
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = PureWhite,
                textAlign = TextAlign.Center,
                lineHeight = 32.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Subtitle
            Text(
                text = "48 hours of hackathons, robo-battles, concert nights, LAN esports, and hands-on creator workshops. Claim your spot before seats run out!",
                fontSize = 13.sp,
                color = Slate300,
                textAlign = TextAlign.Center,
                lineHeight = 19.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Action Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onExploreClicked,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Violet600,
                        contentColor = PureWhite
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Explore 14+ Fest Events ↓",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = onAdminStatsClicked,
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Slate900.copy(alpha = 0.90f),
                        contentColor = Slate200
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Live Fest Stats →",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
