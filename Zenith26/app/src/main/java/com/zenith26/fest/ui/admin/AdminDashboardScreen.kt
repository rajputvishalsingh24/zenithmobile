package com.zenith26.fest.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zenith26.fest.data.remote.ApiClient
import com.zenith26.fest.ui.components.BaseUrlDialog
import com.zenith26.fest.ui.components.ErrorStateBanner
import com.zenith26.fest.ui.components.LoadingStateIndicator
import com.zenith26.fest.ui.components.TopNavHeader
import com.zenith26.fest.ui.theme.*
import com.zenith26.fest.ui.viewmodel.AdminTab
import com.zenith26.fest.ui.viewmodel.AdminViewModel

@Composable
fun AdminDashboardScreen(
    viewModel: AdminViewModel,
    onNavigateToStudent: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var showAddEventModal by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopNavHeader(
                isAdmin = true,
                onNavigateToStudent = onNavigateToStudent,
                onRefresh = { viewModel.loadAllData() },
                onOpenAddEvent = { showAddEventModal = true },
                onOpenSettings = { showSettingsDialog = true }
            )
        },
        containerColor = Slate950
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Global Error Banner
            if (state.errorMessage != null) {
                item {
                    ErrorStateBanner(
                        title = "Unable to Reach Backend API",
                        message = state.errorMessage ?: "Could not fetch live stats.",
                        onRetry = { viewModel.loadAllData() }
                    )
                }
            }

            if (state.isLoading && state.stats == null) {
                item {
                    LoadingStateIndicator(message = "Computing live metrics from database...")
                }
            } else {
                // 1. Overview Stat Cards
                val stats = state.stats
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            MetricCard(
                                title = "Total Events",
                                icon = "🎪",
                                iconBgColor = Violet500.copy(alpha = 0.15f),
                                iconTextColor = Violet300,
                                value = stats?.totalEvents?.toString() ?: "—",
                                subtext = "Across all fest categories",
                                modifier = Modifier.weight(1f)
                            )

                            MetricCard(
                                title = "Total Registrations",
                                icon = "🎟️",
                                iconBgColor = Emerald500.copy(alpha = 0.15f),
                                iconTextColor = Emerald500,
                                value = stats?.totalRegistrations?.toString() ?: "—",
                                subtext = "Out of ${stats?.totalCapacity ?: 0} total capacity",
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            MetricCard(
                                title = "Capacity Utilization",
                                icon = "📈",
                                iconBgColor = Fuchsia500.copy(alpha = 0.15f),
                                iconTextColor = Fuchsia500,
                                value = String.format("%.1f%%", stats?.overallUtilizationPct ?: 0.0),
                                subtext = "Live capacity fill rate",
                                showProgressBar = true,
                                progressPct = (stats?.overallUtilizationPct ?: 0.0).toFloat(),
                                modifier = Modifier.weight(1f)
                            )

                            MetricCard(
                                title = "Upcoming Events",
                                icon = "🗓️",
                                iconBgColor = Amber500.copy(alpha = 0.15f),
                                iconTextColor = Amber500,
                                value = stats?.upcomingEventsCount?.toString() ?: "—",
                                subtext = "Scheduled today or later",
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // 2. Category Breakdown Section
                item {
                    CategoryBreakdownSection(
                        eventsByCategory = stats?.eventsByCategory ?: emptyMap(),
                        totalEvents = stats?.totalEvents ?: 1
                    )
                }

                // 3. Section Tabs Header
                item {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Slate800, RoundedCornerShape(0.dp))
                        ) {
                            val isEventsTab = state.activeTab == AdminTab.EVENTS
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { viewModel.switchTab(AdminTab.EVENTS) }
                                    .padding(vertical = 12.dp)
                                    .background(if (isEventsTab) Violet600.copy(alpha = 0.15f) else Slate950),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "📋 Event Capacity & Utilization",
                                    color = if (isEventsTab) Violet300 else Slate400,
                                    fontSize = 12.sp,
                                    fontWeight = if (isEventsTab) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Normal
                                )
                            }

                            val isRegsTab = state.activeTab == AdminTab.REGISTRATIONS
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { viewModel.switchTab(AdminTab.REGISTRATIONS) }
                                    .padding(vertical = 12.dp)
                                    .background(if (isRegsTab) Violet600.copy(alpha = 0.15f) else Slate950),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "👥 Registrations (${state.stats?.totalRegistrations ?: state.registrations.size})",
                                    color = if (isRegsTab) Violet300 else Slate400,
                                    fontSize = 12.sp,
                                    fontWeight = if (isRegsTab) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Normal
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Events over 90% capacity are highlighted automatically in warning amber",
                            color = Slate400,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                }

                // 4. Tab 1 vs Tab 2 Panel Content
                item {
                    when (state.activeTab) {
                        AdminTab.EVENTS -> {
                            EventUtilizationSection(
                                events = state.events,
                                onFilterByEvent = { eventId ->
                                    viewModel.filterByEventAndSwitchToRegistrations(eventId)
                                }
                            )
                        }
                        AdminTab.REGISTRATIONS -> {
                            RegistrationsSection(
                                registrations = state.registrations,
                                events = state.events,
                                selectedEventId = state.selectedEventFilterId,
                                onSelectEventFilter = { viewModel.setEventFilter(it) },
                                onCancelRegistration = { viewModel.cancelRegistration(it) }
                            )
                        }
                    }
                }
            }
        }
    }

    // Add Event Modal
    if (showAddEventModal) {
        AddEventModal(
            isSubmitting = state.isCreatingEvent,
            errorMessage = state.createEventError,
            onDismiss = { showAddEventModal = false },
            onSubmit = { request ->
                viewModel.createEvent(request) {
                    showAddEventModal = false
                }
            }
        )
    }

    // Base URL settings dialog
    if (showSettingsDialog) {
        BaseUrlDialog(
            currentUrl = ApiClient.getBaseUrl(),
            onDismiss = { showSettingsDialog = false },
            onSave = { newUrl ->
                ApiClient.setBaseUrl(newUrl)
                showSettingsDialog = false
                viewModel.loadAllData()
            }
        )
    }
}
