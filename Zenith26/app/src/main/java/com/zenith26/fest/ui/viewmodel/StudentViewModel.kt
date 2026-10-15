package com.zenith26.fest.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zenith26.fest.data.model.Event
import com.zenith26.fest.data.model.RegistrationRequest
import com.zenith26.fest.data.repository.ZenithRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class StudentUiState(
    val events: List<Event> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedCategory: String = "All",
    val searchQuery: String = "",
    val activeEvent: Event? = null,
    val isRegistering: Boolean = false,
    val registrationError: String? = null,
    val registrationSuccess: String? = null
)

class StudentViewModel(
    private val repository: ZenithRepository = ZenithRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(StudentUiState())
    val uiState: StateFlow<StudentUiState> = _uiState.asStateFlow()

    private var searchDebounceJob: Job? = null

    init {
        loadEvents()
    }

    fun loadEvents() {
        val currentCat = _uiState.value.selectedCategory
        val currentSearch = _uiState.value.searchQuery

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            val result = repository.fetchEvents(category = currentCat, search = currentSearch)
            result.fold(
                onSuccess = { list ->
                    _uiState.update {
                        it.copy(
                            events = list,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { err ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = err.message ?: "Failed to load events from server."
                        )
                    }
                }
            )
        }
    }

    fun selectCategory(category: String) {
        if (_uiState.value.selectedCategory == category) return
        _uiState.update { it.copy(selectedCategory = category) }
        loadEvents()
    }

    fun onSearchQueryChanged(newQuery: String) {
        _uiState.update { it.copy(searchQuery = newQuery) }
        searchDebounceJob?.cancel()
        searchDebounceJob = viewModelScope.launch {
            delay(250) // Match web app 220ms debounce
            loadEvents()
        }
    }

    fun resetFilters() {
        searchDebounceJob?.cancel()
        _uiState.update {
            it.copy(
                selectedCategory = "All",
                searchQuery = ""
            )
        }
        loadEvents()
    }

    fun openEventDetails(event: Event) {
        _uiState.update {
            it.copy(
                activeEvent = event,
                registrationError = null,
                registrationSuccess = null,
                isRegistering = false
            )
        }
        // Fetch fresh single-event details
        viewModelScope.launch {
            val freshResult = repository.fetchEventDetails(event.id)
            freshResult.onSuccess { freshEvent ->
                _uiState.update {
                    if (it.activeEvent?.id == freshEvent.id) {
                        it.copy(activeEvent = freshEvent)
                    } else it
                }
            }
        }
    }

    fun closeEventModal() {
        _uiState.update {
            it.copy(
                activeEvent = null,
                registrationError = null,
                registrationSuccess = null,
                isRegistering = false
            )
        }
    }

    fun submitRegistration(request: RegistrationRequest) {
        val event = _uiState.value.activeEvent ?: return
        _uiState.update {
            it.copy(
                isRegistering = true,
                registrationError = null,
                registrationSuccess = null
            )
        }

        viewModelScope.launch {
            val result = repository.registerForEvent(event.id, request)
            result.fold(
                onSuccess = { response ->
                    val successMsg = "Registered ${response.studentName} for \"${event.title}\"! Confirmation ID #${response.id}."
                    _uiState.update {
                        it.copy(
                            isRegistering = false,
                            registrationSuccess = successMsg,
                            registrationError = null
                        )
                    }
                    // Immediately re-fetch this event details to update available_seats
                    val updatedEventResult = repository.fetchEventDetails(event.id)
                    updatedEventResult.onSuccess { updatedEvent ->
                        _uiState.update {
                            if (it.activeEvent?.id == updatedEvent.id) {
                                it.copy(activeEvent = updatedEvent)
                            } else it
                        }
                    }
                    // Refresh the main grid so the card seat count updates live
                    loadEvents()
                },
                onFailure = { err ->
                    _uiState.update {
                        it.copy(
                            isRegistering = false,
                            registrationError = err.message ?: "Registration failed."
                        )
                    }
                }
            )
        }
    }
}
