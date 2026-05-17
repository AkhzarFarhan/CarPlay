# Copilot Instructions — CarPlay

Refer to `AGENTS.md` in the project root for the complete agent rulebook.

## Quick Reference

- **Kotlin + Jetpack Compose + Material 3**, targeting Android 16 (API 36).
- **Single `:app` module** with package-based feature separation under `feature/<name>/`.
- **Manual DI** via `CarPlayApplication` — no Hilt, Koin, or Dagger.
- **Dark-only theme** using `CarPlayTheme` with `MaterialTheme.colorScheme.*` tokens.
- **DataStore Preferences** for local storage, **Firebase RTDB** for remote.
- All dependency versions in `gradle/libs.versions.toml`.

## Feature Addition Protocol

1. Create `feature/<name>/` package with Screen, ViewModel, Repository.
2. Add `data object` route in `navigation/Screen.kt`.
3. Wire `composable()` in `navigation/AppNavGraph.kt`.
4. Add `FeatureItem` card in `ui/dashboard/DashboardScreen.kt`.

## Key Constraints

- Foreground services must call `startForeground()` immediately in `onStartCommand()`.
- Use `OpenDocument` + `takePersistableUriPermission` for file access.
- Never use `GlobalScope`, `runBlocking`, `SharedPreferences`, or light themes.
- Wrap all ViewModel suspend calls in try/catch, surface errors in UI state.
- Pass navigation via lambdas — never call `navController` from feature screens.
