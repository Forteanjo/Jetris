package sco.carlukesoftware.jetris.data

import sco.carlukesoftware.jetris.utils.GameGrid
import sco.carlukesoftware.jetris.utils.emptyGameGrid

// Represents the overall game state
data class GameState(
    val grid: GameGrid = emptyGameGrid, // The main game board
    val currentPiece: GamePiece? = null,
    val nextPieceColor: BlockColor = BlockColor.randomPlayable(), // Color for the next piece
    val score: Int = 0,
    val linesCleared: Int = 0,
    val level: Int = 1,
    val isGameOver: Boolean = false,
    val gameSpeedMillis: Long = 1000L // Initial speed: 1 tick per second
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameState

        if (score != other.score) return false
        if (linesCleared != other.linesCleared) return false
        if (level != other.level) return false
        if (isGameOver != other.isGameOver) return false
        if (gameSpeedMillis != other.gameSpeedMillis) return false
        if (!grid.contentEquals(other.grid)) return false
        if (currentPiece != other.currentPiece) return false
        if (nextPieceColor != other.nextPieceColor) return false

        return true
    }

    override fun hashCode(): Int {
        var result = score
        result = 31 * result + linesCleared
        result = 31 * result + level
        result = 31 * result + isGameOver.hashCode()
        result = 31 * result + gameSpeedMillis.hashCode()
        result = 31 * result + grid.contentHashCode()
        result = 31 * result + (currentPiece?.hashCode() ?: 0)
        result = 31 * result + nextPieceColor.hashCode()
        return result
    }
}
