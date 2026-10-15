package com.zenith26.fest.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zenith26.fest.ui.theme.*

@Composable
fun LoadingStateIndicator(
    message: String = "Loading Zenith '26 events...",
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(36.dp),
            color = Violet500,
            strokeWidth = 3.dp
        )
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = message,
            color = Slate300,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ErrorStateBanner(
    title: String = "Unable to Load Events",
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Rose950.copy(alpha = 0.50f), RoundedCornerShape(16.dp))
            .border(1.dp, Rose500.copy(alpha = 0.40f), RoundedCornerShape(16.dp))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "⚠️", fontSize = 32.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                color = Rose200,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = message,
                color = Rose300.copy(alpha = 0.90f),
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Rose600,
                    contentColor = PureWhite
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Try Again", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun EmptyStateBanner(
    title: String = "No events match your search",
    subtitle: String = "Try searching with a different keyword or switching the category filter back to \"All\".",
    buttonLabel: String = "Show All Events",
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Slate900.copy(alpha = 0.60f), RoundedCornerShape(16.dp))
            .border(1.dp, Slate800, RoundedCornerShape(16.dp))
            .padding(28.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "🔎", fontSize = 36.sp)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                color = PureWhite,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = subtitle,
                color = Slate400,
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(18.dp))
            Button(
                onClick = onReset,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Violet600,
                    contentColor = PureWhite
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = buttonLabel, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            }
        }
    }
}
