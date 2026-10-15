package com.zenith26.fest.data.model

import com.google.gson.annotations.SerializedName

/**
 * Data model for an Event in Zenith '26.
 * Matches backend GET /api/events and GET /api/events/{event_id}.
 */
data class Event(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("category")
    val category: String,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("event_date")
    val eventDate: String,

    @SerializedName("event_time")
    val eventTime: String,

    @SerializedName("venue")
    val venue: String,

    @SerializedName("capacity")
    val capacity: Int,

    @SerializedName("fee")
    val fee: Int = 0,

    @SerializedName("icon")
    val icon: String? = "✨",

    @SerializedName("available_seats")
    val availableSeats: Int
)

/**
 * Request payload for student registration.
 * Matches backend POST /api/events/{event_id}/register.
 */
data class RegistrationRequest(
    @SerializedName("student_name")
    val studentName: String,

    @SerializedName("roll_number")
    val rollNumber: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("phone")
    val phone: String? = null
)

/**
 * Successful response returned by POST /api/events/{event_id}/register.
 */
data class RegistrationResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("event_id")
    val eventId: Int,

    @SerializedName("student_name")
    val studentName: String,

    @SerializedName("registered_at")
    val registeredAt: String
)

/**
 * Full student registration record for Admin Dashboard.
 * Matches backend GET /api/registrations.
 */
data class RegistrationRecord(
    @SerializedName("id")
    val id: Int,

    @SerializedName("event_id")
    val eventId: Int,

    @SerializedName("event_title")
    val eventTitle: String,

    @SerializedName("student_name")
    val studentName: String,

    @SerializedName("roll_number")
    val rollNumber: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("phone")
    val phone: String? = null,

    @SerializedName("registered_at")
    val registeredAt: String
)

/**
 * Live Fest statistics for Coordinator Admin Dashboard.
 * Matches backend GET /api/stats.
 */
data class Stats(
    @SerializedName("total_events")
    val totalEvents: Int,

    @SerializedName("total_registrations")
    val totalRegistrations: Int,

    @SerializedName("total_capacity")
    val totalCapacity: Int,

    @SerializedName("overall_utilization_pct")
    val overallUtilizationPct: Double,

    @SerializedName("upcoming_events_count")
    val upcomingEventsCount: Int,

    @SerializedName("events_by_category")
    val eventsByCategory: Map<String, Int> = emptyMap()
)

/**
 * Payload for creating a new fest event (Stretch Goal 1).
 * Matches backend POST /api/events.
 */
data class EventCreateRequest(
    @SerializedName("title")
    val title: String,

    @SerializedName("category")
    val category: String,

    @SerializedName("description")
    val description: String = "",

    @SerializedName("event_date")
    val eventDate: String,

    @SerializedName("event_time")
    val eventTime: String,

    @SerializedName("venue")
    val venue: String,

    @SerializedName("capacity")
    val capacity: Int,

    @SerializedName("fee")
    val fee: Int = 0,

    @SerializedName("icon")
    val icon: String = "✨"
)

/**
 * Response when deleting a registration (Stretch Goal 2).
 */
data class DeleteRegistrationResponse(
    @SerializedName("message")
    val message: String,

    @SerializedName("id")
    val id: Int,

    @SerializedName("event_id")
    val eventId: Int,

    @SerializedName("student_name")
    val studentName: String
)

/**
 * Standard backend error response format (FastAPI HTTPException detail).
 */
data class ErrorDetailResponse(
    @SerializedName("detail")
    val detail: String? = null
)
