package com.zenith26.fest.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zenith26.fest.ui.theme.*

@Composable
fun TopNavHeader(
    isAdmin: Boolean = false,
    onNavigateToAdmin: () -> Unit = {},
    onNavigateToStudent: () -> Unit = {},
    onRefresh: () -> Unit = {},
    onOpenAddEvent: () -> Unit = {},
    onOpenSettings: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Slate950.copy(alpha = 0.90f))
            .border(1.dp, Slate800.copy(alpha = 0.80f), RoundedCornerShape(0.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Brand Logo & Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    if (isAdmin) onNavigateToStudent()
                }
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            brush = Brush.linearGradient(
                                listOf(Violet500, Fuchsia500, Amber500)
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "⚡", fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Zenith '26",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = PureWhite
                        )
                        if (!isAdmin) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .background(Violet500.copy(alpha = 0.15f), RoundedCornerShape(50))
                                    .border(1.dp, Violet500.copy(alpha = 0.30f), RoundedCornerShape(50))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "Nov 14–15",
                                    color = Violet300,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                    if (isAdmin) {
                        Text(
                            text = "Coordinator Dashboard",
                            color = Violet300,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Right-side actions
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Server Config Icon Button
                Box(
                    modifier = Modifier
                        .background(Slate800, RoundedCornerShape(10.dp))
                        .border(1.dp, Slate700, RoundedCornerShape(10.dp))
                        .clickable { onOpenSettings() }
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "⚙️", fontSize = 13.sp)
                }

                if (!isAdmin) {
                    // Button to Admin Dashboard
                    Box(
                        modifier = Modifier
                            .background(Slate800, RoundedCornerShape(10.dp))
                            .border(1.dp, Slate700, RoundedCornerShape(10.dp))
                            .clickable { onNavigateToAdmin() }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "📊", fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Admin",
                                color = Slate100,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                } else {
                    // Admin controls: Refresh & Add Event & Student Portal link
                    Box(
                        modifier = Modifier
                            .background(Slate800, RoundedCornerShape(10.dp))
                            .border(1.dp, Slate700, RoundedCornerShape(10.dp))
                            .clickable { onRefresh() }
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Text(text = "🔄", fontSize = 12.sp)
                    }

                    Box(
                        modifier = Modifier
                            .background(Violet600, RoundedCornerShape(10.dp))
                            .clickable { onOpenAddEvent() }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "＋ Add",
                            color = PureWhite,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(Slate900, RoundedCornerShape(10.dp))
                            .border(1.dp, Slate700, RoundedCornerShape(10.dp))
                            .clickable { onNavigateToStudent() }
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "← Portal",
                            color = Slate300,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
