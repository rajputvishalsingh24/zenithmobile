package com.zenith26.fest.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.zenith26.fest.data.remote.ApiClient
import com.zenith26.fest.ui.theme.*

@Composable
fun BaseUrlDialog(
    currentUrl: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var urlText by remember { mutableStateOf(currentUrl) }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Slate900, RoundedCornerShape(16.dp))
                .border(1.dp, Slate800, RoundedCornerShape(16.dp))
                .padding(20.dp)
        ) {
            Text(
                text = "⚙️ Configure Backend API URL",
                color = PureWhite,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Set the address of your running FastAPI server:",
                color = Slate400,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Quick preset buttons
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                OutlinedButton(
                    onClick = { urlText = ApiClient.DEFAULT_TUNNEL_BASE_URL },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Slate300),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("🌐 Cloud Tunnel (Appetize / Internet)", fontSize = 11.sp)
                }
                OutlinedButton(
                    onClick = { urlText = ApiClient.DEFAULT_WIFI_BASE_URL },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Slate300),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("📶 Local Wi-Fi (172.16.54.116)", fontSize = 11.sp)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { urlText = ApiClient.DEFAULT_EMULATOR_BASE_URL },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Slate300),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Emulator", fontSize = 11.sp)
                    }
                    OutlinedButton(
                        onClick = { urlText = ApiClient.DEFAULT_LOCALHOST_BASE_URL },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Slate300),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Localhost", fontSize = 11.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = urlText,
                onValueChange = { urlText = it },
                label = { Text("Base URL", color = Slate400, fontSize = 12.sp) },
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Slate300)
                ) {
                    Text("Cancel", fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    onClick = { onSave(urlText) },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Violet600,
                        contentColor = PureWhite
                    )
                ) {
                    Text("Apply & Refresh", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
