package sco.carlukesoftware.jetris.utils

import androidx.compose.ui.graphics.Color
import sco.carlukesoftware.jetris.ui.data.BlockColor
import sco.carlukesoftware.jetris.ui.data.BlockColors
import sco.carlukesoftware.jetris.ui.theme.TetrisBlueBorderColor
import sco.carlukesoftware.jetris.ui.theme.TetrisBlueColor
import sco.carlukesoftware.jetris.ui.theme.TetrisGreenBorderColor
import sco.carlukesoftware.jetris.ui.theme.TetrisGreenColor
import sco.carlukesoftware.jetris.ui.theme.TetrisOrangeBorderColor
import sco.carlukesoftware.jetris.ui.theme.TetrisOrangeColor
import sco.carlukesoftware.jetris.ui.theme.TetrisPinkBorderColor
import sco.carlukesoftware.jetris.ui.theme.TetrisPinkColor
import sco.carlukesoftware.jetris.ui.theme.TetrisPurpleBorderColor
import sco.carlukesoftware.jetris.ui.theme.TetrisPurpleColor
import sco.carlukesoftware.jetris.ui.theme.TetrisRedBorderColor
import sco.carlukesoftware.jetris.ui.theme.TetrisRedColor
import sco.carlukesoftware.jetris.ui.theme.TetrisYellowBorderColor
import sco.carlukesoftware.jetris.ui.theme.TetrisYellowColor

fun BlockColor.toBlockColors(): BlockColors =
    when (this) {
        BlockColor.EMPTY -> BlockColors(Color.Transparent, Color.Transparent)
        BlockColor.RED -> BlockColors(TetrisRedColor, TetrisRedBorderColor)
        BlockColor.BLUE -> BlockColors(TetrisBlueColor, TetrisBlueBorderColor)
        BlockColor.GREEN -> BlockColors(TetrisGreenColor, TetrisGreenBorderColor)
        BlockColor.PURPLE -> BlockColors(TetrisPurpleColor, TetrisPurpleBorderColor)
        BlockColor.YELLOW -> BlockColors(TetrisYellowColor, TetrisYellowBorderColor)
        BlockColor.ORANGE -> BlockColors(TetrisOrangeColor, TetrisOrangeBorderColor)
        BlockColor.PINK -> BlockColors(TetrisPinkColor, TetrisPinkBorderColor)
    }
