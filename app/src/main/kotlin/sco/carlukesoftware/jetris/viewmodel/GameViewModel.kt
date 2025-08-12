package sco.carlukesoftware.jetris.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import sco.carlukesoftware.jetris.data.BlockColor
import sco.carlukesoftware.jetris.data.GamePiece
import sco.carlukesoftware.jetris.data.GameState
import sco.carlukesoftware.jetris.utils.GRID_COLUMNS
import sco.carlukesoftware.jetris.utils.GameGrid
import sco.carlukesoftware.jetris.utils.clearShapeFromGrid
import sco.carlukesoftware.jetris.utils.deepCopy
import sco.carlukesoftware.jetris.utils.emptyNextBlockGrid
import sco.carlukesoftware.jetris.utils.hasCollision
import sco.carlukesoftware.jetris.utils.mergeAndCenterShape
import sco.carlukesoftware.jetris.utils.mergeShapeIntoGrid
import sco.carlukesoftware.jetris.utils.rotateClockwise
import sco.carlukesoftware.jetris.utils.toBlockShape
import kotlin.coroutines.cancellation.CancellationException

class GameViewModel : ViewModel() {

    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private var gameLoopJob: Job? = null

    // Used to show the next piece in the UI
    private val _nextBlockGrid = MutableStateFlow(emptyNextBlockGrid)
    val nextBlockGrid: StateFlow<GameGrid> = _nextBlockGrid.asStateFlow()

    init {
        spawnNewPiece() // Spawn the first piece
        prepareNextPieceDisplay()
        startGameLoop()
    }

    private fun prepareNextPieceDisplay() {
        val nextPieceTemplate = _gameState.value.nextPieceColor.toBlockShape()
        _nextBlockGrid.value = emptyNextBlockGrid.mergeAndCenterShape(
            shape = nextPieceTemplate.map { rowArray ->
                rowArray.map { cellColor ->
                    if (cellColor != BlockColor.EMPTY) _gameState.value.nextPieceColor else BlockColor.EMPTY
                }.toTypedArray()
            }.toTypedArray(), // Colorize the shape
            emptyColor = BlockColor.EMPTY
        )
    }


    fun startGameLoop() {
        gameLoopJob?.cancel() // Cancel any existing loop
        gameLoopJob = viewModelScope.launch {
//            // Collect the game state
//            _gameState
//                .takeWhile { !it.isGameOver && isActive } // Continue as long as not game over and coroutine active
//                .collect { currentGameState -> // This block executes on each emission (or initial value)
//                    delay(currentGameState.gameSpeedMillis)
//                    // Ensure still not game over after delay (if game over can happen asynchronously)
//                    // and coroutine is still active
//                    if (isActive && !_gameState.value.isGameOver) { // Re-check volatile state
//                        onGameTick()
//                    }
//                }
//        }
            try {
                // 'isActive' here is 'kotlinx.coroutines.isActive'
                // It correctly checks if this launched coroutine is still active.
                while (isActive && !_gameState.value.isGameOver) {
                    val currentGameSpeed = _gameState.value.gameSpeedMillis // Capture speed for this tick
                    delay(currentGameSpeed)

                    // The 'isActive' in the while condition handles cancellation during delay.
                    // Re-check 'isGameOver' because it might have changed due to an async player action
                    // or another event while this coroutine was delaying.
                    if (!_gameState.value.isGameOver) {
                        onGameTick()
                    }
                }
            } catch (e: CancellationException) {
                // Log cancellation or perform specific cleanup for the game loop
                println("Game loop cancelled.")
                throw e // Re-throw is important to ensure proper coroutine cancellation
            } finally {
                // Any cleanup that must always happen, regardless of how the loop exits
            }
        }
    }


    private fun onGameTick() {
        // This is where the piece automatically moves down one step
        movePiece(0, 1) // deltaCol = 0, deltaRow = 1 (move down)
    }

    fun resetGame() {
        _gameState.value = GameState() // Reset to initial state
        spawnNewPiece()
        prepareNextPieceDisplay()
        startGameLoop() // Restart the loop
    }

    private fun spawnNewPiece() {
        val currentGameState = _gameState.value
        if (currentGameState.isGameOver) return

        val pieceColor = currentGameState.nextPieceColor
        val pieceShapeTemplate = pieceColor.toBlockShape() // Get the template shape (often just 0s and 1s or generic BlockColor)

        // Colorize the piece shape (important if toBlockShape() returns a generic template)
        val actualPieceShape = pieceShapeTemplate.map { rowArray ->
            rowArray.map { cell ->
                if (cell != BlockColor.EMPTY) pieceColor else BlockColor.EMPTY
            }.toTypedArray()
        }.toTypedArray()

        // Calculate spawn position (centered horizontally, typically starting above visible grid or at the very top)
        val spawnCol = (GRID_COLUMNS - actualPieceShape[0].size) / 2
        val spawnRow = 0 // Or -actualPieceShape.height if you allow spawning off-screen initially

        val newPiece =
            GamePiece(shape = actualPieceShape, color = pieceColor, row = spawnRow, col = spawnCol)

        // Check for game over (collision immediately upon spawn)
        if (currentGameState.grid
            .hasCollision(
                shape = newPiece.shape,
                potentialTopRow = newPiece.row,
                potentialLeftCol = newPiece.col
            )
            ) {
            _gameState.update { gameState ->
                gameState.copy(isGameOver = true)
            }
            gameLoopJob?.cancel()
            return
        }

        // Merge the new piece onto the grid
        val gridWithNewPiece = currentGameState.grid
            .mergeShapeIntoGrid(
                shape = newPiece.shape,
                topRow = newPiece.row,
                leftCol = newPiece.col
            )

        val nextRandomColor = BlockColor.randomPlayable()
        _gameState.update { gameState ->
            gameState.copy(
                grid = gridWithNewPiece,
                currentPiece = newPiece,
                nextPieceColor = nextRandomColor
            )
        }
        prepareNextPieceDisplay() // Update the "next piece" display
    }

    /**
     * Attempts to move the current piece on the game grid by the specified delta column and row.
     *
     * If the game is over or there's no current piece, the function returns immediately.
     * It calculates the potential new position of the piece.
     *
     * The process involves:
     * 1. Creating a temporary representation of the grid with the current piece cleared from its old position.
     * 2. Checking if the piece, at its potential new position, would collide with existing blocks or grid boundaries
     *    on this temporary grid.
     * 3. If no collision is detected:
     *    a. The piece is merged into the temporary grid at its new position.
     *    b. The game state is updated with this new grid and the piece's updated coordinates.
     * 4. If a collision is detected:
     *    a. If the collision occurred while the piece was moving downwards (deltaRow > 0),
     *       the piece is locked in its current position (on the grid *before* this attempted move),
     *       lines are cleared if any, and a new piece is spawned.
     *    b. If the collision was horizontal (e.g., trying to move into a wall or another piece sideways),
     *       the piece simply does not move, and its position remains unchanged.
     *
     * @param deltaCol The change in the piece's column position (e.g., -1 for left, 1 for right, 0 for no horizontal change).
     * @param deltaRow The change in the piece's row position (e.g., 1 for down, 0 for no vertical change).
     */
    fun movePiece(
        deltaCol: Int,
        deltaRow: Int
    ) {
        val currentGameState = _gameState.value
        val piece = currentGameState.currentPiece ?: return
        if (currentGameState.isGameOver) return

        val potentialNewRow = piece.row + deltaRow
        val potentialNewCol = piece.col + deltaCol

        // 1. Clear current piece from a temporary representation of the grid
        val gridWithoutOldPiece = currentGameState.grid
            .clearShapeFromGrid(
                shape = piece.shape,
                shapeTopRow = piece.row,
                shapeLeftCol = piece.col
            )

        // 2. Check for collision at the new position
        if (!gridWithoutOldPiece
            .hasCollision(
                shape = piece.shape,
                potentialTopRow = potentialNewRow,
                potentialLeftCol = potentialNewCol
            )
            ) {
            // No collision: Move the piece
            val newGridWithMovedPiece = gridWithoutOldPiece
                .mergeShapeIntoGrid(
                    shape = piece.shape,
                    topRow = potentialNewRow,
                    leftCol = potentialNewCol
                )
            val updatedPiece = piece
                .copy(
                    row = potentialNewRow,
                    col = potentialNewCol
                )
            _gameState.update { gameState ->
                gameState
                    .copy(
                        grid = newGridWithMovedPiece,
                        currentPiece = updatedPiece
                    )
            }
        } else {
            // Collision detected
            if (deltaRow > 0) { // If collision happened while moving down (either by player or game tick)
                lockPieceAndSpawnNew(gridWithoutOldPiece, piece) // Pass gridWithoutOldPiece to lock on
            }
            // If collision was horizontal, the piece simply doesn't move.
        }
    }

    fun rotatePiece() {
        val currentGameState = _gameState.value
        val piece = currentGameState.currentPiece ?: return
        if (currentGameState.isGameOver) return

        val rotatedShape = piece.shape.rotateClockwise() // Assumes colors are part of shape

        // Create a temporary grid with the current piece cleared
        val gridWithoutOldPiece = currentGameState.grid.clearShapeFromGrid(piece.shape, piece.row, piece.col)

        // Check for collision with the rotated shape at the current position
        // TODO: Implement wall kicks here for better feel
        if (!gridWithoutOldPiece
            .hasCollision(
                shape = rotatedShape,
                potentialTopRow = piece.row,
                potentialLeftCol = piece.col
            )
            ) {
            val newGridWithRotatedPiece = gridWithoutOldPiece
                .mergeShapeIntoGrid(
                    shape = rotatedShape,
                    topRow = piece.row,
                    leftCol = piece.col
                )
            val updatedPiece = piece.copy(shape = rotatedShape)
            _gameState.update {
                it.copy(grid = newGridWithRotatedPiece, currentPiece = updatedPiece)
            }
        }
        // If collision, rotation fails (or wall kick logic would try alternatives)
    }

    private fun lockPieceAndSpawnNew(gridToLockOn: GameGrid, pieceToLock: GamePiece) {
        // The pieceToLock is at its final resting position (pieceToLock.row, pieceToLock.col)
        // on the `gridToLockOn` (which is the main grid state before this piece was last merged for movement).
        // So, we need to merge it onto gridToLockOn one last time.
        val gridWithLockedPiece = gridToLockOn.mergeShapeIntoGrid(pieceToLock.shape, pieceToLock.row, pieceToLock.col)

        // Check for cleared lines
        val (gridAfterClearingLines, linesClearedThisTurn) = clearCompletedLines(gridWithLockedPiece)

        // Update score and level (simplified)
        var newScore = _gameState.value.score
        var totalLinesCleared = _gameState.value.linesCleared
        if (linesClearedThisTurn > 0) {
            newScore += linesClearedThisTurn * 100 // Example scoring
            totalLinesCleared += linesClearedThisTurn
            // Potentially increase level and game speed
        }

        // Update state before spawning (to avoid collision with the piece that was just locked)
        _gameState.update {
            it.copy(
                grid = gridAfterClearingLines,
                currentPiece = null, // Current piece is now locked
                score = newScore,
                linesCleared = totalLinesCleared
                // Update level and gameSpeedMillis if necessary
            )
        }

        spawnNewPiece() // Spawn the next piece
    }

    private fun clearCompletedLines(grid: GameGrid): Pair<GameGrid, Int> {
        val newGrid = grid.deepCopy() // Work on a copy
        var linesClearedCount = 0
        val rows = newGrid.size
        val cols = newGrid[0].size
        var currentRow = rows - 1 // Start checking from the bottom

        while (currentRow >= 0) {
            if (newGrid[currentRow].all { it != BlockColor.EMPTY }) {
                // Line is full
                linesClearedCount++
                // Shift all rows above it down
                for (r in currentRow downTo 1) {
                    newGrid[r] = newGrid[r - 1].copyOf()
                }
                // Clear the top row
                newGrid[0] = Array(cols) { BlockColor.EMPTY }
                // Don't decrement currentRow, check the same row index again as it now contains the row from above
            } else {
                currentRow-- // Move to the row above
            }
        }
        return Pair(newGrid, linesClearedCount)
    }

    // --- PUBLIC EVENT HANDLERS for UI Controls ---
    fun onMoveLeft() {
        if (_gameState.value.isGameOver || _gameState.value.currentPiece == null) return
        movePiece(-1, 0) // deltaCol = -1, deltaRow = 0
    }

    fun onMoveRight() {
        if (_gameState.value.isGameOver || _gameState.value.currentPiece == null) return
        movePiece(1, 0)  // deltaCol = 1, deltaRow = 0
    }

    fun onRotate() {
        if (_gameState.value.isGameOver || _gameState.value.currentPiece == null) return
        rotatePiece()
    }

    fun onMoveDown() { // Soft drop by player
        if (_gameState.value.isGameOver || _gameState.value.currentPiece == null) return
        movePiece(0, 1)  // deltaCol = 0, deltaRow = 1
    }

    fun onHardDrop() { // Optional: Implement hard drop
        if (_gameState.value.isGameOver || _gameState.value.currentPiece == null) return
        val piece = _gameState.value.currentPiece!!
        val currentGrid = _gameState.value.grid

        // Keep moving down until collision
        val tempGridAfterClearing = currentGrid
            .clearShapeFromGrid(
                shape = piece.shape,
                shapeTopRow = piece.row,
                shapeLeftCol = piece.col
            )
        var newRow = piece.row
        while (!tempGridAfterClearing
            .hasCollision(
                shape = piece.shape,
                potentialTopRow = newRow + 1,
                potentialLeftCol = piece.col
            )
        ) {
            newRow++
        }

        // Lock at the final position
        val finalPiece = piece.copy(row = newRow)
        // The grid passed to lockPieceAndSpawnNew should be the one BEFORE this final piece is merged
        lockPieceAndSpawnNew(
            gridToLockOn = tempGridAfterClearing,
            pieceToLock = finalPiece
        )
    }

    override fun onCleared() {
        super.onCleared()
        gameLoopJob?.cancel() // Ensure the loop is stopped when ViewModel is destroyed
    }

}
