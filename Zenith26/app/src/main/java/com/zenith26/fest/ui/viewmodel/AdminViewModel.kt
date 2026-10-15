package com.zenith26.fest.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zenith26.fest.data.model.Event
import com.zenith26.fest.data.model.EventCreateRequest
import com.zenith26.fest.data.model.RegistrationRecord
import com.zenith26.fest.data.model.Stats
import com.zenith26.fest.data.repository.ZenithRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AdminTab {
    EVENTS,
    REGISTRATIONS
}

data class AdminUiState(
    val stats: Stats? = null,
    val events: List<Event> = emptyList(),
    val registrations: List<RegistrationRecord> = emptyList(),
    val selectedEventFilterId: Int? = null,
    val activeTab: AdminTab = AdminTab.EVENTS,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isCreatingEvent: Boolean = false,
    val createEventError: String? = null
)

class AdminViewModel(
    private val repository: ZenithRepository = ZenithRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState.asStateFlow()

    init {
        loadAllData()
    }

    fun loadAllData() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            val statsResult = repository.fetchStats()
            val eventsResult = repository.fetchEvents()
            val regsResult = repository.fetchRegistrations(eventId = _uiState.value.selectedEventFilterId)

            var globalError: String? = null
            var fetchedStats: Stats? = _uiState.value.stats
            var fetchedEvents: List<Event> = _uiState.value.events
            var fetchedRegs: List<RegistrationRecord> = _uiState.value.registrations

            statsResult.fold(
                onSuccess = { fetchedStats = it },
                onFailure = { globalError = it.message }
            )

            eventsResult.fold(
                onSuccess = { fetchedEvents = it },
                onFailure = { if (globalError == null) globalError = it.message }
            )

            regsResult.fold(
                onSuccess = { fetchedRegs = it },
                onFailure = { if (globalError == null) globalError = it.message }
            )

            _uiState.update {
                it.copy(
                    stats = fetchedStats,
                    events = fetchedEvents,
                    registrations = fetchedRegs,
                    isLoading = false,
                    errorMessage = globalError
                )
            }
        }
    }

    fun switchTab(tab: AdminTab) {
        _uiState.update { it.copy(activeTab = tab) }
        if (tab == AdminTab.REGISTRATIONS) {
            refreshRegistrations()
        }
    }

    fun setEventFilter(eventId: Int?) {
        _uiState.update { it.copy(selectedEventFilterId = eventId) }
        refreshRegistrations()
    }

    fun filterByEventAndSwitchToRegistrations(eventId: Int) {
        _uiState.update {
            it.copy(
                selectedEventFilterId = eventId,
                activeTab = AdminTab.REGISTRATIONS
            )
        }
        refreshRegistrations()
    }

    private fun refreshRegistrations() {
        viewModelScope.launch {
            val regsResult = repository.fetchRegistrations(eventId = _uiState.value.selectedEventFilterId)
            regsResult.onSuccess { list ->
                _uiState.update { it.copy(registrations = list) }
            }
        }
    }

    fun cancelRegistration(registration: RegistrationRecord) {
        viewModelScope.launch {
            val result = repository.deleteRegistration(registration.id)
            result.onSuccess {
                // Reload dashboard data so stats and seat counts update immediately
                loadAllData()
            }
        }
    }

    fun createEvent(request: EventCreateRequest, onDismissDialog: () -> Unit) {
        _uiState.update { it.copy(isCreatingEvent = true, createEventError = null) }
        viewModelScope.launch {
            val result = repository.createEvent(request)
            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(isCreatingEvent = false, createEventError = null) }
                    onDismissDialog()
                    loadAllData()
                },
                onFailure = { err ->
                    _uiState.update {
                        it.copy(
                            isCreatingEvent = false,
                            createEventError = err.message ?: "Failed to create event."
                        )
                    }
                }
            )
        }
    }
}
