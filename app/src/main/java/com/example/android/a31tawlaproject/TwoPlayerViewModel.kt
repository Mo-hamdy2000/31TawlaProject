package com.example.android.a31tawlaproject

import android.app.Application
import com.example.android.a31tawlaproject.databinding.GameFragmentBinding

class TwoPlayerViewModel(application: Application, binding: GameFragmentBinding) : GameViewModel(application, binding) {

    override fun move(cell: Cell) {
        //-s
        unhighlightMove()
        //-s
        addPiece(cell)
        removePiece(sourceCell)
        if (!(cell.cellNumber in playersCells[currentColor-1])) {
            playersCells[currentColor-1].add(cell.cellNumber)
        }
        if (sourceCell.numberOfPieces.value == 0) {
            playersCells[currentColor-1].remove(sourceCell.cellNumber)
        }
        undoList.add(MovePlayed(sourceCell.cellNumber, cell.cellNumber, false))
        binding.undoButton.isEnabled = true
        binding.undoButton.alpha = 1.0f

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
        undoList.clear()
        movesList.clear()
        binding.undoButton.isEnabled = false
        binding.undoButton.alpha = 0.5f
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