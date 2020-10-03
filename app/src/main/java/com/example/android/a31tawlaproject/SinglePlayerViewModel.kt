package com.example.android.a31tawlaproject

import android.app.Application
import com.example.android.a31tawlaproject.databinding.GameFragmentBinding
import kotlinx.coroutines.delay

class SinglePlayerViewModel (application: Application, binding: GameFragmentBinding) : GameViewModel(application, binding) {

        private val playerOneMoves = mutableListOf<Cell>()
        private val preferredCells = mutableListOf<Cell>()
        private val singleCells = mutableListOf<Cell>()
         var i = 0
    init {
    preferredCells.add(cellsArray[23])
    }

        private fun computerMove() {

            i = 0
            while (movesList.isNotEmpty()&& i < preferredCells.size) {
               validateMove(preferredCells[i])
                i++
            }
            i=0
            while (movesList.isNotEmpty()&&i < singleCells.size) {
                validateMove(singleCells[i])
                i++
                }
            if ( piecesAtHomePlayer2 == 15 && movesList.isNotEmpty()) {
                collectPieces()
            }
                switchTurns()
            }


    private fun validateMove(cell: Cell){

        if ( piecesAtHomePlayer2 == 15|| (piecesAtHomePlayer2 == 0 && cell.numberOfPieces == 14 )) {
            return
        }
            if (cell.numberOfPieces > 0 && cell.color == currentColor) {
                // awwel ma ydous 3ala piece at2akked ennaha bta3to w fi pieces fel 5ana di
                val iterator = movesList.iterator()
                while (iterator.hasNext()) {
                   val move = iterator.next()
                    if (cell.cellNumber + sign * move in 1..24) {
                        val destinationCell = cellsArray[cell.cellNumber + sign * move - 1]
                        if (destinationCell.color == currentColor || destinationCell.color == 0) {
                            sourceCell = cell
                            move(destinationCell)
                            iterator.remove()
                            i--
                            break
                        }
                    }
                }

        }
        }



        override fun move(cell: Cell) {
            addPiece(cell)
            if(currentColor==1)
            unhighlightMove()
            removePiece(sourceCell)
            if (!(cell.cellNumber in playersCells[currentColor-1])) {
                playersCells[currentColor-1].add(cell.cellNumber)
            }
            if (sourceCell.numberOfPieces == 0) {
                playersCells[currentColor-1].remove(sourceCell.cellNumber)
            }
            undoList.add(MovePlayed(sourceCell.cellNumber, cell.cellNumber, false))
            binding.undoButton.isEnabled = true
            binding.undoButton.alpha = 1.0f
            if (currentColor == 1) {
                //s-
                playerOneMoves.add(cell)
                //-s
                if (sourceCell.cellNumber < 19 && cell.cellNumber >= 19) {
                    piecesAtHomePlayer1 += 1
                    undoList.peek().pieceMovedToHome = true
                }
            }
            else {
                //s-
                if(sourceCell.numberOfPieces==1){
                    preferredCells.remove(sourceCell)
                    singleCells.add(sourceCell)
                }
                else if(sourceCell.numberOfPieces==0)
                    singleCells.remove(sourceCell)

                if(cell.numberOfPieces==2){
                    preferredCells.add(cell)
                    singleCells.remove(cell)
                }
                else if(cell.numberOfPieces==1)
                    singleCells.add(cell)
                //-s

                if (sourceCell.cellNumber > 6 && cell.cellNumber <= 6) {
                    piecesAtHomePlayer2 += 1
                    undoList.peek().pieceMovedToHome = true
                }
            }

        }


    override fun switchTurns() {
        diceRolled = false
        undoList.clear()
        movesList.clear()
        binding.undoButton.isEnabled = false
        binding.undoButton.alpha = 0.5f
        if (currentColor == 1) {
            sign = -1
            println("player 2")
            computerTurn = true
            currentColor = 2
            binding.rollButton.callOnClick()
            computerMove()
        }
        else {
            sign = 1
            println("player 1")
            currentColor = 1
            computerTurn = false
        }
    }


    }



