# Jetris - A Tetris Clone for Android

![header](screenshots/header.png)

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.0-blue.svg)](https://kotlinlang.org/)
[![Compose](https://img.shields.io/badge/Compose-1.5.4-blue.svg)](https://developer.android.com/jetpack/compose)
[![API](https://img.shields.io/badge/API-27%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=26)


Jetris is a classic Tetris game clone built for Android using modern Android development tools and practices. This project showcases the use of Jetpack Compose for the UI, Kotlin for the programming language, and follows MVVM architecture principles.

## ✨ Features

*   **Classic Tetris Gameplay:** Move, rotate, and drop falling tetrominoes to clear lines.
*   **Responsive UI:** Game interface adapts to different screen sizes.
*   **Score Keeping:** Track your score as you clear lines.
*   **Line Count:** See how many lines you've successfully cleared.
*   **Next Piece Preview:** Plan your moves by seeing the upcoming tetromino.
*   **Game Over Detection:** The game ends when pieces stack to the top.
*   **Restart Functionality:** Easily start a new game.
*   **(Planned/Possible Features - Add or remove as needed)**
    *   Level progression with increasing speed.
    *   Sound effects.
    *   Persistent high scores.
    *   Piece hold functionality.
    *   Ghost piece.


## 🛠️ Tech Stack & Key Libraries

*   **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) - Android's modern toolkit for building native UI.
*   **Language:** [Kotlin](https://kotlinlang.org/) - First-party support, concise, and expressive.
*   **Architecture:** MVVM (Model-View-ViewModel)
    *   **ViewModel:** `GameViewModel` manages the game state, logic, and event handling using Kotlin Coroutines and StateFlow.
    *   **View:** Composable functions observe `StateFlow` from the ViewModel to reactively update the UI.
    *   **Model:** Data classes representing game state (e.g., `GameState`, `GamePiece`, `BlockColor`), game grid, and piece shapes.
*   **Asynchronous Programming:** [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) for managing the game loop and background tasks.
*   **State Management:** [StateFlow](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-state-flow/) for observable, lifecycle-aware state.
*   **Dependency Injection :**
    *   [Koin](https://insert-koin.io/)
*   **Build System:** Gradle

## Core Game Logic Implemented

*   **Game Grid:** 2D array representing the playfield.
*   **Tetrominoes:** Standard Tetris pieces, each defined as a 2D array of `BlockColor`.
*   **Piece Movement:** Left, right, and downward (soft drop).
*   **Piece Rotation:** Clockwise rotation of pieces.
*   **Collision Detection:** Checks for collisions with grid boundaries and other locked pieces.
*   **Line Clearing:** Detects and clears completed horizontal lines, shifting blocks above them down.
*   **Spawning:** New pieces spawn at the top of the grid.
*   **Game Loop:** Managed by Kotlin Coroutines in the `GameViewModel` to control game ticks and piece descent.
*   **Random Piece Generation:** Uses `BlockColor.randomPlayable()` to select the next piece.


## Getting Started

1.  **Clone the repository:**
2.  **Open in Android Studio:**
    *   Open Android Studio (Recommended: Latest stable version - e.g., Hedgehog, Iguana).
    *   Select "Open an Existing Project".
    *   Navigate to the cloned `Jetris` directory and select it.
3.  **Build the project:**
    *   Android Studio should automatically sync and build the project using Gradle.
4.  **Run the app:**
    *   Select an emulator or connect an Android device.
    *   Click the "Run" button in Android Studio.

**Prerequisites:**
* Android Studio (latest stable version recommended - e.g., Iguana | 2023.2.1 or newer)
* Android SDK
* Min SDK 28 (Android 9)

## Project Structure (Key Components)

## Acknowledgements

Font: [Planet Benson - Typodermic Fonts](https://www.1001freefonts.com/designer-typodermic-fonts-fontlisting.php)

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact
Donald McCaskey - [forteanjo@sky.com](mailto:forteanjo@sky.com)
