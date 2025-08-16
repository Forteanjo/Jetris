package sco.carlukesoftware.jetris.utils

import androidx.compose.ui.unit.dp
import sco.carlukesoftware.jetris.data.BlockColor
import kotlin.math.floor

typealias GameGridRow = Array<BlockColor>
typealias GameGrid = Array<GameGridRow>

const val GRID_COLUMNS = 10
const val GRID_ROWS = 15

val SCREEN_PADDING = 8.dp
val GRID_SPACING = 16.dp // Spacing between main grid and next piece grid

val MAX_BLOCK_SIZE = 90.dp

val emptyGameGrid: GameGrid = Array(GRID_ROWS) {
    // For each row, create a new row filled with EMPTY
    Array(GRID_COLUMNS) { BlockColor.EMPTY }
}

const val NEXT_BLOCK_GRID_COLUMNS = 3
const val NEXT_BLOCK_GRID_ROWS = 4

val emptyNextBlockGrid: GameGrid = Array(NEXT_BLOCK_GRID_ROWS) {
    Array(NEXT_BLOCK_GRID_COLUMNS) { BlockColor.EMPTY }
}

val orangeShape = arrayOf(
    arrayOf(BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.ORANGE),
    arrayOf(BlockColor.ORANGE, BlockColor.ORANGE, BlockColor.ORANGE),
)
val rotatedOrangeShape = orangeShape.rotateClockwise()

val blueShape = arrayOf(
    arrayOf(BlockColor.BLUE, BlockColor.EMPTY, BlockColor.EMPTY),
    arrayOf(BlockColor.BLUE, BlockColor.BLUE, BlockColor.BLUE),
)
val redShape = arrayOf(
    arrayOf(BlockColor.RED, BlockColor.RED, BlockColor.EMPTY),
    arrayOf(BlockColor.EMPTY, BlockColor.RED, BlockColor.RED),
)
val greenShape = arrayOf(
    arrayOf(BlockColor.EMPTY, BlockColor.GREEN, BlockColor.GREEN),
    arrayOf(BlockColor.GREEN, BlockColor.GREEN, BlockColor.EMPTY),
)
val yellowShape = arrayOf(
    arrayOf(BlockColor.YELLOW, BlockColor.YELLOW),
    arrayOf(BlockColor.YELLOW, BlockColor.YELLOW),
)
val purpleShape = arrayOf(
    arrayOf(BlockColor.EMPTY, BlockColor.PURPLE, BlockColor.EMPTY),
    arrayOf(BlockColor.PURPLE, BlockColor.PURPLE, BlockColor.PURPLE),
)
val pinkShape = arrayOf(
    arrayOf(BlockColor.PINK),
    arrayOf(BlockColor.PINK),
    arrayOf(BlockColor.PINK),
    arrayOf(BlockColor.PINK),
)


/**
 * Rotates a 2D array (matrix) clockwise by 90 degrees.
 *
 * This is an extension function for `Array<Array<T>>`, making it applicable
 * to any 2D array where `T` can be any type (e.g., `Int`, `String`, `BlockColor`).
 *
 * The function handles empty matrices or matrices with empty rows by returning
 * an appropriately sized empty matrix.
 *
 * The rotation logic involves creating a new matrix where the number of rows
 * is the original number of columns, and the number of columns is the original
 * number of rows. Elements are then mapped from their original position `(r, c)`
 * to the new position `(c, oldRows - 1 - r)`.
 *
 * **Note on Initialization:**
 * The new matrix is initialized with `this[0][0]` as a placeholder. This assumes:
 * 1. The input array is not completely empty (checked at the beginning).
 * 2. The type `T` is non-nullable, or if nullable, the user is aware that the
 *    initialization might use a non-null value from the original array.
 * For truly generic scenarios with nullable types or complex objects, more
 * sophisticated initialization might be required (e.g., using a factory function
 * or requiring `T` to have a default constructor). However, for use cases like
 * `BlockColor`, which is a non-nullable enum, this approach is sufficient.
 *
 * @receiver The 2D array to be rotated.
 * @return A new 2D array representing the clockwise rotated version of the original.
 *         Returns an empty matrix if the input is empty or has empty rows.
 */
inline fun <reified T> Array<Array<T>>.rotateClockwise(): Array<Array<T>> {
    val oldRows = this.size
    if (oldRows == 0) return Array(0) { arrayOf() } // Handle empty matrix
    val oldCols = this[0].size
    if (oldCols == 0) return Array(oldRows) { arrayOf() } // Handle matrix with empty rows

    // The new matrix will have oldCols rows and oldRows columns
    // Need to initialize with a dummy value or handle nulls if T is nullable
    // Using 'this[0][0]' as a placeholder assumes the array is not empty and T is not nullable
    // For a truly generic solution with nullable types or complex objects, careful initialization is needed.
    // However, for BlockColor, which is non-nullable and has discrete values, this is simpler.
    val newMatrix = Array(oldCols) { Array(oldRows) { this[0][0] } } // Placeholder initialization

    for (r in 0 until oldRows) {
        for (c in 0 until oldCols) {
            newMatrix[c][oldRows - 1 - r] = this[r][c]
        }
    }
    return newMatrix
}


/**
 * Creates a deep copy of the GameGrid.
 *
 * This function iterates through each row and column of the original GameGrid
 * and copies its elements to a new GameGrid. This ensures that modifications
 * to the copied grid do not affect the original grid.
 *
 * @receiver The GameGrid to be copied.
 * @return A new GameGrid instance that is a deep copy of the original.
 */
fun GameGrid.deepCopy(): GameGrid {
    return Array(this.size) { r ->
        Array(this[r].size) { c ->
            this[r][c]
        }
    }
}


/**
 * Merges a smaller 2D array (shape) into a larger 2D grid, attempting to center it.
 *
 * @receiver The larger destination grid.
 * @param shape The smaller 2D array to merge.
 * @param emptyColor The BlockColor representing an empty/transparent cell in the shape.
 *                   Cells with this color in the shape will not overwrite the grid.
 * @param startRow Optional: Specify a custom starting row in the grid. If null, centers.
 * @param startCol Optional: Specify a custom starting column in the grid. If null, centers.
 * @return A new GameGrid with the shape merged, or the original grid if the shape cannot fit or is empty.
 */
fun GameGrid.mergeAndCenterShape(
    shape: GameGrid,
    emptyColor: BlockColor = BlockColor.EMPTY,
    startRow: Int? = null,
    startCol: Int? = null
): GameGrid {
    val gridRows = this.size
    if (gridRows == 0) return this.deepCopy() // Nothing to merge into if grid is empty
    val gridCols = this[0].size
    if (gridCols == 0) return this.deepCopy()

    val shapeRows = shape.size
    if (shapeRows == 0) return this.deepCopy() // Nothing to merge if shape is empty
    val shapeCols = shape[0].size
    if (shapeCols == 0) return this.deepCopy()

    // 1. Determine Target Position (Centering)
    val startRow = startRow ?: floor((gridRows.toDouble() - shapeRows.toDouble()) / 2.0).toInt()
    val startCol = startCol ?: floor((gridCols.toDouble() - shapeCols.toDouble()) / 2.0).toInt()


    // 2. Create a Copy of the grid
    val newGrid = this.deepCopy()

    // 3. Iterate and Merge
    for (r in 0 until shapeRows) {
        for (c in 0 until shapeCols) {
            val shapeCellColor = shape[r][c]

            // Only merge if the shape cell is not the designated empty/transparent color
            if (shapeCellColor != emptyColor) {
                val targetGridRow = startRow + r
                val targetGridCol = startCol + c

                // Boundary check before writing to the newGrid
                if (targetGridRow >= 0 && targetGridRow < gridRows &&
                    targetGridCol >= 0 && targetGridCol < gridCols
                ) {
                    newGrid[targetGridRow][targetGridCol] = shapeCellColor
                }
            }
        }
    }
    return newGrid
}


/**
 * Merges a shape into the game grid at a specified position.
 *
 * This function creates a deep copy of the current grid and then overlays the
 * non-empty blocks of the given `shape` onto this copy. The `topRow` and `leftCol`
 * parameters specify the top-left corner of where the shape should be placed
 * on the grid.
 *
 * Cells in the `shape` that are `BlockColor.EMPTY` are considered transparent
 * and will not overwrite the corresponding cells in the grid.
 *
 * Boundary checks are performed: if any part of the shape would be placed
 * outside the grid's dimensions, that part is ignored (not merged).
 *
 * @receiver The base `GameGrid` onto which the shape will be merged.
 * @param shape The `GameGrid` representing the shape to merge.
 * @param topRow The row index in the base grid where the top of the shape will be placed.
 * @param leftCol The column index in the base grid where the left of the shape will be placed.
 * @return A new `GameGrid` instance with the shape merged into it. The original
 *         grid remains unchanged.
 */
fun GameGrid.mergeShapeIntoGrid(shape: GameGrid, topRow: Int, leftCol: Int): GameGrid {
    val newGrid = this.deepCopy()
    shape.forEachIndexed { r, rowArray ->
        rowArray.forEachIndexed { c, blockColor ->
            if (blockColor != BlockColor.EMPTY) {
                val targetR = topRow + r
                val targetC = leftCol + c
                if (targetR in newGrid.indices && targetC in newGrid[0].indices) {
                    newGrid[targetR][targetC] = blockColor
                }
            }
        }
    }
    return newGrid
}

/**
 * Clears a shape from a given position on the grid by setting its non-empty cells to EMPTY.
 *
 * @receiver The current game grid.
 * @param shape The 2D array representing the shape to clear.
 * @param shapeTopRow The top row index on the grid where the shape is currently located.
 * @param shapeLeftCol The left column index on the grid where the shape is currently located.
 * @param shapeEmptyColor The BlockColor in the 'shape' array that represents an empty part
 *                        of the shape's bounding box (these parts won't affect the grid).
 * @return A new GameGrid with the shape cleared.
 */
fun GameGrid.clearShapeFromGrid(
    shape: GameGrid,
    shapeTopRow: Int,
    shapeLeftCol: Int,
    shapeEmptyColor: BlockColor = BlockColor.EMPTY // The color in the shape's own array that's considered empty
): GameGrid {
    val gridRows = this.size
    if (gridRows == 0) return this.deepCopy()
    val gridCols = this[0].size
    if (gridCols == 0) return this.deepCopy()

    val shapeRows = shape.size
    if (shapeRows == 0) return this.deepCopy() // Nothing to clear if shape is empty
    val shapeCols = shape[0].size
    if (shapeCols == 0) return this.deepCopy()

    val newGrid = this.deepCopy()

    for (r in 0 until shapeRows) { // Iterate through the shape's rows
        for (c in 0 until shapeCols) { // Iterate through the shape's columns
            val shapeCellColor = shape[r][c]

            // Only proceed if the current cell in the SHAPE is not considered empty
            if (shapeCellColor != shapeEmptyColor) {
                val targetGridRow = shapeTopRow + r
                val targetGridCol = shapeLeftCol + c

                // Boundary check: Ensure we are within the grid's bounds
                if (targetGridRow >= 0 && targetGridRow < gridRows &&
                    targetGridCol >= 0 && targetGridCol < gridCols
                ) {
                    // Set the corresponding cell in the newGrid to EMPTY
                    newGrid[targetGridRow][targetGridCol] = BlockColor.EMPTY // The actual grid's empty color
                }
            }
        }
    }

    return newGrid
}

/**
 * Checks for collisions if a shape were placed at a given position on the grid.
 *
 * @receiver The main game grid containing already locked blocks.
 * @param shape The 2D array representing the piece's shape.
 * @param potentialTopRow The potential top row index on the grid for the shape.
 * @param potentialLeftCol The potential left column index on the grid for the shape.
 * @param shapeEmptyColor The BlockColor in the 'shape' array that represents an empty part
 *                        of the shape's bounding box (these parts are ignored for collision).
 * @return True if a collision would occur, false otherwise.
 */
fun GameGrid.hasCollision(
    shape: GameGrid,
    potentialTopRow: Int,
    potentialLeftCol: Int,
    shapeEmptyColor: BlockColor = BlockColor.EMPTY
): Boolean {
    val gridRows = this.size
    if (gridRows == 0 && shape.isNotEmpty()) return true // Cannot place on empty grid if shape exists
    val gridCols = if (gridRows > 0) this[0].size else 0
    if (gridCols == 0 && shape.isNotEmpty() && shape[0].isNotEmpty()) return true

    val shapeRows = shape.size
    if (shapeRows == 0) return false // Empty shape causes no collision
    val shapeCols = shape[0].size
    if (shapeCols == 0) return false

    for (r in 0 until shapeRows) { // Iterate through the shape's rows
        for (c in 0 until shapeCols) { // Iterate through the shape's columns
            val shapeCellColor = shape[r][c]

            // Only check collision for solid parts of the shape
            if (shapeCellColor != shapeEmptyColor) {
                val targetGridRow = potentialTopRow + r
                val targetGridCol = potentialLeftCol + c

                // 1. Check for Out of Bounds (Left, Right, Bottom)
                // Top boundary (targetGridRow < 0) might be handled differently
                // (e.g., allow spawning partially off-screen initially, but not for movement).
                // For general movement collision, going off the top isn't usually a "collision"
                // that stops movement, but rather an invalid state for locking.

                if (targetGridCol < 0 || targetGridCol >= gridCols || targetGridRow >= gridRows) {
                    return true // Collision with left, right, or bottom boundary
                }

                // It's also possible to collide with the top if potentialTopRow + r < 0,
                // but usually pieces spawn such that this isn't an immediate issue for the
                // first few cells of the shape. If a piece can rotate such that a part of it
                // is above row 0, you'd add: targetGridRow < 0
                // For now, focusing on side and bottom boundaries for active piece movement.

                // 2. Check for Overlap with Locked Blocks (if within bounds)
                // This check assumes targetGridRow is not negative. If it can be, add it above.
                if (targetGridRow >= 0) { // Ensure we don't try to access grid[-1]
                    if (this[targetGridRow][targetGridCol] != BlockColor.EMPTY) {
                        return true // Collision with an existing locked block
                    }
                } else {
                    // This case (targetGridRow < 0) means part of the piece is above the grid.
                    // Depending on game rules, this might be a collision or an invalid spawn.
                    // For typical Tetris piece movement, this could be a collision if not allowed.
                    // For spawning, pieces often start partially above the visible grid.
                    // If pieces must always be fully within grid boundaries (0 to gridRows-1):
                    return true // Collision with top boundary
                }
            }
        }
    }

    return false // No collision detected
}
