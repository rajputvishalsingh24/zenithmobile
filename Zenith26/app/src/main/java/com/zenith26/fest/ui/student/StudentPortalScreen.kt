package com.zenith26.fest.ui.student

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zenith26.fest.data.remote.ApiClient
import com.zenith26.fest.ui.components.BaseUrlDialog
import com.zenith26.fest.ui.components.EmptyStateBanner
import com.zenith26.fest.ui.components.ErrorStateBanner
import com.zenith26.fest.ui.components.LoadingStateIndicator
import com.zenith26.fest.ui.components.TopNavHeader
import com.zenith26.fest.ui.theme.*
import com.zenith26.fest.ui.viewmodel.StudentViewModel

private val CATEGORIES = listOf(
    "All" to "All",
    "Technical" to "💻 Technical",
    "Cultural" to "🎭 Cultural",
    "Sports" to "⚽ Sports",
    "Gaming" to "🎮 Gaming",
    "Workshop" to "🛠️ Workshop"
)

@Composable
fun StudentPortalScreen(
    viewModel: StudentViewModel,
    onNavigateToAdmin: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var showSettingsDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopNavHeader(
                isAdmin = false,
                onNavigateToAdmin = onNavigateToAdmin,
                onOpenSettings = { showSettingsDialog = true }
            )
        },
        containerColor = Slate950
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // 1. Hero Section
            item {
                HeroSection(
                    onExploreClicked = { /* Already on page, user scrolls to events */ },
                    onAdminStatsClicked = onNavigateToAdmin
                )
            }

            // 2. Filter & Search Controls Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 14.dp)
                        .background(Slate900.copy(alpha = 0.80f), RoundedCornerShape(16.dp))
                        .border(1.dp, Slate800, RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        // Search Box
                        OutlinedTextField(
                            value = state.searchQuery,
                            onValueChange = { viewModel.onSearchQueryChanged(it) },
                            placeholder = {
                                Text(
                                    text = "Search events by title (e.g. Hackathon, Valorant)...",
                                    color = Slate500,
                                    fontSize = 12.sp
                                )
                            },
                            leadingIcon = {
                                Text(text = "🔍", fontSize = 14.sp)
                            },
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

                        Spacer(modifier = Modifier.height(12.dp))

                        // Category Buttons (Horizontal scroll)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            CATEGORIES.forEach { (categoryKey, categoryLabel) ->
                                val isSelected = state.selectedCategory.equals(categoryKey, ignoreCase = true)
                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = if (isSelected) Violet600 else Slate800,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .clickable { viewModel.selectCategory(categoryKey) }
                                        .padding(horizontal = 14.dp, vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = categoryLabel,
                                        color = if (isSelected) PureWhite else Slate300,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Filter Status Row
                        val hasFilter = state.selectedCategory != "All" || state.searchQuery.isNotBlank()
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Slate800, RoundedCornerShape(0.dp))
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val countText = when {
                                state.isLoading -> "Loading events..."
                                state.errorMessage != null -> "Failed to load events"
                                else -> "Showing ${state.events.size} event${if (state.events.size == 1) "" else "s"}" +
                                        (if (state.selectedCategory != "All") " in ${state.selectedCategory}" else "") +
                                        (if (state.searchQuery.isNotBlank()) " matching \"${state.searchQuery.trim()}\"" else "")
                            }
                            Text(
                                text = countText,
                                color = Slate400,
                                fontSize = 11.sp
                            )

                            if (hasFilter) {
                                Text(
                                    text = "Reset filters",
                                    color = Violet400,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.clickable { viewModel.resetFilters() }
                                )
                            }
                        }
                    }
                }
            }

            // 3. Events Grid / List
            when {
                state.isLoading -> {
                    item {
                        LoadingStateIndicator()
                    }
                }
                state.errorMessage != null -> {
                    item {
                        ErrorStateBanner(
                            message = state.errorMessage ?: "Could not connect to the backend API.",
                            onRetry = { viewModel.loadEvents() }
                        )
                    }
                }
                state.events.isEmpty() -> {
                    item {
                        EmptyStateBanner(
                            onReset = { viewModel.resetFilters() }
                        )
                    }
                }
                else -> {
                    items(state.events, key = { it.id }) { event ->
                        Box(modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)) {
                            EventCardItem(
                                event = event,
                                onViewDetails = { viewModel.openEventDetails(event) }
                            )
                        }
                    }
                }
            }
        }
    }

    // Detail & Registration Dialog
    if (state.activeEvent != null) {
        EventDetailModal(
            event = state.activeEvent!!,
            isSubmitting = state.isRegistering,
            errorMessage = state.registrationError,
            successMessage = state.registrationSuccess,
            onDismiss = { viewModel.closeEventModal() },
            onSubmitRegistration = { viewModel.submitRegistration(it) }
        )
    }

    // API Base URL Configuration Dialog
    if (showSettingsDialog) {
        BaseUrlDialog(
            currentUrl = ApiClient.getBaseUrl(),
            onDismiss = { showSettingsDialog = false },
            onSave = { newUrl ->
                ApiClient.setBaseUrl(newUrl)
                showSettingsDialog = false
                viewModel.loadEvents()
            }
        )
    }
}
