package sco.carlukesoftware.jetris.viewmodel

import androidx.lifecycle.ViewModel
import sco.carlukesoftware.jetris.ui.data.BlockColor
import sco.carlukesoftware.jetris.utils.GameGrid
import sco.carlukesoftware.jetris.utils.emptyNextBlockGrid

class GameViewModel : ViewModel() {

    var nextBlockGrid: GameGrid = emptyNextBlockGrid

    var nextUp: Int = 0
    var startingRow: Int = 0

    var num1 = 0
    var num2 = 0
    var num3 = 0
    var num4 = 0

}
