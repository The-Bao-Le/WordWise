# WordWise

WordWise is an Android academic vocabulary learning app designed for
university students who use English as an additional language. It combines
short quiz sessions, spaced review, local progress tracking and live dictionary
lookups to support vocabulary recall without requiring an account.

## Project Status

The core implementation is complete. The project is currently undergoing final
testing, documentation and submission checks.

## Core Features

- Short multiple-choice academic vocabulary sessions
- Five- or ten-question session options
- Immediate answer feedback and example sentences
- Spaced-review scheduling based on learning progress
- Ten locally seeded academic vocabulary entries
- Persistent statistics, including accuracy, completed sessions, attempted
  words, mastered words and words due for review
- Configurable session and display preferences
- Light, dark and system-default theme support
- Resettable learning progress with a confirmation step
- Live English definitions, phonetics, examples and synonyms
- Clear handling of empty searches, unknown words and network failures
- Accessible loading, error and navigation feedback

## Screens and Navigation

WordWise uses Navigation Compose with four primary bottom-navigation
destinations. The Dictionary Explorer is a secondary route opened from Home so
the main navigation remains focused.

| Screen | Purpose |
| --- | --- |
| Home | Summarises current learning progress and starts a review session. It also provides access to Dictionary Explorer. |
| Practice | Presents vocabulary questions, records answers and provides immediate feedback. |
| Statistics | Displays progress calculated from locally stored learning data. |
| Settings | Manages session, display and theme preferences, explains data handling and allows progress to be reset. |
| Dictionary Explorer | Searches the external dictionary service and displays structured word information. |

## Architecture

The project follows a layered Android architecture:

- **UI layer:** Jetpack Compose and Material Design 3 screens
- **State management:** lifecycle-aware ViewModels, StateFlow and immutable UI
  state
- **Domain layer:** app-specific models, repository interfaces and learning
  logic
- **Data layer:** Room, Preferences DataStore and Retrofit implementations
- **Dependency injection:** Hilt modules and constructor injection
- **Navigation:** Navigation Compose with lifecycle-aware state collection

Repository interfaces separate the UI and domain logic from local storage and
network implementations. This keeps Android framework and Retrofit response
types out of the core learning logic and makes the main behaviours easier to
test.

## Local Data

### Room database

Room stores vocabulary content and persistent learning progress. The app starts
with ten seeded academic words, updates review information after practice and
derives statistics from the stored results. Core learning sessions remain
available without an internet connection.

### Preferences DataStore

Preferences DataStore stores user settings such as session length,
example-sentence visibility and theme selection. Resetting learning progress
clears the relevant Room progress data while retaining these preferences.

## External API

WordWise uses the
[Free Dictionary API](https://dictionaryapi.dev/) to retrieve live English
definitions, phonetics, examples, synonyms, source details and licensing
information.

```text
GET https://api.dictionaryapi.dev/api/v2/entries/en/{word}
```

Retrofit and Gson handle the network request and nested JSON response. API data
is mapped from nullable data-transfer objects into app-specific domain models
before it reaches the UI. No API key or user account is required.

Dictionary Explorer requires an internet connection. The rest of the learning
experience uses local content and remains available offline.

## Data, Privacy and Ethical Design

- WordWise does not require an account or collect personally identifiable
  information.
- Vocabulary progress and preferences are stored on the user's device.
- A dictionary search term is sent to the Free Dictionary API only after the
  user initiates a search.
- WordWise does not save dictionary search history.
- The app explains local storage and external data transfer in its interface.
- Resetting progress requires confirmation, supporting user control and
  preventing accidental data loss.
- The design avoids unnecessary permissions, advertising, behavioural tracking
  and manipulative engagement patterns.

The Android app declares internet and network-state permissions only for live
dictionary searches.

## Accessibility and Responsive Design

The Compose interface uses scrollable layouts, Material colour roles and
descriptive text instead of relying on colour alone. Icon-only controls include
content descriptions, result headings use semantic heading information, and
loading or error states provide screen-reader-friendly feedback. The interface
is designed for light and dark themes, different screen orientations and
increased system font sizes.

## Technology

- Kotlin
- Jetpack Compose
- Material Design 3
- Navigation Compose
- Android ViewModel and StateFlow
- Room
- Preferences DataStore
- Retrofit and Gson
- Hilt
- Kotlin coroutines
- JUnit
- AndroidX Room and Compose UI testing

## Testing

The project includes non-GUI and Compose UI tests covering:

- Learning-progress and statistics calculations
- Room database operations
- DataStore preference persistence
- Dictionary response mapping and repository behaviour
- Practice and Settings screen interactions
- Dictionary text entry and search actions

Run local unit tests:

```bash
./gradlew testDebugUnitTest
```

Run instrumented Room and Compose tests on an emulator or connected device:

```bash
./gradlew connectedDebugAndroidTest
```

Run Android lint and create a debug build:

```bash
./gradlew lintDebug
./gradlew assembleDebug
```

On Windows, use `gradlew.bat` in place of `./gradlew`.

## Requirements

- Android Studio
- Android SDK 26 or later
- An Android emulator or physical Android device
- Internet access for Dictionary Explorer only

## Running the Project

1. Clone the repository.
2. Open the project in Android Studio.
3. Allow Gradle to synchronise and download the required dependencies.
4. Select an emulator or connected Android device.
5. Run the `app` configuration.

No API key, account or additional service configuration is required.

## Known Limitations

- The built-in learning dataset currently contains ten academic words.
- Learning progress is local to one device and is not backed up or synchronised.
- Dictionary Explorer supports English word lookups only.
- Dictionary searches require an internet connection and depend on the
  availability and completeness of the external API response.
- Dictionary results are not cached and pronunciation audio is not currently
  played in the app.

## Development Summary

- Local vocabulary learning, Room persistence and progress statistics completed
- Hilt-based architecture and repository separation completed
- DataStore settings and theme persistence completed
- Retrofit-based Free Dictionary API integration and DTO-to-domain mapping
  completed
- Accessibility, privacy transparency, automated testing and
  submission-readiness improvements completed
