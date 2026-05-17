# AGENTS.md — LLM Agent Instructions for CarPlay

> **Audience:** AI coding assistants (Gemini, Claude, GPT, Copilot, Cursor, etc.)  
> **Scope:** Every code generation, refactor, or review task in this repository.  
> **Authority:** Treat this document as the single source of truth for project conventions. When in doubt, match existing code — never invent a new pattern.

---

## 1. Project Identity

| Key | Value |
|---|---|
| **Name** | CarPlay |
| **Type** | Android native super-app (single `:app` module) |
| **Language** | Kotlin (100 %) |
| **Min SDK** | 26 |
| **Target / Compile SDK** | 36 (Android 16) |
| **UI** | Jetpack Compose + Material 3 |
| **DI** | Manual DI via `CarPlayApplication` (no Hilt / Koin / Dagger) |
| **Storage** | DataStore Preferences (local), Firebase Realtime Database (remote) |
| **Build** | Single-module Gradle (version catalog `libs.versions.toml`) |
| **Theme** | Dark-first automotive theme (anti-glare priority) |

---

## 2. Architecture Rules

### 2.1 Package Structure (non-negotiable)

```
com.carplay/
├── CarPlayApplication.kt        ← Manual DI container
├── MainActivity.kt              ← Single Activity, edge-to-edge
├── core/
│   └── datastore/
│       └── AppDataStore.kt      ← Shared DataStore singleton
├── feature/
│   ├── autoaudio/               ← Feature: Auto-Audio Orchestrator
│   │   ├── AudioPlayerService.kt
│   │   ├── AutoAudioDetectionService.kt
│   │   ├── AutoAudioNotificationListener.kt
│   │   ├── AutoAudioPreferences.kt
│   │   ├── AutoAudioStatusRepository.kt
│   │   ├── CarModeReceiver.kt
│   │   └── DiagnosticsRepository.kt
│   └── obd/                     ← Feature: OBD-II Diagnostics
│       ├── DtcDecoder.kt
│       ├── ObdHistoryRepository.kt
│       ├── ObdManager.kt
│       ├── ObdScreen.kt
│       └── ObdViewModel.kt
├── navigation/
│   ├── Screen.kt                ← Sealed route definitions
│   └── AppNavGraph.kt           ← Central nav graph + ViewModelFactories
└── ui/
    ├── dashboard/
    │   └── DashboardScreen.kt   ← Hub grid of FeatureItem cards
    └── theme/
        ├── Color.kt             ← Design tokens (dark palette)
        ├── Theme.kt             ← CarPlayTheme composable
        └── Type.kt              ← Typography scale
```

**Rules:**
- New features go under `feature/<featurename>/`. Never place feature code in `core/`, `ui/`, or `navigation/`.
- Shared infrastructure (DataStore, networking, etc.) goes under `core/`.
- Theme-related code stays in `ui/theme/`. Dashboard-level UI stays in `ui/dashboard/`.
- Navigation wiring lives **only** in `navigation/`.

### 2.2 Adding a New Feature — The Checklist

Every new feature **must** follow these four steps, in this exact order:

1. **Package:** Create `feature/<featurename>/` with Screen, ViewModel (if needed), Repository (if needed), and any services.
2. **Route:** Add a `data object` to the sealed class in `Screen.kt`.
3. **NavGraph:** Add a `composable()` entry in `AppNavGraph.kt`. If the feature has a ViewModel, add a `ViewModelFactory` at the top of that file.
4. **Dashboard:** Add a `FeatureItem` to the grid list in `DashboardScreen.kt`.

Do **not** skip any step. Do **not** add navigation logic inside feature screens.

---

## 3. Dependency Injection — Manual DI Only

```kotlin
// CarPlayApplication.kt
class CarPlayApplication : Application() {
    lateinit var myNewRepository: MyNewRepository
        private set

    override fun onCreate() {
        super.onCreate()
        myNewRepository = MyNewRepository(dataStore)
    }
}
```

**Rules:**
- **Never** add Hilt, Koin, Dagger, or any DI framework.
- All singletons are `lateinit var` properties on `CarPlayApplication` with `private set`.
- Initialize them in `onCreate()` in dependency order.
- Access from Activities/Services: `(application as CarPlayApplication).myProperty`.
- Pass dependencies explicitly through composable function parameters — never use `LocalComposition` for DI.

---

## 4. Kotlin & Compose Conventions

### 4.1 Language Style
- Use **Kotlin idioms**: `data class`, `sealed class`, `object`, `when`, extension functions.
- Prefer `val` over `var`. Use `MutableStateFlow` / `StateFlow` for observable state.
- Coroutines: always use structured concurrency. Service scopes use `SupervisorJob()`.
- Never use `GlobalScope`.
- String resources: use `@string/` references for all user-facing text. Internal log tags can be hardcoded.

### 4.2 Compose Patterns
- **State hoisting:** Screens receive state via parameters, not by reaching into singletons.
- **`collectAsState()`:** For observing `StateFlow` / `Flow` in composables.
- **`rememberCoroutineScope()`:** For one-off suspend calls in UI event handlers.
- **`produceState`:** For async resolution of derived values (e.g., resolving a URI to a filename).
- Screen composables take dependency parameters + an `onNavigateBack: () -> Unit` lambda. Navigation is handled externally in `AppNavGraph`.
- Never call `navController` from inside a feature screen.
- Use `MaterialTheme.colorScheme.*` and `MaterialTheme.typography.*` — never hardcode colors or text styles in feature code (except severity-specific colors like DTC badges).

### 4.3 ViewModel Pattern
- ViewModels expose a single `StateFlow<UiState>` where `UiState` is a `data class`.
- ViewModels receive repositories/managers via constructor parameters — never via `Application` cast.
- Factory pattern: define `ViewModelProvider.Factory` in `AppNavGraph.kt`, co-located with the `composable()` block.
- Use `viewModelScope.launch { }` for all async ViewModel operations.
- All exceptions must be caught and surfaced as `errorMessage` in the UI state.

---

## 5. Storage & Persistence

### 5.1 DataStore Preferences
- Single `AppDataStore` wraps the context-delegated `DataStore<Preferences>`.
- Feature-specific preference classes (e.g., `AutoAudioPreferences`) take `AppDataStore` and namespace their keys with a prefix (e.g., `auto_audio_`).
- Expose reactive data as `Flow<T>`. Provide a `suspend fun readSync()` for service/receiver use cases.
- Never create a second `preferencesDataStore` delegate — reuse `AppDataStore`.

### 5.2 Firebase Realtime Database
- Database URL: `https://my-own-ubiverse-default-rtdb.firebaseio.com/`
- Path pattern: `CarPlay/akhzarfarhan/<feature_data_key>`
- Always wrap Firebase initialization in `try/catch` — the database may not be configured on all builds.
- Use `push().key` for auto-generated IDs.
- Firebase writes are fire-and-forget with `addOnFailureListener` logging — never block UI on writes.
- For reads, use `callbackFlow` + `ValueEventListener` + `awaitClose`.

---

## 6. Android 16 (API 36) Compliance — Critical

### 6.1 Foreground Services
- Any foreground service **must** call `startForeground()` immediately in `onStartCommand()` — before any async work.
- Always specify `foregroundServiceType` in both the manifest and the `startForeground()` call.
- Use `START_NOT_STICKY` for one-shot services, `START_STICKY` for persistent services.
- Notifications: use `NotificationCompat.Builder`, silent (`setSilent(true)`), ongoing (`setOngoing(true)`).

### 6.2 Permissions
- Declare all required permissions in `AndroidManifest.xml` with API-level guards (`android:maxSdkVersion`).
- Request `POST_NOTIFICATIONS` at runtime on API 33+ (check in `MainActivity`).
- Bluetooth: request `BLUETOOTH_CONNECT` on API 31+, fall back to `BLUETOOTH` on lower.
- Use `accompanist-permissions` for Compose-based runtime permission flows.

### 6.3 Scoped Storage & URI Persistence
- Use `ActivityResultContracts.OpenDocument()` — never `ACTION_GET_CONTENT`.
- Immediately call `takePersistableUriPermission(uri, FLAG_GRANT_READ_URI_PERMISSION)` after selection.
- Persist the URI string in DataStore. Reconstruct with `Uri.parse()` when needed.
- Never assume file system paths — always go through `ContentResolver`.

---

## 7. Service & Receiver Patterns

### 7.1 BroadcastReceivers
- Register in manifest for system intents (`ACTION_ENTER_CAR_MODE`, third-party intents).
- Access `CarPlayApplication` via `context.applicationContext as CarPlayApplication`.
- Keep `onReceive()` fast — delegate heavy work to a foreground service via `startForegroundService()`.
- Log all received broadcasts for diagnostics.

### 7.2 NotificationListenerService
- Requires `BIND_NOTIFICATION_LISTENER_SERVICE` permission in manifest.
- User must manually enable via system settings — provide a button to navigate there.
- Check access: `Settings.Secure.getString(contentResolver, "enabled_notification_listeners")`.

### 7.3 Service Lifecycle
- Services use their own `CoroutineScope(Dispatchers.Default + SupervisorJob())`.
- Cancel the scope in `onDestroy()`.
- Release all resources (`MediaPlayer`, `AudioFocus`, sockets) in `onDestroy()` and on errors.

---

## 8. Build System Rules

### 8.1 Dependencies
- All dependency versions live in `gradle/libs.versions.toml` — **never** hardcode versions in `build.gradle.kts`.
- Use `implementation(libs.xxx)` aliases. If a library is not in the catalog, add it to the TOML first.
- Exception: Firebase KTX extensions may use string notation when BOM manages the version.

### 8.2 Build Configuration
- Java 17 source/target compatibility.
- Compose is enabled via `buildFeatures { compose = true }` + the Kotlin Compose plugin.
- Release builds: `isMinifyEnabled = true` with ProGuard.
- Single `:app` module — do **not** create new Gradle modules.

### 8.3 Version Catalog (`libs.versions.toml`)
- Group versions logically: Compose, Storage, Car, Core.
- Use `version.ref` for BOM-managed dependencies (Compose, Firebase).

---

## 9. Code Quality Standards

### 9.1 Error Handling
- Every `suspend` operation in ViewModels must be wrapped in `try/catch`.
- Services: catch exceptions in the main coroutine, log with `Log.e(TAG, ...)`, update status, and call `stopSelf()`.
- Never silently swallow exceptions — at minimum log them.
- Use `AudioStatus.ERROR` (or equivalent enum) with a detail message for user-facing errors.

### 9.2 Logging
- Use Android `Log` class with a `TAG` companion constant.
- In-app diagnostics: use `DiagnosticsRepository.log(tag, message)` for runtime event tracing.
- Firebase logging: log status transitions to `audio_status_logs` (or feature-appropriate path).

### 9.3 Naming Conventions
| Element | Convention | Example |
|---|---|---|
| Package | `feature.<featurename>` | `feature.obd` |
| Screen composable | `<Feature>Screen` | `ObdScreen` |
| ViewModel | `<Feature>ViewModel` | `ObdViewModel` |
| UI State | `<Feature>UiState` | `ObdUiState` |
| Repository | `<Feature>Repository` or `<Feature>Preferences` | `ObdHistoryRepository` |
| Service | `<Feature>Service` | `AudioPlayerService` |
| DataStore keys | `<feature>_<key_name>` | `auto_audio_master_toggle` |
| Firebase paths | `CarPlay/akhzarfarhan/<feature_key>` | `CarPlay/akhzarfarhan/obd_history` |
| Nav routes | `snake_case` strings | `"obd_diagnostics"` |

### 9.4 Documentation
- Every public class must have a KDoc comment explaining its purpose.
- Complex logic (e.g., amplifier relay delay, DTC parsing) must have inline comments.
- Keep `README.md` and `DEVELOPMENT_CYCLE.md` in sync with feature additions.

---

## 10. Anti-Patterns — What NOT To Do

| ❌ Do NOT | ✅ Do THIS instead |
|---|---|
| Add Hilt / Koin / Dagger | Use Manual DI via `CarPlayApplication` |
| Create new Gradle modules | Add packages under `feature/` |
| Use `GlobalScope` | Use `viewModelScope` or service-scoped `CoroutineScope` |
| Hardcode colors in feature code | Use `MaterialTheme.colorScheme.*` |
| Navigate from inside a feature Screen | Pass `onNavigateBack` lambda from `AppNavGraph` |
| Hardcode dependency versions | Add to `libs.versions.toml` |
| Use `ACTION_GET_CONTENT` for files | Use `ActivityResultContracts.OpenDocument()` |
| Skip `startForeground()` in services | Call it immediately in `onStartCommand()` |
| Create light theme variants | This app is **dark-only** by design |
| Use `SharedPreferences` | Use `DataStore<Preferences>` via `AppDataStore` |
| Block the main thread with `runBlocking` | Use `suspend` functions with structured concurrency |
| Ignore exceptions in ViewModels | Catch and surface as `errorMessage` in UI state |

---

## 11. Testing & QA Expectations

- Before submitting code, ensure it compiles: `./gradlew assembleDebug`.
- Verify no Android 16 background execution or scoped storage violations.
- For services: ensure the full lifecycle is handled (start → work → cleanup → stop).
- For UI: ensure all strings come from `res/values/strings.xml`.
- For Firebase: ensure graceful degradation when Firebase is unavailable.

---

## 12. Git Workflow

- Commit messages: descriptive, present tense (e.g., "Add OBD-II history export").
- The `Makefile` provides `make push m="message"` for convenience.
- Keep `google-services.json` in `.gitignore` — it is excluded from version control.
- Never commit IDE-specific files (`.idea/workspace.xml`, `*.iml`).

---

*Last updated: 2026-05-17*
