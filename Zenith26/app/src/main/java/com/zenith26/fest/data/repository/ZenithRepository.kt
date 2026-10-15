package com.zenith26.fest.data.repository

import com.zenith26.fest.data.model.DeleteRegistrationResponse
import com.zenith26.fest.data.model.ErrorDetailResponse
import com.zenith26.fest.data.model.Event
import com.zenith26.fest.data.model.EventCreateRequest
import com.zenith26.fest.data.model.RegistrationRecord
import com.zenith26.fest.data.model.RegistrationRequest
import com.zenith26.fest.data.model.RegistrationResponse
import com.zenith26.fest.data.model.Stats
import com.zenith26.fest.data.remote.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class ZenithRepository {

    private fun getService() = ApiClient.getService()

    private fun <T> parseError(response: Response<T>): String {
        return try {
            val errorBody = response.errorBody()?.string()
            if (!errorBody.isNullOrBlank()) {
                val parsed = ApiClient.gson.fromJson(errorBody, ErrorDetailResponse::class.java)
                parsed?.detail ?: "HTTP ${response.code()}: ${response.message()}"
            } else {
                "HTTP ${response.code()}: ${response.message()}"
            }
        } catch (_: Exception) {
            "HTTP ${response.code()}: ${response.message()}"
        }
    }

    suspend fun fetchEvents(category: String? = null, search: String? = null): Result<List<Event>> =
        withContext(Dispatchers.IO) {
            try {
                val catParam = if (category != null && category.trim().isNotEmpty() && !category.equals("All", ignoreCase = true)) {
                    category.trim()
                } else null

                val searchParam = if (!search.isNullOrBlank()) search.trim() else null

                val response = getService().getEvents(category = catParam, search = searchParam)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception(parseError(response)))
                }
            } catch (e: Exception) {
                Result.failure(Exception(e.localizedMessage ?: "Failed to reach server. Please check connection."))
            }
        }

    suspend fun fetchEventDetails(eventId: Int): Result<Event> =
        withContext(Dispatchers.IO) {
            try {
                val response = getService().getEventDetails(eventId)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception(parseError(response)))
                }
            } catch (e: Exception) {
                Result.failure(Exception(e.localizedMessage ?: "Failed to fetch event details."))
            }
        }

    suspend fun registerForEvent(
        eventId: Int,
        request: RegistrationRequest
    ): Result<RegistrationResponse> =
        withContext(Dispatchers.IO) {
            try {
                val response = getService().registerForEvent(eventId, request)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception(parseError(response)))
                }
            } catch (e: Exception) {
                Result.failure(Exception(e.localizedMessage ?: "Network error during registration."))
            }
        }

    suspend fun fetchRegistrations(eventId: Int? = null): Result<List<RegistrationRecord>> =
        withContext(Dispatchers.IO) {
            try {
                val response = getService().getRegistrations(eventId)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception(parseError(response)))
                }
            } catch (e: Exception) {
                Result.failure(Exception(e.localizedMessage ?: "Failed to fetch registrations."))
            }
        }

    suspend fun fetchStats(): Result<Stats> =
        withContext(Dispatchers.IO) {
            try {
                val response = getService().getStats()
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception(parseError(response)))
                }
            } catch (e: Exception) {
                Result.failure(Exception(e.localizedMessage ?: "Failed to fetch dashboard statistics."))
            }
        }

    suspend fun createEvent(request: EventCreateRequest): Result<Event> =
        withContext(Dispatchers.IO) {
            try {
                val response = getService().createEvent(request)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception(parseError(response)))
                }
            } catch (e: Exception) {
                Result.failure(Exception(e.localizedMessage ?: "Failed to create event."))
            }
        }

    suspend fun deleteRegistration(registrationId: Int): Result<DeleteRegistrationResponse> =
        withContext(Dispatchers.IO) {
            try {
                val response = getService().deleteRegistration(registrationId)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception(parseError(response)))
                }
            } catch (e: Exception) {
                Result.failure(Exception(e.localizedMessage ?: "Failed to delete registration."))
            }
        }
}
