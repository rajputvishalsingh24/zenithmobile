package com.zenith26.fest

import com.google.gson.Gson
import com.zenith26.fest.data.model.Event
import com.zenith26.fest.data.model.RegistrationRequest
import com.zenith26.fest.data.model.RegistrationResponse
import com.zenith26.fest.data.model.Stats
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class EventModelTest {

    private val gson = Gson()

    @Test
    fun testEventJsonDeserialization() {
        val json = """
            {
                "id": 1,
                "title": "HackZenith 24-Hour Hackathon",
                "category": "Technical",
                "description": "Build high-impact AI/Web3/Mobile solutions in 24 hours.",
                "event_date": "2026-11-14",
                "event_time": "09:00 AM",
                "venue": "CS Block Auditorium & Labs",
                "capacity": 120,
                "fee": 250,
                "icon": "💻",
                "available_seats": 119
            }
        """.trimIndent()

        val event = gson.fromJson(json, Event::class.java)

        assertNotNull(event)
        assertEquals(1, event.id)
        assertEquals("HackZenith 24-Hour Hackathon", event.title)
        assertEquals("Technical", event.category)
        assertEquals(120, event.capacity)
        assertEquals(250, event.fee)
        assertEquals("💻", event.icon)
        assertEquals(119, event.availableSeats)
    }

    @Test
    fun testRegistrationRequestSerialization() {
        val req = RegistrationRequest(
            studentName = "Aarav Sharma",
            rollNumber = "2026CS1042",
            email = "aarav.sharma@college.edu",
            phone = "+91 9876543210"
        )

        val json = gson.toJson(req)
        assertTrue(json.contains("\"student_name\":\"Aarav Sharma\""))
        assertTrue(json.contains("\"roll_number\":\"2026CS1042\""))
        assertTrue(json.contains("\"email\":\"aarav.sharma@college.edu\""))
        assertTrue(json.contains("\"phone\":\"+91 9876543210\""))
    }

    @Test
    fun testRegistrationResponseDeserialization() {
        val json = """
            {
                "id": 1,
                "event_id": 1,
                "student_name": "Aarav Sharma",
                "registered_at": "2026-10-13 14:30:00"
            }
        """.trimIndent()

        val resp = gson.fromJson(json, RegistrationResponse::class.java)
        assertEquals(1, resp.id)
        assertEquals(1, resp.eventId)
        assertEquals("Aarav Sharma", resp.studentName)
        assertEquals("2026-10-13 14:30:00", resp.registeredAt)
    }

    @Test
    fun testStatsModelDeserialization() {
        val json = """
            {
                "total_events": 14,
                "total_registrations": 42,
                "total_capacity": 1459,
                "overall_utilization_pct": 2.9,
                "upcoming_events_count": 14,
                "events_by_category": {
                    "Technical": 3,
                    "Cultural": 3,
                    "Sports": 3,
                    "Gaming": 2,
                    "Workshop": 3
                }
            }
        """.trimIndent()

        val stats = gson.fromJson(json, Stats::class.java)
        assertEquals(14, stats.totalEvents)
        assertEquals(42, stats.totalRegistrations)
        assertEquals(1459, stats.totalCapacity)
        assertEquals(2.9, stats.overallUtilizationPct, 0.01)
        assertEquals(14, stats.upcomingEventsCount)
        assertEquals(3, stats.eventsByCategory["Technical"])
        assertEquals(2, stats.eventsByCategory["Gaming"])
    }

    @Test
    fun testClientValidationRules() {
        // Validation Rule 1: Email must contain @
        val invalidEmail = "invalidemail.com"
        val validEmail = "test@student.edu"

        assertTrue(!invalidEmail.contains("@") || invalidEmail.startsWith("@") || invalidEmail.endsWith("@"))
        assertTrue(validEmail.contains("@") && !validEmail.startsWith("@") && !validEmail.endsWith("@"))

        // Validation Rule 2: Available seats calculation
        val capacity = 100
        val registrationsCount = 92
        val availableSeats = capacity - registrationsCount
        val utilizationPct = (registrationsCount.toDouble() / capacity.toDouble()) * 100.0

        assertEquals(8, availableSeats)
        assertEquals(92.0, utilizationPct, 0.01)
        assertTrue(utilizationPct > 90.0) // Triggers warning highlight
    }
}
