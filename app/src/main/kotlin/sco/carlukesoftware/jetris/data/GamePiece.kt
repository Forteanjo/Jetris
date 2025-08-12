package sco.carlukesoftware.jetris.data

import sco.carlukesoftware.jetris.utils.GameGrid
import kotlin.collections.map
import kotlin.collections.toTypedArray

// Represents the current state of the active piece
data class GamePiece(
    val shape: GameGrid, // Current 2D array of the piece
    val color: BlockColor, // The actual color of the piece (not from shape)
    var row: Int,          // Current top-left row on the main grid
    var col: Int           // Current top-left column on the main grid
) {
    // Helper to get the actual (non-EMPTY) shape of the piece,
    // useful if your shape definitions in BlockColor are just templates.
    // If your shape arrays already have the correct color, you might not need this.
    val coloredShape: GameGrid by lazy {
        shape.map { rowArray ->
            rowArray.map { cellColor ->
                if (cellColor != BlockColor.EMPTY) color else BlockColor.EMPTY
            }.toTypedArray()
        }.toTypedArray()
    }
}
