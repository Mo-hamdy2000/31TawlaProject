package com.example.android.a31tawlaproject.game

import android.app.Application
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SinglePlayerViewModel(application: Application) : GameViewModel(application) {
    private val uiScope = CoroutineScope(Dispatchers.Main)
    private val preferredCells: Array<MutableList<Int>> = Array(2) { mutableListOf<Int>() }
    private val singleCells: Array<MutableList<Int>> = Array(2) { mutableListOf<Int>() }
    private val preferredDestinationCells: Array<MutableList<Int>> =
        Array(2) { mutableListOf<Int>() }
    private val singleDestinationCells: Array<MutableList<Int>> = Array(2) { mutableListOf<Int>() }
init {
    preferredCells[0].add(24)
}
    override fun switchTurns() {
        super.switchTurns()
        if (currentColor.value == 1)
            check()
        else {
            computerTurn = true
            uiScope.launch {
                _isUndoEnabled.value = false
                getComputerMoves()
                    delay(2000)
                    for (i in 0..1) {
                        preferredCells[i].clear()
                        singleCells[i].clear()
                        preferredDestinationCells[i].clear()
                        singleDestinationCells[i].clear()
                    }

                    computerTurn = false
                   switchTurns()

            }

        }
    }

    private suspend fun playMove(smallerList: Int, biggerList: Int) { // 0 or 1
        // movePossibleCells[movesListIndex].sortWith(MoveComparator())

        //here
        if ((piecesAtHomePlayer[1] == 15)) {
            collectPieces()
            switchTurns()
            return
        }
    //lamma awwel piece ted5ol elhome
        if(piecesAtHomePlayer[1]==1 && cellsArray[23].numberOfPieces.value==14 && cellsArray[23- movesList[biggerList]].color!=oppositeColor() ){
            preferredCells[biggerList].add(24)
            preferredCells[biggerList].add(24- movesList[biggerList])
        }
        singleCells[smallerList].sortDescending()
        preferredCells[smallerList].sortDescending()
        val destinationCellIndex: Int
        val sourceCellIndex: Int
        if (preferredCells[smallerList].isNotEmpty()) {
            Toast.makeText(
                getApplication(),
                "Preferred " + preferredCells[smallerList].toString(),
                Toast.LENGTH_SHORT
            ).show()
            sourceCellIndex = preferredCells[smallerList][0]
            sourceCell = cellsArray[sourceCellIndex - 1]
            destinationCellIndex = sourceCellIndex - movesList[smallerList]
            preferredCells[smallerList].remove(sourceCellIndex)
            preferredDestinationCells[smallerList].remove(destinationCellIndex)
        } else {
            if (singleCells[smallerList].size == 0)
                return
            Toast.makeText(
                getApplication(),
                "Single " + singleCells[smallerList].toString(),
                Toast.LENGTH_SHORT
            ).show()
            sourceCellIndex = singleCells[smallerList][0]
            sourceCell = cellsArray[sourceCellIndex - 1]
            destinationCellIndex = sourceCellIndex - movesList[smallerList]

            singleCells[smallerList].remove(sourceCellIndex)
            singleDestinationCells[smallerList].remove(destinationCellIndex)
            //PREFERRED CELLS FILTER
            //law elpiece elli et7arraket sabet makanha fady w kanet yenfa3 al3abha belmove eltania
            if (singleDestinationCells[biggerList].contains(sourceCellIndex)) {
                preferredCells[biggerList].add(sourceCellIndex + movesList[biggerList])
                preferredDestinationCells[biggerList].add(sourceCellIndex )
                singleCells[biggerList].remove(sourceCellIndex + movesList[biggerList])
                singleDestinationCells[biggerList].remove(sourceCellIndex )
            }
        }
        delay(1500)

        move(cellsArray[destinationCellIndex - 1])

        //SINGLE CELLS FILTER
        //law elcell di mawgouda fel possible moves bta3et elmove eltania w mafeesh
        //8eir piece wa7da ashelha
    if (singleCells[biggerList].isNotEmpty() && sourceCell.numberOfPieces.value == 0) {
        singleCells[biggerList].remove(sourceCellIndex)
        singleDestinationCells[biggerList].remove(destinationCellIndex)
    }

        override fun move(cell: Cell) {
            if(currentColor==1)
                unhighlightMove()
            removePiece(sourceCell)
            addPiece(cell)
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
    //law elle3ba elli etla3abet di fata7etlo cell yel3ab menha elmove eltania
    if (destinationCellIndex - movesList[biggerList] in 1..24 && cellsArray[destinationCellIndex - movesList[biggerList] - 1].color == 0) { //no of pieces must be 1
        singleCells[biggerList].add(destinationCellIndex)
        singleDestinationCells[biggerList].add(destinationCellIndex - movesList[biggerList])
    }


    //PREFERRED CELLS FILTER
    //law elcell elli la3abtelha di kanet preferred cell ashelha OR elcell elli la3abtaha ba2a feha piece wa7da
    if (preferredCells[biggerList].isNotEmpty() && preferredDestinationCells[biggerList].contains(destinationCellIndex)
        || sourceCell.numberOfPieces.value == 1
    ) {
        preferredCells[biggerList].remove(destinationCellIndex + movesList[biggerList]) //elsource cell bta3etha
        preferredDestinationCells[biggerList].remove(destinationCellIndex)
        singleCells[biggerList].add(destinationCellIndex + movesList[biggerList])
        singleDestinationCells[biggerList].add(destinationCellIndex)
}
//        if (movesPlayed == movesList.size) {
//            delay(2000)
//            switchTurns()
//            for (i in 0..1) {
//                preferredCells[i].clear()
//                singleCells[i].clear()
//                preferredDestinationCells[i].clear()
//                singleDestinationCells[i].clear()
//            }
//
//            computerTurn = false
//            movesPlayed = 0
//        }

    }


    private suspend fun getComputerMoves() {
        if (piecesAtHomePlayer[1] == 15)
            return
//        Toast.makeText(
//            getApplication(),
//            "Player Cells" + playersCells[1].toString(),
//            Toast.LENGTH_SHORT
//        ).show()
        for (cellNum in playersCells[1]) {
            for (i in 0 ..1) {
                val sourceCell = cellsArray[cellNum - 1]
                //here
                val destinationCellIndex = cellNum - movesList[i]
                if (destinationCellIndex in 1..24 && cellsArray[destinationCellIndex - 1].color != oppositeColor()) {
                    if(cellNum == 24 && piecesAtHomePlayer[1]==0)
                        continue
                    if (sourceCell.numberOfPieces.value!! > 1 && cellsArray[destinationCellIndex - 1].color == 0) {
                        preferredCells[i].add(cellNum)
                        preferredDestinationCells[i].add(cellNum - movesList[i])
                    } else {
                        singleCells[i].add(cellNum)
                        singleDestinationCells[i].add(cellNum - movesList[i])
                    }
                }
                //kefaya a7seb move wa7da heyya elli ashta8al 3aleiha
                if(movesList.size == 4)
                    break
            }
        }

//        if (piecesAtHomePlayer[1] == 0) {
//            if (cellsArray[23].numberOfPieces.value == 14) {
//                preferredCells[0].remove(24)
//                preferredCells[1].remove(24)
//                singleCells[0].remove(24)
//                singleCells[1].remove(24)
//            } else if (cellsArray[23].numberOfPieces.value == 15)
//                preferredCells[1].remove(24)
//                singleCells[1].remove(24)
//        }
//        if (homeFirstTime > 0) {
//
//            when (homeFirstTime) {
//                4 -> {
//                    if (cellsArray[23 - movesList[0]].color != 1)
//                        for (i in 0..3) {
//                            playMove(0, 0)
//                        }
//                    else {
//                        playMove(0, 0)
//                        preferredCells[0].add(23)
//                    }
//                }
//
//                2 -> {
//                    if (cellsArray[23 - movesList[0]].color == 0) {// ella3eb eltani mesh afel 3aleih y7arrak men cell 23
//                        playMove(1, 0)
//                        preferredCells[0].add(23)
//                        playMove(0, 1)
//                    } else {
//                        playMove(0, 1)
//                        if (cellsArray[23 - movesList[1]].color == 0)
//                            preferredCells[1].add(23)
//                        playMove(1, 0)
//                    }
//                }
//
//                1 -> {
//                    for (i in 0..1) {
//                        if (singleCells[i].isEmpty()) {
//                            playMove(i xor 1, i)
//                            if (cellsArray[23 - movesList[i]].color == i) {
//                                preferredCells[i].add(23)
//                                playMove(i, i xor 1)
//                            }
//                        }
//                    }
//                }
//            }
//            return
//        }
//        for (i in 0..1) {
//            if (movePossibleCells[i].size == 0)
//                movesList.remove(movesList[i])
//        }
        val size1 = singleCells[0].size + preferredCells[0].size
        val size2 = singleCells[1].size + preferredCells[1].size

        if(size1+size2==0) {
//                switchTurns()
//            computerTurn = false
//            movesPlayed = 0
                return
            }
        if (movesList.size == 4) {
            for (i in 0..3) {
                playMove(0, 0) // baddilo nafs ellist kol marra
            }
            return
        }

        when { //law elsize beta3 7add fihom b zero al3ab eltani elawwel momken yefta7li el move eltania
            // ( law elcell di hatrou7 elhome ya3ni aw hal3ab bnafs elpiece men makanha elgdeid )
            size1 == 0 -> {
                playMove(1, 0)
                playMove(0, 1)
            }
            size2 == 0 -> {
                playMove(0, 1)
                playMove(1, 0)
            }
            else -> {
                //8eir keda al3ab men elli elsize beta3ha a2all 3ashan law move metkarrara feletnein ma2felsh elmove eltania
                val smallerList = if (size1 < size2)
                    0
                else
                    1
                playMove(smallerList, smallerList xor 1)
                playMove(smallerList xor 1, smallerList)
            }
        }
//    if(firstMovePossibleCells.size == 1 && secondMovePossibleCells.contains(firstMovePossibleCells[0]) )
//        secondMovePossibleCells.remove(firstMovePossibleCells[0])
//    if(secondMovePossibleCells.size == 1 && firstMovePossibleCells.contains(secondMovePossibleCells[0]) )
//        firstMovePossibleCells.remove(secondMovePossibleCells[0])
    }
}




