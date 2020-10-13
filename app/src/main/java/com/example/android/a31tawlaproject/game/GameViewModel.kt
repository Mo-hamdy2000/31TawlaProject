package com.example.android.a31tawlaproject.game

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.a31tawlaproject.miscUtils.Cell
import com.example.android.a31tawlaproject.miscUtils.MovePlayed
import com.example.android.a31tawlaproject.miscUtils.*
import java.util.*
import kotlin.math.abs

abstract class GameViewModel(application: Application) : AndroidViewModel(
    application
) {

    private var startingPointSelected = false // -> flag for selecting source cell
    lateinit var sourceCell : Cell
    var sign = 1
    var computerTurn = false
    ///dice variables
    private var isMoved = false
    //undo variables
    val isUndoEnabled: LiveData<Boolean>
        get() = _isUndoEnabled
    val playersCells: Array<MutableList<Int>> = Array(2) {
        mutableListOf<Int>()
    }

    //static members
    companion object{

        var read = false
        lateinit var cellsArray: Array<Cell>
        val piecesAtHomePlayer: Array<Int> = Array(2) { 0 }
        val piecesCollectedPlayer: Array<Int> = Array(2) { 0 }
        private var _scoreOne = MutableLiveData<Int> (0)
        val scoreOne: LiveData<Int>
            get() = _scoreOne
        private var _scoreTwo = MutableLiveData<Int> (0)
        val scoreTwo: LiveData<Int>
            get() = _scoreTwo
        var currentColor = 1
        var gameMode = 0
        val movesList = mutableListOf<Int>()
        var diceRolled = false
        val undoList = Stack<MovePlayed>()
        var _isUndoEnabled = MutableLiveData<Boolean>(false)
        var endGame = MutableLiveData<Boolean>(false)

        fun writeArray(): String {
            //TOO MUCHHH
            //CODE 3ERRAAAAAAAAAA
            val sb = StringBuilder()
            sb.append(gameMode).append(" ")
            for (cell in cellsArray) {
                sb.append(cell.numberOfPieces.value).append(" ").append(cell.color).append(" ")
            }
            sb.append(piecesAtHomePlayer[0]).append(" ").append(piecesAtHomePlayer[1]).append(" ")
                .append(piecesCollectedPlayer[0]).append(" ").append(piecesCollectedPlayer[1])
                .append(" ")
                .append(scoreOne).append(" ").append(scoreTwo).append(" ")
                .append(currentColor).append(" ")
                .append(diceRolled).append(" ").append(diceOneVal).append(" ").append(diceTwoVal).append(" ")
                .append(movesList.size).append(" ")
            for (move in movesList)
                sb.append(move).append(" ")
            //undo
            while(undoList.isNotEmpty()){
                val temp = undoList.pop()
                sb.append(temp.sourceCellNo).append(" ")
                    .append(temp.destCellNo).append(" ")
                    .append(temp.pieceMovedToHome).append(" ")
            }
            return sb.toString()
        }

        fun readArray(string: String?) { //sets read to false if the file was empty " nothing is saved "

            if(string==null) {
                read = false
                return
            }
            val st = StringTokenizer(string)
            gameMode = st.nextToken().toInt()
            cellsArray = Array(24) {
                Cell(
                    it + 1,
                    MutableLiveData(st.nextToken().toInt()),
                    st.nextToken().toInt(),
                    MutableLiveData(false),
                    MutableLiveData(false)
                )
            }

            piecesAtHomePlayer[0] = st.nextToken().toInt()
            piecesAtHomePlayer[1] = st.nextToken().toInt()

            piecesCollectedPlayer[0] = st.nextToken().toInt()
            piecesCollectedPlayer[1] = st.nextToken().toInt()

            _scoreOne.value = st.nextToken().toInt()
            _scoreTwo.value = st.nextToken().toInt()

            currentColor = st.nextToken().toInt()

            diceRolled = st.nextToken()!!.toBoolean()
            diceOneVal = st.nextToken().toInt()
            diceTwoVal = st.nextToken().toInt()
            val movesSize = st.nextToken().toInt()
            movesList.clear()
            for (i in 0 until movesSize)
                movesList.add(st.nextToken().toInt())
            while(st.hasMoreTokens()) {
                val undo= MovePlayed(st.nextToken().toInt(),st.nextToken().toInt(), st.nextToken()!!.toBoolean())
                undoList.push(undo)
            }
            if(undoList.isNotEmpty())
                _isUndoEnabled.value = true

            read = true
        }

        fun getWinner(): String{
            if (scoreOne.value!! > scoreTwo.value!!) {
                return "Player 1"
            }
            else if (scoreOne.value!! < scoreTwo.value!!) {
                return "Player 2"
            }
            return "Absolutely No Winner"
        }

        fun getScore(): Array<Int> {
            if (scoreOne.value!! > scoreTwo.value!!) {
                return arrayOf(scoreOne.value!!, scoreTwo.value!!)
            }
            return arrayOf(scoreTwo.value!!, scoreOne.value!!)
        }
    }

    private var oneMoveOnly = false

    init {

        if (!read) {
            cellsArray=Array(24) {
                Cell(it + 1, MutableLiveData(0), 0, MutableLiveData(false), MutableLiveData(false))
            }
            resetGame()
        }
        else{
            sign = if(currentColor==1)
                1
            else -1
            for(cell in cellsArray){
                if(cell.color==1)
                    playersCells[0].add(cell.cellNumber)
                else if(cell.color==2)
                    playersCells[1].add(cell.cellNumber)
            }
        }
    }

    fun selectCell(cell: Cell) {
        println("Wslnaaaaaaaaaaaaaaa")
        if (!diceRolled ||computerTurn|| (piecesAtHomePlayer[currentColor - 1] == 15)) {
            return
        }
        if (!startingPointSelected) {
            // Zwdt condition 3ashan awl stage fe el l3ba
            // El la3b lazem yl3b b piece w7da w ywslha el n7ia el tania
            // Fa hwa msh hy2dr y7rk piece mn el home elly feha 14 piece l8ayt ma ykhls el first move dy
            if (piecesAtHomePlayer[currentColor - 1] == 0 && cell.numberOfPieces.value == 14) {
                return
            }
            if (cell.numberOfPieces.value!! > 0 && cell.color == currentColor) {
                // awwel ma ydous 3ala piece at2akked ennaha bta3to w fi pieces fel 5ana di
                if (getPossibleMoves(cell)) {
                    startingPointSelected = true
                    cell.isPieceHighlighted.value = true
                    sourceCell = cell
                }
            }
        } else {
            isMoved = false
            for (move in movesList) {
                if (sourceCell.cellNumber + sign * move in 1..24) {
                    val destinationCell = cellsArray[sourceCell.cellNumber + sign * move - 1]
                    if (cell.cellNumber == destinationCell.cellNumber && (destinationCell.color != oppositeColor())) {
                        // lamma ydous 3ala cell tania ba3d elawwalaneyya at2akkedd ennaha men el highlighted
                        move(cell)
                        movesList.remove(move)
                        isMoved = true
                        if (oneMoveOnly) {
                            oneMoveOnly = false
                            movesList.clear()
                        }
                        if ((piecesAtHomePlayer[currentColor - 1] == 15) && movesList.isNotEmpty()) {
                            collectPieces()
                            return
                        }
                        break
                    }
                }
            }
            //law e5tar cell tania 5ales ya3ni 3ayez y8ayyer el source cell
            if (!isMoved && cell.numberOfPieces.value!! > 0 && cell.color == currentColor) {
                // s- 5adt rl condition di copy hena kaman
                if (piecesAtHomePlayer[currentColor - 1] == 0 && cell.numberOfPieces.value == 14)
                    return
                unhighlightMove()
                //-s
                if (getPossibleMoves(cell)) {
                    startingPointSelected = true
                    cell.isPieceHighlighted.value = true
                    sourceCell = cell
                }
                else {
                    Toast.makeText(getApplication(), "No possible moves here", Toast.LENGTH_SHORT).show()
                }
            }

        }
        if (movesList.size == 0)
            switchTurns()
    }

    private fun oppositeColor(): Int {
        return currentColor +  sign
    }

    fun check () {
        if (movesList.size == 2)
            checkTwoMoves()
        else if (movesList.size == 4)
            checkFourMoves()
    }

    private fun checkTwoMoves() {
        val firstMove = movesList[0]
        val secondMove = movesList[1]
        // Check if one piece can do the two moves
        for (cellNum in playersCells[currentColor - 1]) {
            if ((cellNum == 1 && piecesAtHomePlayer[currentColor - 1] == 0 && cellsArray[cellNum - 1].numberOfPieces.value == 14) ||
                (cellNum == 24 && piecesAtHomePlayer[currentColor - 1] == 0 && cellsArray[cellNum - 1].numberOfPieces.value == 14)) {
                continue
            }
            if ((cellNum + sign * firstMove + sign * secondMove) in 1.. 24 && cellsArray[cellNum + sign * firstMove + sign * secondMove - 1].color != oppositeColor() &&
                (cellsArray[cellNum + sign * firstMove - 1].color != oppositeColor() || cellsArray[cellNum + sign * secondMove - 1].color != oppositeColor())) {
                return
            }
        }
        val firstMovePossibleCells = mutableListOf<Int>()
        val secondMovePossibleCells = mutableListOf<Int>()
        var homeFlag = false
        for (cellNum in playersCells[currentColor - 1]) {
            if ((cellNum + sign * firstMove) in 1..24 && cellsArray[cellNum + sign * firstMove - 1].color != oppositeColor()) {
                firstMovePossibleCells.add(cellNum)
                if (((cellNum + sign * firstMove) in 19..24 && currentColor == 1) || ((cellNum + sign * firstMove) in 1..6 && currentColor == 2))
                    homeFlag = true
            }

            if ((cellNum + sign * secondMove) in 1..24 && cellsArray[cellNum + sign * secondMove - 1].color != oppositeColor()) {
                secondMovePossibleCells.add(cellNum)
                if (((cellNum + sign * secondMove) in 19..24 && currentColor == 1) || ((cellNum + sign * secondMove) in 1..6 && currentColor == 2))
                    homeFlag = true
            }
        }
//maycollectesh
        if (homeFlag && (piecesAtHomePlayer[currentColor - 1] == 14)) {
            return
        }
        // Bt2kd Eno my7sbsh el pieces elly fe el home lw lsa mraw7sh haga
        //m-
        // note that expression ((( currentColor * (23/3) - sign * (23/3) ))) gets the start cell for each player just it compresses the code
        //-s
        //5alletha 3.0 bas 3ashan kan bye3mel integer division w beyragga3 21 fi 7alet player 2
        if (piecesAtHomePlayer[currentColor - 1] == 0 && cellsArray[(currentColor * (23 / 3.0) - sign * (23 / 3.0)).toInt()].numberOfPieces.value == 14
            && !homeFlag) {
            firstMovePossibleCells.remove(1)
            secondMovePossibleCells.remove(1)
            firstMovePossibleCells.remove(24)
            secondMovePossibleCells.remove(24)
        }

        println(firstMovePossibleCells)
        println(secondMovePossibleCells)

        if (firstMovePossibleCells.size == 0 || secondMovePossibleCells.size == 0) {
            if (firstMovePossibleCells.size == 0 && secondMovePossibleCells.size == 0) {
                switchTurns()
            }
            if (firstMovePossibleCells.size == 0) {
                Toast.makeText(getApplication(), "YOU CAN'T MOVE " + firstMove, Toast.LENGTH_SHORT).show()
                movesList.remove(firstMove)
            }

            if (secondMovePossibleCells.size == 0) {
                Toast.makeText(getApplication(), "YOU CAN'T MOVE " + secondMove, Toast.LENGTH_SHORT).show()
                movesList.remove(secondMove)
            }
        } else {
            //law nafs elpossible move feletnein w mafeesh 8eir piece wa7daa
            if (firstMovePossibleCells.size == 1 && secondMovePossibleCells.size == 1
                && firstMovePossibleCells[0] == secondMovePossibleCells[0] && cellsArray[firstMovePossibleCells[0] - 1].numberOfPieces.value == 1) {
                Toast.makeText(getApplication(), "YOU CAN ONLY PLAY ONE MOVE", Toast.LENGTH_SHORT).show()
                oneMoveOnly = true
            }
        }
    }

    private fun checkFourMoves() {
        var movesNum = 0
        val move = movesList[0]
        var homeComing = 0
        var startMoves = 0
        for (cellNum in playersCells[currentColor - 1]) {
            var temp = 0
            for (i in 1..4) {
                if ((cellNum + sign * move * i) in 1..24 && cellsArray[cellNum + sign * move * i - 1].color != oppositeColor()) {
                    temp += cellsArray[cellNum - 1].numberOfPieces.value!!
                    if ((cellNum + sign * move * i in 19..24 && currentColor == 1) ||(cellNum + sign * move * i in 1..6 && currentColor == 2)) {
                        homeComing++
                    }
                } else {
                    break
                }
            }
            movesNum += temp
            if (cellNum ==1 || cellNum == 24) {
                startMoves = temp
            }
        }

        if (piecesAtHomePlayer[currentColor - 1] == 0 && cellsArray[(currentColor * (23 / 3.0) - sign * (23 / 3.0)).toInt()].numberOfPieces.value == 14 && homeComing < 1) {
            println(movesNum)
            movesNum -= startMoves
            println(movesNum)
        }

        if (piecesAtHomePlayer[currentColor - 1] + homeComing == 15) {
            return
        }

        if (movesNum < 4) {
            Toast.makeText(
                getApplication(),
                "YOU CAN'T PLAY " + (4 - movesNum) + " MOVE",
                Toast.LENGTH_SHORT
            ).show()
            for (i in 1..(4-movesNum)) {
                movesList.remove(move)
            }
        }
        if (movesNum == 0) {
            switchTurns()
        }
    }

    abstract fun move(cell: Cell)

    private fun getPossibleMoves(selectedCell: Cell): Boolean {
        var isPossible = false
        for (move in movesList) {
            if (selectedCell.cellNumber + sign * move in 1..24) {
                val destinationCell = cellsArray[selectedCell.cellNumber + sign * move - 1]
                if (destinationCell.color != oppositeColor()) {
                    destinationCell.isCellHighlighted.value = true
                    cellsArray[selectedCell.cellNumber + sign * move - 1] = destinationCell
                    isPossible = true
                }
            }
        }
        return isPossible
    }

    open fun switchTurns() {
        undoList.clear()
        movesList.clear()
        _isUndoEnabled.value = false
    }

    fun addPiece(cell: Cell){
        startingPointSelected = false
        cell.color = currentColor
        cell.numberOfPieces.value = cell.numberOfPieces.value!! + 1
    }

    fun removePiece(cell: Cell){
        cell.numberOfPieces.value = cell.numberOfPieces.value!! - 1
        if (cell.numberOfPieces.value == 0)
            cell.color = 0
    }

    fun undo() {
        val movePlayed = undoList.pop()
        if (startingPointSelected) {
            unhighlightMove()
        }
        addPiece(cellsArray[movePlayed.sourceCellNo - 1])
        removePiece(cellsArray[movePlayed.destCellNo - 1])
        if (!(movePlayed.sourceCellNo in playersCells[currentColor - 1])) {
            playersCells[currentColor - 1].add(movePlayed.sourceCellNo)
        }
        if (cellsArray[movePlayed.destCellNo - 1].numberOfPieces.value == 0) {
            playersCells[currentColor - 1].remove(movePlayed.destCellNo)
        }
        movesList.add(abs(movePlayed.destCellNo - movePlayed.sourceCellNo))
        if (undoList.empty()) {
            _isUndoEnabled.value = false
        }
    }

    fun collectPieces() {
        if (currentColor == 1) {
            for (move in movesList) {
                if (cellsArray[24 - move].numberOfPieces.value!! > 0 && cellsArray[24 - move].color == currentColor) {
                    removePiece(cellsArray[24 - move])
                    piecesCollectedPlayer[0]++
                    continue
                }
                var isPlayed = false
                for (i in (24 - move) downTo 19) {
                    if (i >= 19 && i + move < 25 && cellsArray[i - 1].numberOfPieces.value!! > 0
                        && cellsArray[i - 1].color == currentColor && cellsArray[i + move - 1].color != 2) {
                        addPiece(cellsArray[i + move - 1])
                        removePiece(cellsArray[i - 1])
                        isPlayed = true
                        break
                    }
                }
                if (!isPlayed) {
                    for (i in (25 - move) .. 24) {
                        if (i <= 24 && cellsArray[i - 1].numberOfPieces.value!! > 0 && cellsArray[i - 1].color == currentColor) {
                            removePiece(cellsArray[i - 1])
                            println("The cell " + i + " is collected")
                            piecesCollectedPlayer[0] ++
                            break
                        }
                    }
                }
            }
        } else {
            for (move in movesList) {
                if (cellsArray[move - 1].numberOfPieces.value!! > 0 && cellsArray[move - 1].color == currentColor) {
                    removePiece(cellsArray[move - 1])
                    println("The cell " + move + " is collected")
                    piecesCollectedPlayer[1]++
                    continue
                }
                var isPlayed = false
                for (i in (move + 1) .. 6) {
                    if (i - move > 0 && cellsArray[i - 1].numberOfPieces.value!! > 0 && cellsArray[i - 1].color == currentColor
                        && cellsArray[i - move - 1].color != 1) {
                        addPiece(cellsArray[i - move - 1])
                        removePiece(cellsArray[i - 1])
                        isPlayed = true
                        break
                    }
                }
                if (!isPlayed) {
                    for (i in (move - 1) downTo 1) {
                        println("cell " + i + "no of pieces " + cellsArray[i - 1].numberOfPieces.value)
                        if (i > 0 && cellsArray[i - 1].numberOfPieces.value!! > 0 && cellsArray[i - 1].color == currentColor) {
                            removePiece(cellsArray[i - 1])
                            println("The cell " + i + " is collected")
                            piecesCollectedPlayer[1]++
                            break
                        }
                    }
                }
            }
        }
        movesList.clear()
        println(piecesCollectedPlayer[currentColor - 1])
        if (piecesCollectedPlayer[currentColor - 1] == 15) {
            endRound()
        }
        else {
            switchTurns()
        }
    }

    fun unhighlightMove(){
        for (move in movesList) {
            if (sourceCell.cellNumber + sign * move in 1..24) {
                cellsArray[sourceCell.cellNumber + sign * move - 1].isCellHighlighted.value = false
            }
        }
        sourceCell.isPieceHighlighted.value = false
    }

    fun resetGame() {
        println("Hereeeeeeeeeeeeeeeee we end Again")
        piecesAtHomePlayer[0] = 0
        piecesAtHomePlayer[1] = 0
        piecesCollectedPlayer[0] = 0
        piecesCollectedPlayer[1] = 0
        for (cell in cellsArray) {
            cell.numberOfPieces.value = 0
            cell.color = 0
            cell.isCellHighlighted.value = false
            cell.isPieceHighlighted.value = false
        }
        movesList.clear()
        diceRolled = false
        currentColor = 2
        addPiece(cellsArray[23])
        cellsArray[23].numberOfPieces.value = 15

        currentColor = 1
        addPiece(cellsArray[0])
        cellsArray[0].numberOfPieces.value = 15

        playersCells[0].add(1)
        playersCells[1].add(24)
    }

    private fun endRound() {
        println("Hereeeeeeeeeeeeeeeee we end")
        var winner: Int
        if (currentColor == 1) {
            _scoreOne.value = scoreOne.value!! + (15 - piecesCollectedPlayer[1])
            winner = 1
            if (scoreOne.value!! >= 31) {
                endGame.value = true
            }
        }
        else {
            _scoreTwo.value = scoreTwo.value!! + (15 - piecesCollectedPlayer[0])
            winner = 2
            if (scoreTwo.value!! >= 31) {
                endGame.value = true
            }
        }
        resetGame()
        currentColor = winner
    }
}