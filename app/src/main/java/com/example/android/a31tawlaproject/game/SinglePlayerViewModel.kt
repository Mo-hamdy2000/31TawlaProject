package com.example.android.a31tawlaproject.game

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.a31tawlaproject.miscUtils.Cell
import com.example.android.a31tawlaproject.miscUtils.MovePlayed

class SinglePlayerViewModel (application: Application) : GameViewModel(application) {

    private val playerOneMoves = mutableListOf<Cell>()
    private val preferredCells = mutableListOf<Cell>()
    private val singleCells = mutableListOf<Cell>()
    private val _roll = MutableLiveData<Boolean>(false)
    val roll: LiveData<Boolean>
        get() = _roll
    private var i = 0
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
            if ( piecesAtHomePlayer[1] == 15 && movesList.isNotEmpty()) {
                collectPieces()
            }
                switchTurns()
            }


    private fun validateMove(cell: Cell){

        if ( piecesAtHomePlayer[1] == 15|| (piecesAtHomePlayer[1] == 0 && cell.numberOfPieces.value == 14 )) {
            return
        }
            if (cell.numberOfPieces.value!! > 0 && cell.color == currentColor) {
                // awwel ma ydous 3ala piece at2akked ennaha bta3to w fi pieces fel 5ana di
                val iterator = movesList.iterator()
                while (iterator.hasNext()) {
                   val move = iterator.next()
                    if (cell.cellNumber + sign * move in 1..24) {
                        val destinationCell = cellsArray[cell.cellNumber + sign * move - 1]
                        if (destinationCell.color != currentColor || destinationCell.color == 0) {
                            sourceCell = cell
                            move(destinationCell)
                            Thread.sleep(1500)
                            iterator.remove()
                            i--
                            break
                        }
                    }
                }

        }
        }



        override fun move(cell: Cell) {
            if(currentColor==1)
                unhighlightMove()
            addPiece(cell)
            removePiece(sourceCell)
            if (cell.cellNumber !in playersCells[currentColor-1]) {
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
            if (currentColor == 1) {
                //s-
                playerOneMoves.add(cell)
                //-s
                if (sourceCell.cellNumber < 19 && cell.cellNumber >= 19) {
                    piecesAtHomePlayer[0] += 1
                    undoList.peek().pieceMovedToHome = true
                }
            }
            else {
                //s-
                if(sourceCell.numberOfPieces.value == 1){
                    preferredCells.remove(sourceCell)
                    singleCells.add(sourceCell)
                }
                else if(sourceCell.numberOfPieces.value ==0)
                    singleCells.remove(sourceCell)

                if(cell.numberOfPieces.value == 2){
                    preferredCells.add(cell)
                    singleCells.remove(cell)
                }
                else if(cell.numberOfPieces.value == 1)
                    singleCells.add(cell)
                //-s

                if (sourceCell.cellNumber > 6 && cell.cellNumber <= 6) {
                    piecesAtHomePlayer[1] ++
                    undoList.peek().pieceMovedToHome = true
                }
            }

        }


    override fun switchTurns() {
        super.switchTurns()
        if (currentColor == 1) {
            sign = -1
            println("player 2")
            computerTurn = true
            currentColor = 2
            _roll.value = true
            computerMove()
            _roll.value = false
        }
        else {
            sign = 1
            println("player 1")
            currentColor = 1
            computerTurn = false
        }
    }


    }



