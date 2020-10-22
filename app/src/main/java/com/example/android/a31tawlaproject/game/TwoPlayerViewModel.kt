package com.example.android.a31tawlaproject.game

import android.app.Application
import com.example.android.a31tawlaproject.miscUtils.Cell
import com.example.android.a31tawlaproject.miscUtils.MovePlayed

class TwoPlayerViewModel(application: Application) : GameViewModel(application) {

    override fun move(cell: Cell) {
        //-s
        unhighlightMove()
        //-s
        removePiece(sourceCell)
        addPiece(cell)
        if (!(cell.cellNumber in playersCells[currentColor-1])) {
            playersCells[currentColor-1].add(cell.cellNumber)
        }
        if (sourceCell.numberOfPieces.value == 0) {
            playersCells[currentColor-1].remove(sourceCell.cellNumber)
        }
        undoList.add(
            MovePlayed(
                sourceCell.cellNumber,
                cell.cellNumber,
                false
            )
        )
        _isUndoEnabled.value = true

        if (currentColor == 1 && sourceCell.cellNumber < 19 && cell.cellNumber >= 19) {
            piecesAtHomePlayer[0]++
            undoList.peek().pieceMovedToHome = true
        }
        else if (currentColor == 2 && sourceCell.cellNumber > 6 && cell.cellNumber <= 6) {
            piecesAtHomePlayer[1] ++
            undoList.peek().pieceMovedToHome = true
        }

    }

    override fun switchTurns() {
        super.switchTurns()
        currentColor = if (currentColor == 1) {
            sign = -1
            println("player 2")
            2
        }
        else {
            sign = 1
            println("player 1")
            1
        }
        diceRolled = false
    }

}