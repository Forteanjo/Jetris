package sco.carlukesoftware.jetris.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import sco.carlukesoftware.jetris.data.BlockColor
import sco.carlukesoftware.jetris.utils.GameGrid
import sco.carlukesoftware.jetris.utils.emptyNextBlockGrid
import sco.carlukesoftware.jetris.utils.mergeAndCenterShape
import sco.carlukesoftware.jetris.utils.toBlockShape

class GameViewModel : ViewModel() {

    private val _nextBlockGrid: MutableStateFlow<GameGrid> = MutableStateFlow(emptyNextBlockGrid)
    val nextBlockGrid: StateFlow<GameGrid> = _nextBlockGrid.asStateFlow()

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



}
