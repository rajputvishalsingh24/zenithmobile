# Zenith '26 UI/UX Comparison & Platform Verification Report

This report provides an in-depth, side-by-side comparative analysis of the original web application (`frontend/index.html`, `frontend/admin.html`) versus the native Android Kotlin application built with **Jetpack Compose**.

---

## 1. Visual Design System Parity

| Design Token | Web Application (HTML + Tailwind CSS) | Android Application (Jetpack Compose) | Parity Evaluation |
| :--- | :--- | :--- | :--- |
| **Theme / Background** | `bg-slate-950` (`#020617`) | `Slate950 = Color(0xFF020617)` applied to `Surface` and `Scaffold` | **100% Identical** |
| **Card Surfaces** | `bg-slate-900/90` (`#0F172A`) with `border-slate-800` (`#1E293B`) | `Slate900.copy(alpha = 0.90f)` with border `Slate800` (1.dp) | **100% Identical** |
| **Corner Radii** | `rounded-2xl` (16px), `rounded-xl` (12px), `rounded-full` (9999px) | `RoundedCornerShape(16.dp)`, `RoundedCornerShape(12.dp)`, `RoundedCornerShape(50)` | **100% Identical** |
| **Primary Accents** | Tailwind `violet-600` (`#7C3AED`), `violet-500` (`#8B5CF6`) | `Violet600 = Color(0xFF7C3AED)`, `Violet500 = Color(0xFF8B5CF6)` | **100% Identical** |
| **Secondary Accents** | Tailwind `fuchsia-600` (`#C026D3`), `fuchsia-500` (`#D946EF`) | `Fuchsia600 = Color(0xFFC026D3)`, `Fuchsia500 = Color(0xFFD946EF)` | **100% Identical** |
| **Warning / Status** | `amber-500`, `rose-500`, `emerald-500` | `Amber500`, `Rose500`, `Emerald500` | **100% Identical** |
| **Typography Scale** | Tailwind `font-black`, `font-extrabold`, `font-bold`, `text-sm`, `text-xs` | Material 3 `Typography` with `FontWeight.Black`, `FontWeight.ExtraBold`, `sp` scales | **Faithfully Preserved** |

---

## 2. Screen-by-Screen Component Comparison

### 2.1 Student Event Portal
1. **Top Navbar**:
   - *Web*: Sticky backdrop blurred header with ⚡ icon in gradient box, "Zenith '26" gradient text, "Nov 14-15" badge, "Events" anchor link, and "Admin Dashboard" button.
   - *Android*: `TopNavHeader.kt` renders identical ⚡ icon in linear gradient box, "Zenith '26" with bold typography, "Nov 14-15" badge, and one-tap transition button to Admin Dashboard, plus a ⚙️ server configuration icon.
2. **Hero Section**:
   - *Web*: Radial/linear gradient backdrop, "🗓️ November 14–15, 2026 • Main Campus Arena" pill, headline "Where Code, Culture & Adrenaline Collide at Zenith '26", descriptive subtitle, and 2 CTA buttons.
   - *Android*: `HeroSection.kt` reproduces the identical badge pill, exact headline wording, subtitle copy, and action buttons. On mobile phone screens, buttons wrap neatly into an accessible touch-friendly row.
3. **Search & Category Filter Bar**:
   - *Web*: Search input with 🔍 icon and placeholder, 6 category buttons (`All`, `💻 Technical`, `🎭 Cultural`, `⚽ Sports`, `🎮 Gaming`, `🛠️ Workshop`), live count status, and "Reset filters" button.
   - *Android*: `StudentPortalScreen.kt` integrates an `OutlinedTextField` with leading search icon, debounced 250ms keystroke listener, horizontal scrolling category chips with active violet highlight, dynamic count label, and "Reset filters" action.
4. **Event Cards Grid**:
   - *Web*: Responsive 1/2/3-column grid of cards with emoji box, category badge, free/fee pill, title, 2-line description clamp, date, venue, seats left badge, and "View Details →" button.
   - *Android*: `EventCardItem.kt` renders an identical layout adapted to mobile phone widths. Touch targets meet Android's 48dp accessibility standard.

---

### 2.2 Event Detail & Registration Dialog
1. **Modal Header & Metadata**:
   - *Web*: Modal with ✕ button, emoji box, category pill, seat availability badge, title, full description, and 4-pill metadata box (Date/Time, Venue, Fee, Availability).
   - *Android*: `EventDetailModal.kt` displays the exact same modal header hierarchy with full description and a 2x2 metadata grid.
2. **Student Registration Form**:
   - *Web*: Required inputs (Student Name, Roll Number, College Email) marked with red asterisk `*`, optional Phone input, inline error alert, inline success alert, and "Confirm Registration" button.
   - *Android*: Implements identical form fields, placeholder texts, client-side validation (non-empty check and email containing `@`), server-side validation error presentation, and inline success notification with confirmation ID. After success, automatically refreshes live seat counts and closes modal.

---

### 2.3 Coordinator Admin Dashboard
1. **Overview Stat Cards (4 Cards)**:
   - *Web*: Total Events (🎪), Total Registrations (🎟️), Capacity Utilization (📈) with animated progress bar, Upcoming Events (🗓️).
   - *Android*: `MetricCard.kt` displays all 4 metrics with identical icons, labels, subtexts, and progress bar for utilization.
2. **Category Breakdown**:
   - *Web*: 5 cards showing Category icon, badge, count, and progress bar representing percentage of fest schedule.
   - *Android*: `CategoryBreakdownSection.kt` renders all 5 category cards with matching track colors and percentage bars.
3. **Tabbed Navigation**:
   - *Web*: Two tabs: "📋 Event Capacity & Utilization" and "👥 Student Registrations (X)".
   - *Android*: Direct tab selector in `AdminDashboardScreen.kt` toggling between the utilization table and registration records.
4. **Event Utilization Table**:
   - *Web*: Table sorted descending by utilization %; rows > 90% highlighted in amber with left border. Clicking registered count filters student registrations.
   - *Android*: `EventUtilizationSection.kt` renders a smooth horizontally scrollable table preserving all 6 columns (`Title`, `Category`, `Date & Time`, `Venue`, `Registered / Cap`, `Utilization %`). Highlighting for events >90% or FULL is preserved using amber background and warning badges. Clicking the registration count navigates to the Registrations tab filtered by that event.
5. **Student Registrations Directory**:
   - *Web*: Filter dropdown by event, table with ID, Name, Roll Number, Email, Event, Registered At, and Cancel button (DELETE `/api/registrations/{id}`).
   - *Android*: `RegistrationsSection.kt` implements the event filter dropdown, full table, and a confirmation dialog before calling the DELETE endpoint to cancel registrations and free up seats.
6. **Add Event Modal (Stretch Goal)**:
   - *Web*: Form creating new event (`POST /api/events`).
   - *Android*: `AddEventModal.kt` provides complete form validation and API creation.

---

## 3. Platform Adaptations & Differences Handled

1. **Touch Targets**: Desktop mouse hovers were adapted to native Android ripple effects (`Modifier.clickable`), touch states, and tactile button feedback.
2. **Soft Keyboard Handling**: Form inputs are wrapped in scrollable containers with `windowSoftInputMode="adjustResize"` in `AndroidManifest.xml` so that the virtual keyboard never obscures input fields or action buttons.
3. **Wide Table on Narrow Phone**: The event utilization table and registrations table are wrapped in Compose `horizontalScroll` with fixed column widths, preventing text clipping, overlapping columns, or omitted data on narrow mobile phone screens.
4. **Configurable Base URL**: While the web app assumes `window.location.origin`, the Android app includes `BaseUrlDialog.kt` to easily switch between standard Android Emulator loopback (`http://10.0.2.2:8001/`) and physical LAN IP (`http://192.168.x.x:8001/`).
