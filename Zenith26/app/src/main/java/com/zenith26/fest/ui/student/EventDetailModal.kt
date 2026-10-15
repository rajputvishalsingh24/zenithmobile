package com.zenith26.fest.ui.student

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
import com.zenith26.fest.data.model.Event
import com.zenith26.fest.data.model.RegistrationRequest
import com.zenith26.fest.ui.components.CategoryBadge
import com.zenith26.fest.ui.theme.*

@Composable
fun EventDetailModal(
    event: Event,
    isSubmitting: Boolean,
    errorMessage: String?,
    successMessage: String?,
    onDismiss: () -> Unit,
    onSubmitRegistration: (RegistrationRequest) -> Unit
) {
    var studentName by remember { mutableStateOf("") }
    var rollNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var clientValidationError by remember { mutableStateOf<String?>(null) }

    val isFull = event.availableSeats <= 0

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .background(Slate900, RoundedCornerShape(20.dp))
                .border(1.dp, Slate700.copy(alpha = 0.80f), RoundedCornerShape(20.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Top Header of Modal
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Slate900.copy(alpha = 0.95f))
                        .border(1.dp, Slate800, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(Violet500.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
                                    .border(1.dp, Violet500.copy(alpha = 0.30f), RoundedCornerShape(16.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = event.icon ?: "✨", fontSize = 24.sp)
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    CategoryBadge(category = event.category)

                                    if (isFull) {
                                        Box(
                                            modifier = Modifier
                                                .background(Rose500.copy(alpha = 0.20f), RoundedCornerShape(50))
                                                .border(1.dp, Rose500.copy(alpha = 0.40f), RoundedCornerShape(50))
                                                .padding(horizontal = 8.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = "🚫 FULL (0 seats left)",
                                                color = Rose300,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.ExtraBold
                                            )
                                        }
                                    } else {
                                        Box(
                                            modifier = Modifier
                                                .background(Emerald500.copy(alpha = 0.15f), RoundedCornerShape(50))
                                                .border(1.dp, Emerald500.copy(alpha = 0.30f), RoundedCornerShape(50))
                                                .padding(horizontal = 8.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = "💺 ${event.availableSeats} of ${event.capacity} seats open",
                                                color = Emerald300,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = event.title,
                                    color = PureWhite,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    lineHeight = 23.sp
                                )
                            }
                        }

                        // Close button (✕)
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .background(Slate800, RoundedCornerShape(10.dp))
                                .clickable { onDismiss() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "✕", color = Slate300, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Modal Content Body
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    // Full Description
                    Text(
                        text = event.description ?: "No description provided.",
                        color = Slate300,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 4-item Metadata Grid
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Slate950.copy(alpha = 0.70f), RoundedCornerShape(14.dp))
                            .border(1.dp, Slate800, RoundedCornerShape(14.dp))
                            .padding(14.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("📅 Date & Time", color = Slate400, fontSize = 11.sp)
                                    Text(
                                        "${event.eventDate} • ${event.eventTime}",
                                        color = PureWhite,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("📍 Venue", color = Slate400, fontSize = 11.sp)
                                    Text(
                                        event.venue,
                                        color = PureWhite,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("🎟️ Entry Fee", color = Slate400, fontSize = 11.sp)
                                    Text(
                                        if (event.fee == 0) "Free" else "₹${event.fee}",
                                        color = PureWhite,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("💺 Availability", color = Slate400, fontSize = 11.sp)
                                    Text(
                                        "${event.availableSeats} / ${event.capacity} available",
                                        color = PureWhite,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Divider before registration form
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Slate800)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Registration Heading
                    Text(
                        text = "Student Registration",
                        color = PureWhite,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Enter your college details below to reserve your seat.",
                        color = Slate400,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Inline Error Alert (Client validation or Backend error)
                    val displayError = clientValidationError ?: errorMessage
                    if (!displayError.isNullOrBlank()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Rose950.copy(alpha = 0.70f), RoundedCornerShape(12.dp))
                                .border(1.dp, Rose500.copy(alpha = 0.50f), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "⚠️", fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = displayError,
                                    color = Rose200,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Inline Success Alert
                    if (!successMessage.isNullOrBlank()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Emerald950.copy(alpha = 0.70f), RoundedCornerShape(12.dp))
                                .border(1.dp, Emerald500.copy(alpha = 0.50f), RoundedCornerShape(12.dp))
                                .padding(14.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "🎉", fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = successMessage,
                                    color = Emerald200,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Input Form Fields
                    // 1. Student Name
                    Text(
                        text = "Student Name *",
                        color = Slate300,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = studentName,
                        onValueChange = {
                            studentName = it
                            clientValidationError = null
                        },
                        placeholder = { Text("Aarav Sharma", color = Slate500, fontSize = 13.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = PureWhite,
                            unfocusedTextColor = Slate100,
                            focusedContainerColor = Slate950,
                            unfocusedContainerColor = Slate950,
                            focusedBorderColor = Violet600,
                            unfocusedBorderColor = Slate700
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // 2. Roll Number
                    Text(
                        text = "Roll Number *",
                        color = Slate300,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = rollNumber,
                        onValueChange = {
                            rollNumber = it
                            clientValidationError = null
                        },
                        placeholder = { Text("2026CS1042", color = Slate500, fontSize = 13.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = PureWhite,
                            unfocusedTextColor = Slate100,
                            focusedContainerColor = Slate950,
                            unfocusedContainerColor = Slate950,
                            focusedBorderColor = Violet600,
                            unfocusedBorderColor = Slate700
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // 3. College Email
                    Text(
                        text = "College Email *",
                        color = Slate300,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            clientValidationError = null
                        },
                        placeholder = { Text("aarav@college.edu", color = Slate500, fontSize = 13.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = PureWhite,
                            unfocusedTextColor = Slate100,
                            focusedContainerColor = Slate950,
                            unfocusedContainerColor = Slate950,
                            focusedBorderColor = Violet600,
                            unfocusedBorderColor = Slate700
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // 4. Phone Number (optional)
                    Text(
                        text = "Phone Number (optional)",
                        color = Slate400,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        placeholder = { Text("+91 98765 43210", color = Slate500, fontSize = 13.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = PureWhite,
                            unfocusedTextColor = Slate100,
                            focusedContainerColor = Slate950,
                            unfocusedContainerColor = Slate950,
                            focusedBorderColor = Violet600,
                            unfocusedBorderColor = Slate700
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Actions Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Slate800,
                                contentColor = Slate300
                            )
                        ) {
                            Text("Cancel", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Button(
                            onClick = {
                                val sName = studentName.trim()
                                val rNum = rollNumber.trim()
                                val em = email.trim()

                                // Client-side validation (matching web frontend/js/app.js)
                                if (sName.isEmpty() || rNum.isEmpty() || em.isEmpty()) {
                                    clientValidationError = "Please fill in all required fields: Student Name, Roll Number, and Email."
                                    return@Button
                                }
                                if (!em.contains("@") || em.startsWith("@") || em.endsWith("@")) {
                                    clientValidationError = "Please enter a valid email address containing '@'."
                                    return@Button
                                }

                                clientValidationError = null
                                onSubmitRegistration(
                                    RegistrationRequest(
                                        studentName = sName,
                                        rollNumber = rNum,
                                        email = em,
                                        phone = phone.trim().ifEmpty { null }
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
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = PureWhite,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Registering...", fontSize = 13.sp)
                            } else {
                                Text(
                                    text = if (isFull) "Event Full (Try Register)" else "Confirm Registration",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
