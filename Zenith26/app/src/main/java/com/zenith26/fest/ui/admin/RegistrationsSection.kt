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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zenith26.fest.data.model.Event
import com.zenith26.fest.data.model.RegistrationRecord
import com.zenith26.fest.ui.theme.*

@Composable
fun RegistrationsSection(
    registrations: List<RegistrationRecord>,
    events: List<Event>,
    selectedEventId: Int?,
    onSelectEventFilter: (Int?) -> Unit,
    onCancelRegistration: (RegistrationRecord) -> Unit,
    modifier: Modifier = Modifier
) {
    var dropdownExpanded by remember { mutableStateOf(false) }
    var pendingCancelRecord by remember { mutableStateOf<RegistrationRecord?>(null) }

    val filterLabel = if (selectedEventId == null) {
        "All Events (${events.size})"
    } else {
        val matched = events.find { it.id == selectedEventId }
        matched?.let { "${it.icon ?: "✨"} ${it.title}" } ?: "Event #$selectedEventId"
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Slate900.copy(alpha = 0.90f), RoundedCornerShape(16.dp))
            .border(1.dp, Slate800, RoundedCornerShape(16.dp))
    ) {
        Column {
            // Header with Event Filter Dropdown
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Student Registrations Directory",
                    color = PureWhite,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "Live records from GET /api/registrations. Filter by event or cancel a registration.",
                    color = Slate400,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Event Filter Selector
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Filter by Event: ",
                        color = Slate300,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    Box {
                        Box(
                            modifier = Modifier
                                .background(Slate950, RoundedCornerShape(10.dp))
                                .border(1.dp, Slate700, RoundedCornerShape(10.dp))
                                .clickable { dropdownExpanded = true }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = filterLabel,
                                    color = PureWhite,
                                    fontSize = 12.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("▼", color = Slate400, fontSize = 10.sp)
                            }
                        }

                        DropdownMenu(
                            expanded = dropdownExpanded,
                            onDismissRequest = { dropdownExpanded = false },
                            modifier = Modifier.background(Slate900)
                        ) {
                            DropdownMenuItem(
                                text = { Text("All Events (${events.size})", color = PureWhite, fontSize = 12.sp) },
                                onClick = {
                                    onSelectEventFilter(null)
                                    dropdownExpanded = false
                                }
                            )
                            events.forEach { ev ->
                                DropdownMenuItem(
                                    text = { Text("${ev.icon ?: "✨"} ${ev.title}", color = Slate300, fontSize = 12.sp) },
                                    onClick = {
                                        onSelectEventFilter(ev.id)
                                        dropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Slate800)
            )

            // Horizontal Scrollable Registrations Table
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                // Table Header
                Row(
                    modifier = Modifier
                        .background(Slate950.copy(alpha = 0.60f))
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("ID", color = Slate400, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(60.dp))
                    Text("STUDENT NAME", color = Slate400, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(160.dp))
                    Text("ROLL NUMBER", color = Slate400, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(120.dp))
                    Text("EMAIL", color = Slate400, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(200.dp))
                    Text("EVENT", color = Slate400, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(180.dp))
                    Text("REGISTERED AT", color = Slate400, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(150.dp))
                    Text("ACTION", color = Slate400, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(90.dp))
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Slate800)
                )

                if (registrations.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("📭", fontSize = 28.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                "No registrations found",
                                color = PureWhite,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "No students have registered for the selected filter yet.",
                                color = Slate400,
                                fontSize = 12.sp
                            )
                        }
                    }
                } else {
                    registrations.forEach { reg ->
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "#${reg.id}",
                                color = Slate400,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp,
                                modifier = Modifier.width(60.dp)
                            )
                            Text(
                                text = reg.studentName,
                                color = PureWhite,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.width(160.dp)
                            )
                            Text(
                                text = reg.rollNumber,
                                color = Slate300,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp,
                                modifier = Modifier.width(120.dp)
                            )
                            Text(
                                text = reg.email,
                                color = Slate300,
                                fontSize = 12.sp,
                                modifier = Modifier.width(200.dp)
                            )
                            Box(modifier = Modifier.width(180.dp)) {
                                Box(
                                    modifier = Modifier
                                        .background(Violet500.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                                        .border(1.dp, Violet500.copy(alpha = 0.30f), RoundedCornerShape(8.dp))
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = reg.eventTitle,
                                        color = Violet300,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                            Text(
                                text = reg.registeredAt,
                                color = Slate400,
                                fontSize = 11.sp,
                                modifier = Modifier.width(150.dp)
                            )
                            Box(modifier = Modifier.width(90.dp)) {
                                Box(
                                    modifier = Modifier
                                        .background(Rose500.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                                        .border(1.dp, Rose500.copy(alpha = 0.30f), RoundedCornerShape(8.dp))
                                        .clickable { pendingCancelRecord = reg }
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "Cancel",
                                        color = Rose300,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
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

    // Confirmation dialog for cancelling registration
    if (pendingCancelRecord != null) {
        val target = pendingCancelRecord!!
        AlertDialog(
            onDismissRequest = { pendingCancelRecord = null },
            title = {
                Text("Cancel Registration #${target.id}?", color = PureWhite, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    "Cancel registration for ${target.studentName} in \"${target.eventTitle}\"?\n\nThis will immediately free up 1 seat.",
                    color = Slate300,
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onCancelRegistration(target)
                        pendingCancelRecord = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Rose600,
                        contentColor = PureWhite
                    )
                ) {
                    Text("Confirm Cancellation", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { pendingCancelRecord = null },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Slate300)
                ) {
                    Text("Keep Registration")
                }
            },
            containerColor = Slate900
        )
    }
}
