# Auto-Audio Orchestrator Development Cycle Guidelines

This document outlines the required skills, system instructions, and checkpoints to build the Android 16 (API 36) Auto-Audio Orchestrator application.

## 1. Relevant Skills & Tech Stack

To successfully develop this application, the following specific skills and technologies are essential:

### Core Android Development & UI
*   **Target SDK:** Android 16 (API 36).
*   **Language:** Kotlin.
*   **UI Framework:** Modern Jetpack Compose for a lightweight, single-screen interface.

### Background Processing & Lifecycle
*   **BroadcastReceivers:** Understanding `UiModeManager.ACTION_ENTER_CAR_MODE` (or AndroidX Car App Library equivalents) to detect car connections.
*   **Services:** Implementing lightweight Foreground Services compliant with Android 16 constraints (including `FOREGROUND_SERVICE` and `POST_NOTIFICATIONS` permissions).
*   **Concurrency:** Kotlin Coroutines to handle exact timing delays off the main thread (e.g., `delay(3000)`).

### Media & Storage
*   **Media Routing:** `MediaPlayer` usage, configuring `AudioAttributes` (`USAGE_MEDIA`, `CONTENT_TYPE_MUSIC`), and requesting AudioFocus (`AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK`).
*   **Storage & Permissions:** `DataStore` (Preferences) for state persistence. Mastering Scoped Storage, `ActivityResultContracts.OpenDocument`, and crucial persistable URI permissions (`takePersistableUriPermission`) to survive device reboots.

---

## 2. System Instructions (Developer Guidelines)

When implementing the Auto-Audio Orchestrator, adhere to the following architecture and execution logic:

### UI Layer Requirements
*   **Single Home Screen:** Built entirely in Jetpack Compose.
*   **Master Toggle:** An "Enable/Disable" switch whose state is instantly saved via DataStore.
*   **Audio Picker:** A button utilizing `ActivityResultContracts.OpenDocument` (mime type `audio/*`) to select an mp3/wav file.
*   **Status Display:** A text label clearly showing the currently selected audio file name.

### Storage & State Management
*   **DataStore Preferences:** Used exclusively to persist the boolean state of the Master Toggle and the String URI of the selected audio file.
*   **Reboot Resilience:** You **must** take persistable URI permissions immediately upon file selection to prevent `SecurityException` crashes in the future.

### Core Execution Logic (Background Service)
*   **Detection:** Listen for the Android Auto connection intent.
*   **The Gatekeeper:** Upon receiving the intent, verify the Master Toggle is `true` and the URI is not `null`. If either fails, terminate the operation.
*   **The Delay:** Execute a precise 3000-millisecond delay using Coroutines within a scope that outlives the broadcast receiver but respects system constraints.
*   **Audio Execution:** 
    1. Instantiate a `MediaPlayer`.
    2. Set AudioAttributes to route through the car's media pipe.
    3. Request transient AudioFocus.
    4. Play the URI.
    5. Release the `MediaPlayer` immediately upon completion.

---

## 3. Development Checkpoints (The Cycle)

Follow these checkpoints to ensure the application meets all Android 16 requirements:

### Checkpoint 1: UI & Storage Setup
*   [ ] Implement the Jetpack Compose single-screen UI.
*   [ ] Integrate `DataStore` to persist the toggle state.
*   [ ] Implement the `OpenDocument` launcher and successfully grab/persist the audio file URI.
*   [ ] **CRITICAL:** Verify `takePersistableUriPermission` is functioning correctly (test by rebooting the device and re-reading the URI).

### Checkpoint 2: Service & Manifest Configuration
*   [ ] Define the required Android 16 permissions in `AndroidManifest.xml` (`FOREGROUND_SERVICE`, `POST_NOTIFICATIONS`, etc.).
*   [ ] Create the Foreground Service and ensure the silent "Auto-Audio Active" notification displays correctly.
*   [ ] Register the BroadcastReceiver for `ACTION_ENTER_CAR_MODE`.

### Checkpoint 3: Execution Logic & Media Routing
*   [ ] Implement the DataStore read-check within the connection trigger.
*   [ ] Implement the 3-second Coroutine delay.
*   [ ] Configure `MediaPlayer` with the strict AudioAttributes required for Android Auto routing.
*   [ ] Test audio focus requests and playback release.

### Checkpoint 4: Final QA & Android 16 Validation
*   [ ] Test the full flow: Connect to Android Auto (or DHU) -> Verify 3-second delay -> Verify audio plays over car speakers.
*   [ ] Ensure no crashes occur due to Android 16 background execution limits or scoped storage violations.
*   [ ] Code review for clean, production-ready Kotlin.
