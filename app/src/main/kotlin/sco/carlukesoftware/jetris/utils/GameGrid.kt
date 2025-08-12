package sco.carlukesoftware.jetris.utils

import sco.carlukesoftware.jetris.data.BlockColor
import kotlin.math.floor

typealias GameGridRow = Array<BlockColor>
typealias GameGrid = Array<GameGridRow>

const val GRID_COLUMNS = 10
const val GRID_ROWS = 15

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
