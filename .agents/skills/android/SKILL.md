---
name: modern-android-dev
description: Guidelines for building high-quality Android apps using Jetpack Compose, MVVM, and Kotlin best practices.
metadata:
  version: "1.0"
  author: "Akhzar Farhan"
---

# Modern Android Development Skill

Use this skill when creating new UI components, implementing business logic, or refactoring legacy Android code.

## Tech Stack & Architecture
- **Language**: Kotlin with Coroutines and Flow.
- **UI Framework**: Jetpack Compose (no XML unless specified).
- **Architecture**: MVVM or MVI with Unidirectional Data Flow (UDF).
- **Dependency Injection**: Hilt or Koin.
- **Networking**: Retrofit with Kotlinx Serialization.
- **Database**: Room for offline-first capabilities.

## Development Rules

### 1. State Management
- Use `ViewModel` to hold state. 
- Expose state as `StateFlow` and events as `SharedFlow` or `Channel`.
- Collect state in Compose using `.collectAsStateWithLifecycle()`.

### 2. UI & Design
- Follow [Material 3 Design Systems](https://material.io) for colors, typography, and shapes.
- Ensure all components are accessible (TalkBack support, semantic properties).
- Optimize for large screens and foldables using adaptive layouts.

### 3. Clean Code & Quality
- **Naming**: Use camelCase for functions and PascalCase for Composables.
- **Validation**: Use `ktlint` or `detekt` for static analysis.
- **Testing**: Prioritize Unit tests with JUnit5 and UI tests with Compose Rule.

## Workflows
- **New Feature**: Define the Data Layer (DTOs/Repository) -> Domain Layer (UseCases) -> UI Layer (ViewModel/Compose).
- **Refactor**: Convert XML layouts to Jetpack Compose by mapping attributes to Modifier chains.
- **CLI Usage**: Use the [Android CLI](https://android.com) for environment setup and emulator management.

## References
- [Official Android Developer Documentation](https://android.com).
- [Android Skills GitHub Repository](https://github.com/android/skills).
