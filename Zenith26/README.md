# Zenith '26 Native Android Mobile Application

Native Android implementation of the **Zenith '26 College Fest Management System**, written in **Kotlin** using **Jetpack Compose**, **Retrofit 2**, **Kotlin Coroutines**, and **Android Architecture Components (ViewModel, StateFlow)**.

---

## 1. Project Overview & Architecture

This application preserves 100% of the original Zenith '26 web interface styling, color tokens, business logic, validation rules, and workflows without redesigning the product.

- **Platform:** Native Android (Kotlin 1.9.22)
- **UI Toolkit:** Jetpack Compose (BOM 2024.02.01) with Material 3 custom tokens
- **Target SDK:** Android 14 (API 34)
- **Minimum SDK:** Android 7.0 (API 24)
- **Networking:** Retrofit 2.9.0 + OkHttp 4.12.0 + Gson
- **Asynchronous Operations:** Kotlin Coroutines & Flow
- **Architecture:** Clean MVVM with Repository Pattern
- **Backend Single Source of Truth:** Python FastAPI backend + SQLite (`fest.db`)

---

## 2. Project Directory Structure

```text
Zenith26/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/zenith26/fest/
│   │   │   │   ├── MainActivity.kt                  # Root single-activity container
│   │   │   │   ├── data/
│   │   │   │   │   ├── model/Models.kt              # Data models matching backend API schemas
│   │   │   │   │   ├── remote/
│   │   │   │   │   │   ├── ApiClient.kt             # Configurable Retrofit singleton
│   │   │   │   │   │   └── ZenithApiService.kt      # Retrofit endpoint definitions
│   │   │   │   │   └── repository/ZenithRepository.kt # Repository layer with error parsing
│   │   │   │   ├── ui/
│   │   │   │   │   ├── theme/                       # Slate-950, Violet, Category colors & Typography
│   │   │   │   │   │   ├── Color.kt
│   │   │   │   │   │   ├── Theme.kt
│   │   │   │   │   │   └── Type.kt
│   │   │   │   │   ├── components/                  # Badges, Header, State banners, URL dialog
│   │   │   │   │   │   ├── BaseUrlDialog.kt
│   │   │   │   │   │   ├── CategoryBadge.kt
│   │   │   │   │   │   ├── SeatBadge.kt
│   │   │   │   │   │   ├── StatusBanners.kt
│   │   │   │   │   │   └── TopNavHeader.kt
│   │   │   │   │   ├── student/                     # Student portal & registration components
│   │   │   │   │   │   ├── EventCardItem.kt
│   │   │   │   │   │   ├── EventDetailModal.kt
│   │   │   │   │   │   ├── HeroSection.kt
│   │   │   │   │   │   └── StudentPortalScreen.kt
│   │   │   │   │   ├── admin/                       # Admin dashboard components
│   │   │   │   │   │   ├── AddEventModal.kt
│   │   │   │   │   │   ├── AdminDashboardScreen.kt
│   │   │   │   │   │   ├── CategoryBreakdownSection.kt
│   │   │   │   │   │   ├── EventUtilizationSection.kt
│   │   │   │   │   │   ├── MetricCard.kt
│   │   │   │   │   │   └── RegistrationsSection.kt
│   │   │   │   │   └── viewmodel/
│   │   │   │   │       ├── AdminViewModel.kt
│   │   │   │   │       └── StudentViewModel.kt
│   │   │   │   └── navigation/
│   │   │   │       └── ZenithNavigation.kt          # Compose Navigation Host
│   │   │   ├── res/
│   │   │   │   ├── values/                          # strings.xml, colors.xml, themes.xml
│   │   │   │   └── xml/network_security_config.xml  # Cleartext HTTP for dev server
│   │   │   └── AndroidManifest.xml                  # Permissions & activity declarations
│   │   └── test/java/com/zenith26/fest/
│   │       └── EventModelTest.kt                    # Unit tests for models & serialization
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── gradle/wrapper/gradle-wrapper.properties
├── build.gradle.kts
├── settings.gradle.kts
├── gradlew
├── gradlew.bat
└── README.md
```

---

## 3. Required Software & Prerequisites

1. **Android Studio**: Android Studio Hedgehog (2023.1.1) / Iguana (2023.2.1) or newer.
2. **Java Development Kit**: JDK 17 (bundled automatically with Android Studio, or Temurin/OpenJDK 17).
3. **Python**: Python 3.10+ (to run the existing FastAPI backend).
4. **Android SDK**: API level 34 build tools & platform.

---

## 4. How to Run the FastAPI Backend

Before launching the Android app, start the FastAPI server so the app can communicate with the live SQLite database:

```powershell
# Open terminal in project root
cd "c:\Users\HP\OneDrive\Desktop\activiety 2"

# Start the server listening on all network interfaces (port 8001)
.\.venv\Scripts\python.exe -m uvicorn backend.main:app --reload --host 0.0.0.0 --port 8001
```

Confirm the backend is running:
- Student Portal: [http://localhost:8001/](http://localhost:8001/)
- Admin Dashboard: [http://localhost:8001/admin.html](http://localhost:8001/admin.html)
- Swagger Docs: [http://localhost:8001/docs](http://localhost:8001/docs)

---

## 5. How to Open and Run the Android App

### Step 1: Open in Android Studio
1. Launch Android Studio.
2. Select **Open** and choose the `Zenith26` folder (`c:\Users\HP\OneDrive\Desktop\activiety 2\Zenith26`).
3. Allow Android Studio to complete Gradle sync.

### Step 2: Configure API Base URL
- **Standard Android Emulator**: The default base URL is `http://10.0.2.2:8001/`. Android emulators map `10.0.2.2` to the host computer's `localhost`.
- **Physical Android Device**: Connect your phone and PC to the same Wi-Fi network. Find your PC's IP address (`ipconfig` on Windows, e.g., `192.168.1.5`). Click the ⚙️ icon in the app header and set the base URL to `http://192.168.1.5:8001/`.

### Step 3: Run the App
Select your emulator or connected device in Android Studio and press **Run 'app'** (`Shift + F10`).

---

## 6. How to Build the Debug APK

To build the debug APK using the Gradle wrapper from the terminal:

```powershell
cd "c:\Users\HP\OneDrive\Desktop\activiety 2\Zenith26"
.\gradlew assembleDebug
```

The compiled APK will be output to:
`app/build/outputs/apk/debug/app-debug.apk`

### Release APK Signing
To generate a signed release APK:
```powershell
.\gradlew assembleRelease
```
Ensure a production keystore is configured in `app/build.gradle.kts` (`signingConfigs`) before creating production release builds. Do not ship with default debug keys.

---

## 7. How to Run Tests

### A. Run End-to-End Backend Verification Suite
Runs all 11 backend, database, validation, and mounting tests:
```powershell
cd "c:\Users\HP\OneDrive\Desktop\activiety 2"
.\.venv\Scripts\python.exe test_e2e_backend.py
```
**Result:** 11/11 tests pass (100% pass rate).

### B. Run Android Kotlin Unit Tests
```powershell
cd "c:\Users\HP\OneDrive\Desktop\activiety 2\Zenith26"
.\gradlew test
```

---

## 8. Known Limitations & Technical Documentation
1. **Host Environment JDK Absence**: The host Windows machine does not have Android Studio or standalone JDK 17 pre-installed in the global environment; developers must open the `Zenith26` project inside Android Studio where the bundled JDK and SDK manager are provided.
2. **Cleartext HTTP**: Cleartext HTTP is permitted in development via `network_security_config.xml` for `10.0.2.2` and local development IP addresses. Production deployments should use an HTTPS domain with SSL certificates.
3. **Public Admin Dashboard**: In keeping with the original web design scope, the Admin Dashboard does not require authentication; access is open to coordinators on the local network.
