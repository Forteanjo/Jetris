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
            _gameState.update { it.copy(isGameOver = true) }
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
        _gameState.update {
            it.copy(
                grid = gridWithNewPiece,
                currentPiece = newPiece,
                nextPieceColor = nextRandomColor
            )
        }
        prepareNextPieceDisplay() // Update the "next piece" display
    }

    fun movePiece(deltaCol: Int, deltaRow: Int) {
        val currentGameState = _gameState.value
        val piece = currentGameState.currentPiece ?: return
        if (currentGameState.isGameOver) return

        val potentialNewRow = piece.row + deltaRow
        val potentialNewCol = piece.col + deltaCol

        // 1. Clear current piece from a temporary representation of the grid
        val gridWithoutOldPiece = currentGameState.grid.clearShapeFromGrid(piece.shape, piece.row, piece.col)

        // 2. Check for collision at the new position
        if (!gridWithoutOldPiece
            .hasCollision(
                shape = piece.shape,
                potentialTopRow = potentialNewRow,
                potentialLeftCol = potentialNewCol
            )
            ) {
            // No collision: Move the piece
            val newGridWithMovedPiece = gridWithoutOldPiece.mergeShapeIntoGrid(piece.shape, potentialNewRow, potentialNewCol)
            val updatedPiece = piece.copy(row = potentialNewRow, col = potentialNewCol)
            _gameState.update {
                it.copy(grid = newGridWithMovedPiece, currentPiece = updatedPiece)
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

    override fun onCleared() {
        super.onCleared()
        gameLoopJob?.cancel() // Ensure the loop is stopped when ViewModel is destroyed
    }


        var startingRow: Int = 0

    var num1 = 0
    var num2 = 0
    var num3 = 0
    var num4 = 0

    var shapeIs = 0
    var lines = 0
    var once = 0

    /**
     * How to use in your Jetris game:
     *
     * 1.   When a new piece is spawned, you would call mergeAndCenterShape with your main game grid
     *      and the piece's current shape array.
     *
     * 2.   When a piece moves, you would typically:
     *  •   First, "clear" the piece from its old position on a temporary grid (by merging it
     *      with BlockColor.EMPTY where the piece was).
     *  •   Then, calculate the new position.
     *  •   Then, merge the piece into the temporary grid at its new position using
     *      mergeAndCenterShape with customStartRow and customStartCol set to the piece's new
     *      top-left coordinates.
     *  •   Check for collisions after this merge.
     *  •   If no collision, this temporary grid becomes the new main game grid.
     */
    private fun nextShape(): BlockColor {
        val nextBlock = BlockColor.randomPlayable()
        _nextBlockGrid.value = emptyNextBlockGrid.mergeAndCenterShape(
            shape = nextBlock.toBlockShape(),
        )

        return nextBlock
    }

/*
    // --- Inside your game state or ViewModel ---
    var currentGameGrid: GameGrid = initialEmptyGrid()
    var currentPiece: TetrisPiece? = null // Holds current piece's shape, position, etc.

    fun movePiece(direction: Direction) {
        if (currentPiece == null) return

        val piece = currentPiece!!

        // 1. Create a temporary grid with the current piece cleared from its OLD position
        val gridWithoutOldPiece = clearShapeFromGrid(
            currentGameGrid,
            piece.currentShapeArray, // The 2D array of the piece
            piece.row,               // Piece's current top row
            piece.col                // Piece's current left col
        )

        // 2. Calculate the piece's NEW potential position
        val newRow = piece.row + direction.deltaRow // However you calculate new position
        val newCol = piece.col + direction.deltaCol

        // 3. Attempt to place/merge the piece at its NEW position on the temporary grid
        //    You'd use a function like `mergeShapeIntoGrid(grid, shape, newRow, newCol)`
        //    This merge function would also typically perform collision detection.
        val (potentialNewGrid, collisionDetected) = tryPlacingPiece(
            gridWithoutOldPiece,
            piece.currentShapeArray,
            newRow,
            newCol
        )

        if (!collisionDetected) {
            // 4. If no collision, update the main game grid and the piece's position
            currentGameGrid = potentialNewGrid
            currentPiece = piece.copy(row = newRow, col = newCol)
            // Update UI
        } else {
            // Handle collision (e.g., piece locks if moving down, or invalid move)
            // If the collision was with the bottom or other locked pieces when moving down:
            if (direction == androidx.test.uiautomator.Direction.DOWN) {
                lockPieceAndSpawnNewOne() // You'd merge the piece onto currentGameGrid at its last valid spot
            }
            // Else, the move was invalid (e.g., into a wall), so piece doesn't move.
            // The currentGameGrid remains as it was before this attempted move.
        }
    }

    // Dummy tryPlacingPiece for illustration
    data class PlacementResult(val grid: GameGrid, val collision: Boolean)

    fun tryPlacingPiece(grid: GameGrid, shape: GameGrid, newRow: Int, newCol: Int): PlacementResult {
        // This function would merge the shape into a copy of the grid at newRow, newCol.
        // It would check if any part of the shape overlaps with existing blocks in the grid
        // or goes out of bounds.
        // For simplicity, let's assume it always succeeds for now and just merges.
        val tempGrid = grid.deepCopy()
        var collision = false

        for (r in 0 until shape.size) {
            for (c in 0 until shape[0].size) {
                if (shape[r][c] != BlockColor.EMPTY) {
                    val targetR = newRow + r
                    val targetC = newCol + c
                    if (targetR < 0 || targetR >= tempGrid.size || targetC < 0 || targetC >= tempGrid[0].size ||
                        tempGrid[targetR][targetC] != BlockColor.EMPTY) {
                        collision = true
                        break // Found collision
                    }
                    // tempGrid[targetR][targetC] = shape[r][c] // Only merge if no collision check for whole piece is done first
                }
            }
            if (collision) break
        }

        if (!collision) {
            for (r in 0 until shape.size) {
                for (c in 0 until shape[0].size) {
                    if (shape[r][c] != BlockColor.EMPTY) {
                        val targetR = newRow + r
                        val targetC = newCol + c
                        tempGrid[targetR][targetC] = shape[r][c]
                    }
                }
            }
            return PlacementResult(tempGrid, false)
        }

        return PlacementResult(grid, true) // Return original grid and collision true
    }

    This "clear-then-try-place" strategy is fundamental for Tetris-like game
*/

    /**
     * // --- Inside your game state or ViewModel ---
     * // (Similar to the `movePiece` example from the "clearShapeFromGrid" answer)
     *
     * fun attemptMove(piece: TetrisPiece, deltaRow: Int, deltaCol: Int): Boolean {
     *     val potentialNewRow = piece.row + deltaRow
     *     val potentialNewCol = piece.col + deltaCol
     *
     *     if (!hasCollision(currentGameGrid, piece.currentShapeArray, potentialNewRow, potentialNewCol)) {
     *         // No collision, actual move can happen
     *         // 1. Clear piece from old position on a temporary grid (or the main grid if that's your strategy)
     *         val gridWithoutOldPiece = clearShapeFromGrid(currentGameGrid, piece.currentShapeArray, piece.row, piece.col)
     *         // 2. Merge piece into new position on this temporary grid
     *         currentGameGrid = mergeShapeIntoGrid(gridWithoutOldPiece, piece.currentShapeArray, potentialNewRow, potentialNewCol) // Assuming you have mergeShapeIntoGrid
     *         currentPiece = piece.copy(row = potentialNewRow, col = potentialNewCol)
     *         return true // Move was successful
     *     }
     *     return false // Move failed due to collision
     * }
     *
     * fun attemptRotate(piece: TetrisPiece): Boolean {
     *     val rotatedShape = piece.currentShapeArray.rotateClockwise() // Or your rotation logic
     *     if (!hasCollision(currentGameGrid, rotatedShape, piece.row, piece.col)) {
     *         // No collision with rotated shape at current position
     *         val gridWithoutOldPiece = clearShapeFromGrid(currentGameGrid, piece.currentShapeArray, piece.row, piece.col)
     *         currentGameGrid = mergeShapeIntoGrid(gridWithoutOldPiece, rotatedShape, piece.row, piece.col)
     *         currentPiece = piece.copy(currentShapeArray = rotatedShape)
     *         return true // Rotation successful
     *     }
     *     // Optional: Implement wall kicks here if rotation fails
     *     return false // Rotation failed
     * }
     *
     * // When a piece is moving down automatically (game tick):
     * fun gameTick() {
     *     if (currentPiece == null) spawnNewPiece()
     *
     *     currentPiece?.let { piece ->
     *         val potentialNewRow = piece.row + 1 // Moving down one step
     *         val potentialNewCol = piece.col
     *
     *         if (hasCollision(currentGameGrid, piece.currentShapeArray, potentialNewRow, potentialNewCol)) {
     *             // Collision detected when trying to move down
     *             lockPieceAndSpawnNewOne(piece)
     *         } else {
     *             // No collision, move the piece down
     *             val gridWithoutOldPiece = clearShapeFromGrid(currentGameGrid, piece.currentShapeArray, piece.row, piece.col)
     *             currentGameGrid = mergeShapeIntoGrid(gridWithoutOldPiece, piece.currentShapeArray, potentialNewRow, potentialNewCol)
     *             currentPiece = piece.copy(row = potentialNewRow)
     *         }
     *     }
     *     // Update UI
     * }
     *
     * fun lockPieceAndSpawnNewOne(pieceToLock: TetrisPiece) {
     *     // The piece is already at its final resting place (pieceToLock.row, pieceToLock.col)
     *     // currentGameGrid should reflect this state *before* this function is called,
     *     // OR, if you haven't moved it to the colliding spot, merge it at its last valid spot.
     *
     *     // For simplicity, let's assume currentGameGrid already has the piece at its
     *     // last valid non-colliding position before the *colliding* move down was attempted.
     *     // So, we just need to "finalize" it on the grid (which is already done by mergeShapeIntoGrid)
     *
     *     // Check for cleared lines
     *     // clearLines()
     *     // Add score
     *
     *     spawnNewPiece()
     *     // Check for game over (if new piece immediately collides)
     * }
     *
     * // Dummy merge function
     * fun mergeShapeIntoGrid(grid: GameGrid, shape: GameGrid, row: Int, col: Int): GameGrid {
     *     val newGrid = grid.deepCopy()
     *     for (r in 0 until shape.size) {
     *         for (c in 0 until shape[0].size) {
     *             if (shape[r][c] != BlockColor.EMPTY) {
     *                 if ((row + r >= 0 && row + r < newGrid.size) && (col + c >= 0 && col + c < newGrid[0].size)) {
     *                     newGrid[row + r][col + c] = shape[r][c]
     *                 }
     *             }
     *         }
     *     }
     *     return newGrid
     * }
     *
     *
     */



}
