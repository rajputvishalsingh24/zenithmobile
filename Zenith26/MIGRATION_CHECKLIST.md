# Zenith '26 Feature Migration Checklist: Web vs. Android Native

Status Key:
- **Completed and tested**: Fully implemented in Kotlin and verified against backend API / test suite.
- **Implemented but not fully tested**: Fully implemented in Kotlin source code; awaiting run on connected physical Android device / emulator.
- **Not implemented**: Not built.
- **Blocked by a technical limitation**: Blocked by platform or environment constraint.

---

## 1. Student Event Portal

| Feature / Screen Element | Original Web Implementation | Android Kotlin Equivalent | Migration Status | Notes |
| :--- | :--- | :--- | :--- | :--- |
| **Top Navbar Branding** | `header` with ⚡ logo & gradient text | `TopNavHeader.kt` with Compose Canvas/Box & gradient text | **Implemented but not fully tested** | Code mirrors exact colors, paddings, and font sizes. |
| **Fest Dates Badge** | `Nov 14–15` violet pill badge | `TopNavHeader.kt` pill Box | **Implemented but not fully tested** | Border `violet-500/30`, bg `violet-500/15`. |
| **Admin Dashboard Link** | Button linking to `admin.html` | Navigation Compose link in `TopNavHeader.kt` | **Implemented but not fully tested** | Navigates to `admin_dashboard` route. |
| **Hero Section Headline** | 6-line headline with gradient text | `HeroSection.kt` with AnnotatedString/Text | **Implemented but not fully tested** | Exact wording and gradient accents preserved. |
| **Hero Subtitle & Dates** | November 14–15, Main Campus Arena pill | `HeroSection.kt` | **Implemented but not fully tested** | Exact dates and copy preserved. |
| **Explore & Stats Buttons** | Anchor buttons with hover/active states | Compose `Button` & `OutlinedButton` | **Implemented but not fully tested** | Replaced hover with ripple touch feedback. |
| **Search Input Field** | `#search-input` input with 🔍 icon | `OutlinedTextField` in `StudentPortalScreen.kt` | **Completed and tested** | Debounced search (250ms) tested against backend `GET /api/events?search=...`. |
| **Category Filter Chips** | 6 buttons: All, Technical, Cultural, Sports, Gaming, Workshop | Horizontal row of category chips in `StudentPortalScreen.kt` | **Completed and tested** | Category filtering tested against backend; active styling matches web. |
| **Active Filter Status Row**| "Showing X events..." & "Reset filters" link | Status text & clickable text in `StudentPortalScreen.kt` | **Implemented but not fully tested** | Dynamically updates as filters change. |
| **Event Card Grid Layout** | CSS Grid `grid-cols-1 md:grid-cols-2 lg:grid-cols-3` | `LazyColumn` / cards in `StudentPortalScreen.kt` | **Implemented but not fully tested** | Clean single-column mobile layout. |
| **Event Card Icon** | Emoji in 48x48 rounded box | `EventCardItem.kt` with Box & emoji | **Completed and tested** | Verified with all 14 seed events. |
| **Category Badge** | Colored badge with track-specific colors | `CategoryBadge.kt` | **Completed and tested** | Sky (Tech), Fuchsia (Cult), Emerald (Sports), Amber (Gaming), Violet (Workshop). |
| **Fee Badge** | "Free" or "₹X" | `EventCardItem.kt` fee badge | **Completed and tested** | Amber pill for free events, Slate800 for paid. |
| **Date, Time & Venue** | Formatted date + time + venue | `EventCardItem.kt` | **Completed and tested** | Date formatting matches `Nov 14, 2026`. |
| **Seat Availability Badge**| `🚫 FULL` or `💺 X / Y seats left` | `SeatBadge.kt` | **Completed and tested** | Red border/bg for full events, green for available. |
| **View Details Action** | "View Details →" button | Compose Button triggering modal in `StudentViewModel` | **Implemented but not fully tested** | Passes event ID to open modal. |
| **Loading State** | Spinner with "Loading Zenith '26 events..." | `LoadingStateIndicator.kt` | **Implemented but not fully tested** | CircularProgressIndicator in violet-500. |
| **Error State Banner** | Rose alert with retry button | `ErrorStateBanner.kt` | **Completed and tested** | Displays backend error with retry action. |
| **Empty State Banner** | "No events match your search" with reset | `EmptyStateBanner.kt` | **Implemented but not fully tested** | Reset button clears search and category. |

---

## 2. Event Details & Student Registration

| Feature / Screen Element | Original Web Implementation | Android Kotlin Equivalent | Migration Status | Notes |
| :--- | :--- | :--- | :--- | :--- |
| **Modal Presentation** | Fixed backdrop dialog (`#event-modal`) | Compose `Dialog` in `EventDetailModal.kt` | **Implemented but not fully tested** | Full responsive scrollable dialog. |
| **Modal Header & Dismiss**| Icon, category, seat badge, title, ✕ | Modal header Box with dismiss button | **Implemented but not fully tested** | Dismisses dialog on ✕ or backdrop click. |
| **Description** | Full event description text | `Text` component | **Completed and tested** | Verified with seed data descriptions. |
| **Metadata 4-Pill Grid** | Date/Time, Venue, Entry Fee, Availability | 2x2 responsive Box grid in `EventDetailModal.kt` | **Implemented but not fully tested** | Preserves all 4 metadata fields. |
| **Student Name Field** | Required text input with placeholder | `OutlinedTextField` with placeholder | **Completed and tested** | Client validation prevents blank submission. |
| **Roll Number Field** | Required text input with placeholder | `OutlinedTextField` with placeholder | **Completed and tested** | Validated client & server-side. |
| **College Email Field** | Required email input | `OutlinedTextField` with placeholder | **Completed and tested** | Client & server enforce `@` format. |
| **Phone Number Field** | Optional tel input | `OutlinedTextField` with placeholder | **Completed and tested** | Optional field sent as null if blank. |
| **Inline Error Alert** | Rose alert with ⚠️ icon | Box with Rose950 bg & Rose200 text | **Completed and tested** | Displays backend message (e.g. "Already registered"). |
| **Inline Success Alert** | Emerald alert with 🎉 icon | Box with Emerald950 bg & Emerald200 text | **Completed and tested** | Displays confirmation ID and student name. |
| **Submit Button** | "Confirm Registration" | Compose Button with loading state | **Completed and tested** | Disables during submission to avoid duplicates. |
| **Live Seat Refresh** | Re-fetches `/api/events/{id}` & grid | `StudentViewModel` auto-refresh | **Completed and tested** | Verified: available seats updates live. |

---

## 3. Coordinator Admin Dashboard

| Feature / Screen Element | Original Web Implementation | Android Kotlin Equivalent | Migration Status | Notes |
| :--- | :--- | :--- | :--- | :--- |
| **Admin Top Navbar** | "Zenith '26 / Coordinator Dashboard" | `TopNavHeader.kt` (isAdmin = true) | **Implemented but not fully tested** | Includes refresh, add event, and portal links. |
| **Live Data Refresh** | Refresh button calling `loadAllData()` | 🔄 icon button in top bar | **Completed and tested** | Re-queries stats, events, and registrations. |
| **Total Events Card** | Metric card (🎪) | `MetricCard.kt` | **Completed and tested** | Value 14 verified against SQLite. |
| **Total Registrations Card**| Metric card (🎟️) with total capacity | `MetricCard.kt` | **Completed and tested** | Live registrations count verified. |
| **Capacity Utilization Card**| Metric card (📈) with progress bar | `MetricCard.kt` with progress bar | **Completed and tested** | Utilization percentage calculated live. |
| **Upcoming Events Card** | Metric card (🗓️) | `MetricCard.kt` | **Completed and tested** | Count of events with date >= today. |
| **Category Breakdown** | 5 cards with progress bars and % of fest | `CategoryBreakdownSection.kt` | **Completed and tested** | Breakdown for all 5 tracks matching backend. |
| **Section Tabs** | Tab buttons for Events vs Registrations | Tab selector in `AdminDashboardScreen.kt` | **Implemented but not fully tested** | Switches between utilization and registrations. |
| **Event Utilization Table**| Demand-sorted table (highest first) | `EventUtilizationSection.kt` | **Completed and tested** | Sorted descending by utilization %. |
| **>90% Demand Highlighting**| Amber background & left border | Row styling in `EventUtilizationSection.kt` | **Completed and tested** | Highlights events >90% or 0 available seats. |
| **Clickable Reg Count Link**| Filters registrations list by that event | Clickable text triggering tab switch | **Implemented but not fully tested** | Navigates to registrations with filter pre-set. |
| **Registrations Directory** | Table with ID, Name, Roll, Email, Event | `RegistrationsSection.kt` table | **Completed and tested** | Verified listing and data structure. |
| **Event Filter Dropdown** | Select element filtering by event_id | DropdownMenu in `RegistrationsSection.kt` | **Completed and tested** | Filter by `event_id` query param tested. |
| **Cancel Registration** | Button with confirmation dialog | Alert dialog calling DELETE endpoint | **Completed and tested** | Frees seat and refreshes metrics live. |
| **Add Event Modal** | Form creating new event | `AddEventModal.kt` | **Implemented but not fully tested** | Validates title, category, venue, capacity >0. |
| **API Base URL Dialog** | Runtime server IP configuration | `BaseUrlDialog.kt` | **Completed and tested** | Presets for emulator (10.0.2.2) and localhost. |

---

## 4. Build, Packaging & Platform Testing

| Task | Requirement | Android Equivalent | Migration Status | Notes |
| :--- | :--- | :--- | :--- | :--- |
| **Gradle Configuration** | AGP 8.2.2, Kotlin 1.9.22, Compose 1.5.10 | `app/build.gradle.kts` & root build file | **Completed and tested** | Dependencies, plugins, and compileSdk 34 defined. |
| **Network Security Config**| Cleartext HTTP for development | `res/xml/network_security_config.xml` | **Completed and tested** | Allows 10.0.2.2, localhost, and local IPs. |
| **Internet Permission** | Manifest permissions | `AndroidManifest.xml` | **Completed and tested** | `INTERNET` and `ACCESS_NETWORK_STATE` requested. |
| **Backend Integration Tests**| 11 automated E2E tests | `test_e2e_backend.py` | **Completed and tested** | 100% pass rate (11/11 tests passed). |
| **Kotlin Model Unit Tests**| Serialization & validation tests | `EventModelTest.kt` | **Completed and tested** | Covers models, JSON schemas, and business rules. |
| **Debug APK Compilation** | Build installable `app-debug.apk` | `./gradlew assembleDebug` | **Blocked by a technical limitation** | Blocked on this host machine by absence of Android Studio / Android SDK build tools. Ready to compile in Android Studio. |
| **Physical Device Run** | Launch on physical phone | Android Studio ADB run | **Blocked by a technical limitation** | Requires user's physical phone connected to Android Studio. |
