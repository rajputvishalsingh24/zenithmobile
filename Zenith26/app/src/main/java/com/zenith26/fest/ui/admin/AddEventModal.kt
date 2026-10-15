package com.zenith26.fest.ui.admin

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.zenith26.fest.data.model.EventCreateRequest
import com.zenith26.fest.ui.theme.*

private val CATEGORY_OPTIONS = listOf(
    "Technical",
    "Cultural",
    "Sports",
    "Gaming",
    "Workshop"
)

@Composable
fun AddEventModal(
    isSubmitting: Boolean,
    errorMessage: String?,
    onDismiss: () -> Unit,
    onSubmit: (EventCreateRequest) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var icon by remember { mutableStateOf("🚀") }
    var category by remember { mutableStateOf("Technical") }
    var venue by remember { mutableStateOf("") }
    var eventDate by remember { mutableStateOf("2026-11-15") }
    var eventTime by remember { mutableStateOf("03:30 PM") }
    var capacityStr by remember { mutableStateOf("60") }
    var feeStr by remember { mutableStateOf("0") }
    var description by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }
    var categoryExpanded by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .background(Slate900, RoundedCornerShape(20.dp))
                .border(1.dp, Slate700, RoundedCornerShape(20.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Slate950.copy(alpha = 0.80f))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "＋ Create New Fest Event",
                            color = PureWhite,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(Slate800, RoundedCornerShape(8.dp))
                                .clickable { onDismiss() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("✕", color = Slate300, fontSize = 14.sp)
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Error message
                    val displayErr = localError ?: errorMessage
                    if (!displayErr.isNullOrBlank()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Rose950.copy(alpha = 0.70f), RoundedCornerShape(10.dp))
                                .border(1.dp, Rose500.copy(alpha = 0.50f), RoundedCornerShape(10.dp))
                                .padding(10.dp)
                        ) {
                            Text(text = displayErr, color = Rose200, fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    // Title & Icon
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Column(modifier = Modifier.weight(3f)) {
                            Text("Event Title *", color = Slate300, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = title,
                                onValueChange = { title = it; localError = null },
                                placeholder = { Text("Quantum Computing Demystified", color = Slate500, fontSize = 12.sp) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = PureWhite,
                                    unfocusedTextColor = Slate100,
                                    focusedContainerColor = Slate950,
                                    unfocusedContainerColor = Slate950
                                )
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text("Icon", color = Slate300, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = icon,
                                onValueChange = { icon = it },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = PureWhite,
                                    unfocusedTextColor = Slate100,
                                    focusedContainerColor = Slate950,
                                    unfocusedContainerColor = Slate950
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Category & Venue
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Category *", color = Slate300, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Box {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Slate950, RoundedCornerShape(10.dp))
                                        .border(1.dp, Slate700, RoundedCornerShape(10.dp))
                                        .clickable { categoryExpanded = true }
                                        .padding(horizontal = 12.dp, vertical = 14.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(category, color = PureWhite, fontSize = 12.sp)
                                        Text("▼", color = Slate400, fontSize = 10.sp)
                                    }
                                }
                                DropdownMenu(
                                    expanded = categoryExpanded,
                                    onDismissRequest = { categoryExpanded = false },
                                    modifier = Modifier.background(Slate900)
                                ) {
                                    CATEGORY_OPTIONS.forEach { opt ->
                                        DropdownMenuItem(
                                            text = { Text(opt, color = PureWhite) },
                                            onClick = {
                                                category = opt
                                                categoryExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text("Venue *", color = Slate300, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = venue,
                                onValueChange = { venue = it; localError = null },
                                placeholder = { Text("Seminar Hall 2", color = Slate500, fontSize = 12.sp) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = PureWhite,
                                    unfocusedTextColor = Slate100,
                                    focusedContainerColor = Slate950,
                                    unfocusedContainerColor = Slate950
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Date & Time
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Event Date (YYYY-MM-DD) *", color = Slate300, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = eventDate,
                                onValueChange = { eventDate = it; localError = null },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = PureWhite,
                                    unfocusedTextColor = Slate100,
                                    focusedContainerColor = Slate950,
                                    unfocusedContainerColor = Slate950
                                )
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text("Event Time *", color = Slate300, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = eventTime,
                                onValueChange = { eventTime = it; localError = null },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = PureWhite,
                                    unfocusedTextColor = Slate100,
                                    focusedContainerColor = Slate950,
                                    unfocusedContainerColor = Slate950
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Capacity & Fee
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Seat Capacity (>0) *", color = Slate300, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = capacityStr,
                                onValueChange = { capacityStr = it; localError = null },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = PureWhite,
                                    unfocusedTextColor = Slate100,
                                    focusedContainerColor = Slate950,
                                    unfocusedContainerColor = Slate950
                                )
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text("Entry Fee (₹, 0 for Free)", color = Slate300, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = feeStr,
                                onValueChange = { feeStr = it },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = PureWhite,
                                    unfocusedTextColor = Slate100,
                                    focusedContainerColor = Slate950,
                                    unfocusedContainerColor = Slate950
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Description
                    Text("Description", color = Slate300, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        placeholder = { Text("Describe what participants will build, experience, or compete in...", color = Slate500, fontSize = 12.sp) },
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = PureWhite,
                            unfocusedTextColor = Slate100,
                            focusedContainerColor = Slate950,
                            unfocusedContainerColor = Slate950
                        )
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Slate300)
                        ) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(
                            onClick = {
                                val t = title.trim()
                                val v = venue.trim()
                                val d = eventDate.trim()
                                val tm = eventTime.trim()
                                val cap = capacityStr.trim().toIntOrNull()
                                val fee = feeStr.trim().toIntOrNull() ?: 0

                                if (t.isEmpty() || v.isEmpty() || d.isEmpty() || tm.isEmpty() || cap == null || cap <= 0) {
                                    localError = "Please fill in all required fields and provide a seat capacity > 0."
                                    return@Button
                                }

                                onSubmit(
                                    EventCreateRequest(
                                        title = t,
                                        category = category,
                                        description = description.trim(),
                                        eventDate = d,
                                        eventTime = tm,
                                        venue = v,
                                        capacity = cap,
                                        fee = fee,
                                        icon = icon.trim().ifEmpty { "✨" }
                                    )
                                )
                            },
                            enabled = !isSubmitting,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Violet600,
                                contentColor = PureWhite
                            )
                        ) {
                            if (isSubmitting) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), color = PureWhite, strokeWidth = 2.dp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Creating...")
                            } else {
                                Text("Create Event", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
