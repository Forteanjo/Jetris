package sco.carlukesoftware.jetris.data

enum class BlockColor {
    EMPTY,
    RED,
    BLUE,
    GREEN,
    PURPLE,
    YELLOW,
    ORANGE,
    PINK;

    companion object {
        private val PLAYABLE_COLORS = entries.filter { it != EMPTY }

        /**
         * Returns a random playable block color.
         *
         * @return A random [BlockColor] from the [PLAYABLE_COLORS] list.
         * @throws IllegalStateException if [PLAYABLE_COLORS] is empty.
         */
        fun randomPlayable(): BlockColor {
            if (PLAYABLE_COLORS.isEmpty()) {
                // This case should ideally not happen if you have playable colors
                // Or if EMPTY is the only color, it's a specific scenario to handle
                throw IllegalStateException("No playable block colors defined.")
            }
            return PLAYABLE_COLORS.random()
        }

        /**
         * Returns a random BlockColor from the available enum entries.
         *
         * @return A randomly selected [BlockColor].
         * @throws IllegalStateException if there are no block colors defined in the enum.
         */
        fun randomAny(): BlockColor {
            val allColors = entries.toTypedArray()
            if (allColors.isEmpty()) {
                throw IllegalStateException("No block colors defined.")
            }
            return allColors.random()
        }
    }
}
