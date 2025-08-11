package sco.carlukesoftware.jetris.utils

import sco.carlukesoftware.jetris.ui.data.BlockColor

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
