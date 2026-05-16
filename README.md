# CarPlay

A modular Android "super-app" targeting Android 16 (API 36), designed to interface with Android Auto and Android Automotive systems.

The application acts as a central hub (Dashboard) for various car-related sub-apps and features, providing a scalable architecture for adding new functionalities.

## Architecture

*   **Modular Super-App:** The app uses a single `:app` module with clean package separation (`core`, `navigation`, `ui`, `feature`). This enables easy scalability without the overhead of a multi-module Gradle build.
*   **UI Layer:** Built purely with modern Jetpack Compose, featuring a premium dark-first automotive theme to reduce glare while driving.
*   **Navigation:** Uses Jetpack Navigation Compose, routing from a central Dashboard grid to independent feature screens.
*   **Dependency Injection:** Manual DI via the `CarPlayApplication` class for simplicity and transparency.
*   **Storage:** Shared `DataStore<Preferences>` for persistent state.

## Current Features

### 1. Auto-Audio Orchestrator
Automatically plays a user-selected audio file over the car's speakers when the phone connects to Android Auto.
*   **Detection:** Uses the `CarConnection` API as the primary detector, with an `ACTION_ENTER_CAR_MODE` BroadcastReceiver as a fallback for standard car docks.
*   **Execution:** A Foreground Service (`FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK`) waits exactly 3 seconds to allow physical car amplifier relays to initialize before requesting AudioFocus and playing the media.
*   **Storage Resilience:** Uses `takePersistableUriPermission` to ensure the app retains access to the user's selected audio file even after device reboots.

## Adding a New Feature

The app is built to be easily extensible. To add a new sub-app:
1. Create a new package under `app/src/main/java/com/carplay/feature/yourfeature/`.
2. Define the navigation route in `Screen.kt`.
3. Add the composable entry in `AppNavGraph.kt`.
4. Add a `FeatureItem` card to the grid in `DashboardScreen.kt`.

## Getting Started

*   **Requirements:** Android Studio Meerkat (or newer) supporting Android Gradle Plugin 8.9.0+ and API level 36.
*   **Build:** Open the project in Android Studio. The IDE will automatically synchronize dependencies and generate the required Gradle Wrapper binary. Run `./gradlew assembleDebug` to build.
