package com.zenith26.fest.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zenith26.fest.ui.theme.*

data class CategoryStyle(
    val textColor: Color,
    val backgroundColor: Color,
    val borderColor: Color,
    val icon: String
)

fun getCategoryStyle(category: String): CategoryStyle {
    return when (category.trim().lowercase()) {
        "technical" -> CategoryStyle(
            textColor = Sky300,
            backgroundColor = Sky500.copy(alpha = 0.15f),
            borderColor = Sky500.copy(alpha = 0.30f),
            icon = "💻"
        )
        "cultural" -> CategoryStyle(
            textColor = Fuchsia300,
            backgroundColor = Fuchsia500.copy(alpha = 0.15f),
            borderColor = Fuchsia500.copy(alpha = 0.30f),
            icon = "🎭"
        )
        "sports" -> CategoryStyle(
            textColor = Emerald300,
            backgroundColor = Emerald500.copy(alpha = 0.15f),
            borderColor = Emerald500.copy(alpha = 0.30f),
            icon = "⚽"
        )
        "gaming" -> CategoryStyle(
            textColor = Amber300,
            backgroundColor = Amber500.copy(alpha = 0.15f),
            borderColor = Amber500.copy(alpha = 0.30f),
            icon = "🎮"
        )
        "workshop" -> CategoryStyle(
            textColor = Violet300,
            backgroundColor = Violet500.copy(alpha = 0.15f),
            borderColor = Violet500.copy(alpha = 0.30f),
            icon = "🛠️"
        )
        else -> CategoryStyle(
            textColor = Slate300,
            backgroundColor = Slate700.copy(alpha = 0.40f),
            borderColor = Slate600,
            icon = "✨"
        )
    }
}

@Composable
fun CategoryBadge(
    category: String,
    modifier: Modifier = Modifier,
    showIcon: Boolean = false
) {
    val style = getCategoryStyle(category)
    Box(
        modifier = modifier
            .background(style.backgroundColor, RoundedCornerShape(50))
            .border(1.dp, style.borderColor, RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        val label = if (showIcon) "${style.icon} $category" else category
        Text(
            text = label,
            color = style.textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
