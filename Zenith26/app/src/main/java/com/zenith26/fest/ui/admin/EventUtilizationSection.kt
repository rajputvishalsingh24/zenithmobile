package com.zenith26.fest.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zenith26.fest.data.model.Event
import com.zenith26.fest.ui.components.CategoryBadge
import com.zenith26.fest.ui.theme.*

data class EnrichedEvent(
    val event: Event,
    val registered: Int,
    val utilizationPct: Double,
    val isOver90: Boolean,
    val isFull: Boolean
)

@Composable
fun EventUtilizationSection(
    events: List<Event>,
    onFilterByEvent: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val enrichedEvents = events.map { ev ->
        val registered = (ev.capacity - ev.availableSeats).coerceAtLeast(0)
        val pct = if (ev.capacity > 0) (registered.toDouble() / ev.capacity.toDouble()) * 100.0 else 0.0
        val isOver90 = pct > 90.0 || (ev.capacity > 0 && ev.availableSeats == 0)
        val isFull = ev.capacity > 0 && ev.availableSeats <= 0
        EnrichedEvent(
            event = ev,
            registered = registered,
            utilizationPct = pct,
            isOver90 = isOver90,
            isFull = isFull
        )
    }.sortedWith(compareByDescending<EnrichedEvent> { it.utilizationPct }.thenByDescending { it.registered })

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Slate900.copy(alpha = 0.90f), RoundedCornerShape(16.dp))
            .border(1.dp, Slate800, RoundedCornerShape(16.dp))
    ) {
        Column {
            // Header
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "All Events — Sorted by Utilization (Highest Demand First)",
                    color = PureWhite,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "Click any event's registration count to filter the student registrations list.",
                    color = Slate400,
                    fontSize = 11.sp
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Slate800)
            )

            // Horizontal Scrollable Table to preserve all columns faithfully without clipping
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                // Table Header Row
                Row(
                    modifier = Modifier
                        .background(Slate950.copy(alpha = 0.60f))
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("TITLE", color = Slate400, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(220.dp))
                    Text("CATEGORY", color = Slate400, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(110.dp))
                    Text("DATE & TIME", color = Slate400, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(150.dp))
                    Text("VENUE", color = Slate400, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(140.dp))
                    Text("REGISTERED / CAP", color = Slate400, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(130.dp))
                    Text("UTILIZATION %", color = Slate400, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(120.dp))
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Slate800)
                )

                // Table Rows
                if (enrichedEvents.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No events found in the database.", color = Slate400, fontSize = 13.sp)
                    }
                } else {
                    enrichedEvents.forEach { item ->
                        val ev = item.event
                        val rowBg = if (item.isOver90) Amber500.copy(alpha = 0.10f) else Color.Transparent

                        Row(
                            modifier = Modifier
                                .background(rowBg)
                                .border(
                                    width = if (item.isOver90) 3.dp else 0.dp,
                                    color = if (item.isOver90) Amber400 else Color.Transparent,
                                    shape = RoundedCornerShape(0.dp)
                                )
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // 1. Title Column
                            Row(
                                modifier = Modifier.width(220.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = ev.icon ?: "✨", fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = ev.title,
                                        color = PureWhite,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    if (item.isFull) {
                                        Text(
                                            text = "FULL",
                                            color = Rose300,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    } else if (item.isOver90) {
                                        Text(
                                            text = "⚠️ >90% High Demand",
                                            color = Amber300,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            // 2. Category
                            Box(modifier = Modifier.width(110.dp)) {
                                CategoryBadge(category = ev.category)
                            }

                            // 3. Date & Time
                            Text(
                                text = "${ev.eventDate} • ${ev.eventTime}",
                                color = Slate300,
                                fontSize = 12.sp,
                                modifier = Modifier.width(150.dp)
                            )

                            // 4. Venue
                            Text(
                                text = ev.venue,
                                color = Slate300,
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.width(140.dp)
                            )

                            // 5. Registered / Capacity (clickable to filter registrations)
                            Text(
                                text = "${item.registered} / ${ev.capacity}",
                                color = Violet300,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                textDecoration = TextDecoration.Underline,
                                modifier = Modifier
                                    .width(130.dp)
                                    .clickable { onFilterByEvent(ev.id) }
                            )

                            // 6. Utilization % with Bar
                            Row(
                                modifier = Modifier.width(120.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(6.dp)
                                        .background(Slate800, RoundedCornerShape(50))
                                ) {
                                    val barColor = when {
                                        item.isFull -> Rose500
                                        item.isOver90 -> Amber400
                                        item.utilizationPct >= 50.0 -> Violet500
                                        else -> Emerald500
                                    }
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth((item.utilizationPct.toFloat() / 100f).coerceIn(0f, 1f))
                                            .height(6.dp)
                                            .background(barColor, RoundedCornerShape(50))
                                    )
                                }

                                Text(
                                    text = String.format("%.1f%%", item.utilizationPct),
                                    color = if (item.isOver90) Amber300 else Slate100,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Slate800.copy(alpha = 0.60f))
                        )
                    }
                }
            }
        }
    }
}
