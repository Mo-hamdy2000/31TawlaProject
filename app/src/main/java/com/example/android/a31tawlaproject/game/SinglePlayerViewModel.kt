package com.example.android.a31tawlaproject.game

import android.app.Application
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.StringBuilder
import java.util.*

class SinglePlayerViewModel(application: Application) : GameViewModel(application) {
    private val uiScope = CoroutineScope(Dispatchers.Main)
    private val preferredCells: Array<MutableList<Int>> = Array(2) { mutableListOf<Int>() }
    private val singleCells: Array<MutableList<Int>> = Array(2) { mutableListOf<Int>() }
    private val preferredDestinationCells: Array<MutableList<Int>> =
        Array(2) { mutableListOf<Int>() }
    private val singleDestinationCells: Array<MutableList<Int>> = Array(2) { mutableListOf<Int>() }
    private val movesPlayed = mutableListOf<Int>()
    val sb = StringBuilder()
init {
    if(cellsArray[23].numberOfPieces.value == 15) // zawwedt elcondition da 3ashan elsave welload
    preferredCells[0].add(24)
}
    override suspend fun switchTurns() {
        super.switchTurns()
       if(currentColor.value == 2) {
            computerTurn = true
            uiScope.launch {
                _isUndoEnabled.value = false
                getComputerMoves()
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
        else
               check()
    }

    private suspend fun playMove(smallerList: Int, biggerList: Int) { // 0 or 1
        // movePossibleCells[movesListIndex].sortWith(MoveComparator())
        //here
        if ((piecesAtHomePlayer[1] == 15)) {
            movesList.removeAll(movesPlayed)
            Log.i("MOVES", movesList.toString())
            collectPieces()
            return
        }
    //lamma awwel piece ted5ol elhome
        sb.clear()

        if( cellsArray[23].numberOfPieces.value==14 && piecesAtHomePlayer[1]==1 && cellsArray[23- movesList[biggerList]].color!=oppositeColor() ){
            sb.append("PR at 1 is 24 \n" )
            preferredCells[biggerList].add(24)
            preferredDestinationCells[biggerList].add(24- movesList[biggerList])
        }

        singleCells[smallerList].sortByDescending {
            cellsArray[it-1].numberOfPieces.value
        }
        preferredCells[smallerList].sort()
        Log.i("I_HATE_YOU", movesList[smallerList].toString() + singleCells[smallerList].toString() +preferredCells[smallerList].toString())
        val destinationCellIndex: Int
        val sourceCellIndex: Int

        if (preferredCells[smallerList].isNotEmpty()) {
         //   Toast.makeText(getApplication(), "Preferred " + preferredCells[smallerList].toString(), Toast.LENGTH_SHORT).show()

            sourceCellIndex = preferredCells[smallerList][0]
            sourceCell = cellsArray[sourceCellIndex - 1]
            destinationCellIndex = sourceCellIndex - movesList[smallerList]

            //el 4 lines dol 3ashan el 4 moves bab3atlo kol marra nafs el 2 lists kol marra fa lazem lamma al3ab le3ba ashelha
            if(movesList.size ==4) {
                preferredCells[smallerList].remove(sourceCellIndex)
                preferredDestinationCells[smallerList].remove(destinationCellIndex)
                singleCells[smallerList].add(sourceCellIndex)
                singleDestinationCells[smallerList].add(destinationCellIndex)
                sb.append("SN at 2 is $sourceCellIndex \n" )

            }
        } else {
            if (singleCells[smallerList].size == 0)
                return
         //   Toast.makeText(getApplication(), "Single " + singleCells[smallerList].toString(), Toast.LENGTH_SHORT).show()

            sourceCellIndex = singleCells[smallerList][0]
            sourceCell = cellsArray[sourceCellIndex - 1]
            destinationCellIndex = sourceCellIndex - movesList[smallerList]

            if(movesList.size ==4) {
                if (cellsArray[sourceCellIndex - 1].numberOfPieces.value == 0) {
                    singleCells[smallerList].remove(sourceCellIndex)
                    singleDestinationCells[smallerList].remove(destinationCellIndex)
                }
            }
            //PREFERRED CELLS FILTER
            //law elpiece elli et7arraket sabet makanha fady w kanet yenfa3 al3abha belmove eltania
            if (singleDestinationCells[biggerList].contains(sourceCellIndex)) {
                preferredCells[biggerList].add(sourceCellIndex + movesList[biggerList])
                preferredDestinationCells[biggerList].add(sourceCellIndex )
                sb.append("PR at 3 is ${sourceCellIndex+ movesList[biggerList]} \n" )
                singleCells[biggerList].remove(sourceCellIndex + movesList[biggerList])
                singleDestinationCells[biggerList].remove(sourceCellIndex )
            }
        }
        delay(1500)

        move(cellsArray[destinationCellIndex - 1])
        movesPlayed.add(movesList[smallerList])
        //SINGLE CELLS FILTER
        //law elcell di mawgouda fel possible moves bta3et elmove eltania w mafeesh
        //8eir piece wa7da ashelha
    if (singleCells[biggerList].isNotEmpty() && sourceCell.numberOfPieces.value == 0) {
        singleCells[biggerList].remove(sourceCellIndex)
        singleDestinationCells[biggerList].remove(destinationCellIndex)
    }

    //law elle3ba elli etla3abet di fata7etlo cell yel3ab menha elmove eltania
    if (destinationCellIndex - movesList[biggerList] in 1..24 && cellsArray[destinationCellIndex - movesList[biggerList] - 1].color == 0) { //no of pieces must be 1
        singleCells[biggerList].add(destinationCellIndex)
        singleDestinationCells[biggerList].add(destinationCellIndex - movesList[biggerList])
        sb.append("SN at 4 is $destinationCellIndex \n" )
    }
//law la3abt fi cell feha 1 piece w ba2wt preferred
        if(singleCells[biggerList].contains(destinationCellIndex) && cellsArray[destinationCellIndex - movesList[biggerList]-1].color == 0){
            preferredCells[biggerList].add(destinationCellIndex)
            preferredDestinationCells[biggerList].add(destinationCellIndex- movesList[biggerList] )
            sb.append("pr at 5 is $destinationCellIndex \n" )
            singleCells[biggerList].remove(destinationCellIndex)
            singleDestinationCells[biggerList].remove(destinationCellIndex- movesList[biggerList] )
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
        sb.append("SN at 6 is ${destinationCellIndex + movesList[biggerList]} \n" )
}
        Log.i("LOOK AT ME", sb.toString())
    }


    private suspend fun getComputerMoves() {
        if (piecesAtHomePlayer[1] == 15)
            return
        Log.i("COMPUTERCELLS", playersCells[1].toString())
        for (cellNum in playersCells[1]) {
            for (i in 0 ..1) {
                val sourceCell = cellsArray[cellNum - 1]
                //here aaa
                val destinationCellIndex = cellNum - movesList[i]
                if (destinationCellIndex in 1..24 && cellsArray[destinationCellIndex - 1].color !=1) {
                    if(cellNum == 24 && piecesAtHomePlayer[1]==0)
                        continue
                    if (sourceCell.numberOfPieces.value!! > 1 && cellsArray[destinationCellIndex - 1].color == 0 && isOppositePlayerHere(cellNum)) {
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


        val size1 = singleCells[0].size + preferredCells[0].size
        val size2 = singleCells[1].size + preferredCells[1].size

        if(size1+size2==0)
                return
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

    }

    //bashouf law elplayer la3eb felcells elli oddam elcell elli hal3ab menha 3ashan law mesh mawgoud malhash ma3na a7gez elcell di
    private fun isOppositePlayerHere(cellNum : Int) : Boolean {
        for(i in playersCells[0])
            if(i < cellNum)
                return true
        return false
    }
}




