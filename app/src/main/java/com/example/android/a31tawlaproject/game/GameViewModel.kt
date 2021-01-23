package com.example.android.a31tawlaproject.game

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.a31tawlaproject.miscUtils.Cell
import com.example.android.a31tawlaproject.miscUtils.MovePlayed
import com.example.android.a31tawlaproject.miscUtils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.abs

abstract class GameViewModel(application: Application) : AndroidViewModel(
    application
) {
/*
player2> -1
player1> 1
 */
    private val uiScope = CoroutineScope(Dispatchers.Main)
    private var waitTime = 0
    private var startingPointSelected = false // -> flag for selecting source cell
    lateinit var sourceCell: Cell
    private var sign = 1
    var computerTurn = false

    ///dice variables

    //undo variables
    val isUndoEnabled: LiveData<Boolean>
        get() = _isUndoEnabled
    val playersCells: Array<MutableList<Int>> = Array(2) {
        mutableListOf<Int>()
    }

    //static members
    companion object {

        var read = false
        var isMoved = MutableLiveData<Boolean> (false)
        lateinit var cellsArray: Array<Cell>
        val piecesAtHomePlayer: Array<Int>  = Array(2){ 0 }
        val piecesCollectedPlayer: Array<Int> = Array(2) { 0 }
        private var _scoreOne = MutableLiveData<Int> (0)
        val scoreOne: LiveData<Int>
            get() = _scoreOne
        private var _scoreTwo = MutableLiveData<Int> (0)
        val scoreTwo: LiveData<Int>
            get() = _scoreTwo
        var currentColor = MutableLiveData (1)
        var gameMode = 0
        val movesList = mutableListOf<Int>()
       // var currentColor = MutableLiveData (1)
        var diceRolled = false
        val undoList = Stack<MovePlayed>()
        var _isUndoEnabled = MutableLiveData<Boolean>(false)
        var endGame = MutableLiveData<Boolean>(false)
        var movedFromCell = MutableLiveData(0)
        var movedToCell = MutableLiveData(0)
        var collectionStarted = Array<MutableLiveData<Boolean>>(2) {
            MutableLiveData(false)
        }
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
                .append(scoreOne.value).append(" ").append(scoreTwo.value).append(" ")
                .append(currentColor.value).append(" ")
                .append(diceRolled).append(" ").append(diceValues[0]).append(" ").append(diceValues[1])
                .append(" ")
                .append(movesList.size).append(" ")
            for (move in movesList)
                sb.append(move).append(" ")
            //undo
            while (undoList.isNotEmpty()) {
                val temp = undoList.pop()
                sb.append(temp.sourceCellNo).append(" ")
                    .append(temp.destCellNo).append(" ")
                    .append(temp.pieceMovedToHome).append(" ")
            }
            return sb.toString()
        }


        fun readArray(string: String?) { //sets read to false if the file was empty " nothing is saved "

            if (string == null) {
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

            currentColor.value = st.nextToken().toInt()

            diceRolled = st.nextToken()!!.toBoolean()
            diceValues[0] = st.nextToken().toInt()
            diceValues[1] = st.nextToken().toInt()
            val movesSize = st.nextToken().toInt()
            if((diceValues[0]== diceValues[1] && movesSize <=2 )|| movesSize < 2)
                isMoved.value = true
            movesList.clear()
            for (i in 0 until movesSize)
                movesList.add(st.nextToken().toInt())
            while (st.hasMoreTokens()) {
                val undo = MovePlayed(
                    st.nextToken().toInt(),
                    st.nextToken().toInt(),
                    st.nextToken()!!.toBoolean()
                )
                undoList.push(undo)
            }
            if (undoList.isNotEmpty())
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
            cellsArray = Array(24) {
                Cell(it + 1, MutableLiveData(0), 0, MutableLiveData(false), MutableLiveData(false))
            }
            //ragga3i dool
//            resetGame()
//            // w sheli men hena
            addPiece(cellsArray[18], 1)
            playersCells[0].add(19)

            addPiece(cellsArray[5],2)
            playersCells[1].add(6)

            cellsArray[18].numberOfPieces.value = 14
            cellsArray[5].numberOfPieces.value = 14

            addPiece(cellsArray[12],1)
            playersCells[0].add(13)



            addPiece(cellsArray[11],2)
            playersCells[1].add(12)



            piecesAtHomePlayer[0] = 14
            piecesAtHomePlayer[1] = 14
            // lhena
        }
        else{
            sign = if (currentColor.value == 1)
                1
            else -1
            for (cell in cellsArray) {
                if (cell.color == 1)
                    playersCells[0].add(cell.cellNumber)
                else if (cell.color == 2)
                    playersCells[1].add(cell.cellNumber)
            }
        }
    }

    fun selectCell(cell: Cell) {
        println("Wslnaaaaaaaaaaaaaaa")
        if (!diceRolled || computerTurn || (piecesAtHomePlayer[currentColor.value!! - 1] == 15)) {
            println("hna")
            return
        }
        if (!startingPointSelected) {
            // Zwdt condition 3ashan awl stage fe el l3ba
            // El la3b lazem yl3b b piece w7da w ywslha el n7ia el tania
            // Fa hwa msh hy2dr y7rk piece mn el home elly feha 14 piece l8ayt ma ykhls el first move dy
            if (piecesAtHomePlayer[currentColor.value!! - 1] == 0 && cell.numberOfPieces.value == 14) {
                println("hnakk")
                return
            }
            println("1")
            if (cell.numberOfPieces.value!! > 0 && cell.color == currentColor.value) {
                // awwel ma ydous 3ala piece at2akked ennaha bta3to w fi pieces fel 5ana di
                println("3")
                if (getPossibleMoves(cell)) {
                    println("4")
                    startingPointSelected = true
                    cell.isPieceHighlighted.value = true
                    sourceCell = cell
                }
            }
        } else { //if source cell is selected
            println("2")
            isMoved.value = false
            for (move in movesList) {
                if (sourceCell.cellNumber + sign * move in 1..24) {
                    val destinationCell = cellsArray[sourceCell.cellNumber + sign * move - 1]
                    if (cell.cellNumber == destinationCell.cellNumber && (destinationCell.color != oppositeColor())) {
                        // lamma ydous 3ala cell tania ba3d elawwalaneyya at2akkedd ennaha men el highlighted
                        move(cell)
                        movesList.remove(move)
                        isMoved.value = true
                        if (oneMoveOnly) {
                            oneMoveOnly = false
                            movesList.clear() //eh lazmetha :O
                        }
                        if ((piecesAtHomePlayer[currentColor.value!! - 1] == 15) && movesList.isNotEmpty()) {
                            collectPieces()
                            println("La la la")
                            return
                        }
                        break
                    }
                }
            }
            //law e5tar cell tania 5ales ya3ni 3ayez y8ayyer el source cell
            if (isMoved.value == false && cell.numberOfPieces.value!! > 0 && cell.color == currentColor.value) {
                // s- 5adt rl condition di copy hena kaman
                if (piecesAtHomePlayer[currentColor.value!! - 1] == 0 && cell.numberOfPieces.value == 14)
                    return
                unhighlightMove()
                //-s
                if (getPossibleMoves(cell)) {
                    startingPointSelected = true
                    cell.isPieceHighlighted.value = true
                    sourceCell = cell
                } else {
                    Toast.makeText(getApplication(), "No possible moves here", Toast.LENGTH_SHORT).show()
                }
            }

        }
        if (movesList.size == 0) {
            uiScope.launch {
                switchTurns()
            }
        }
    }

    fun oppositeColor(): Int {
        println("Opposite color " + currentColor.value!! + sign)
        return currentColor.value!! + sign
    }

     fun check() {
        if (movesList.size == 2)
            checkTwoMoves()
        else if (movesList.size == 4)
            checkFourMoves()
    }

    private fun checkTwoMoves() {
        Log.i("playermoves", playersCells[currentColor.value!!-1].toString())
        val firstMove = movesList[0]
       val secondMove = movesList[1]
        // Check if one piece can do the two moves
        for (cellNum in playersCells[currentColor.value!! - 1]) {
            if ((cellNum == 1 && piecesAtHomePlayer[currentColor.value!! - 1] == 0 && cellsArray[cellNum - 1].numberOfPieces.value == 14) ||
                (cellNum == 24 && piecesAtHomePlayer[currentColor.value!! - 1] == 0 && cellsArray[cellNum - 1].numberOfPieces.value == 14))
                continue

            if ((cellNum + sign * firstMove + sign * secondMove) in 1..24 && cellsArray[cellNum + sign * firstMove + sign * secondMove - 1].color != oppositeColor() &&
                (cellsArray[cellNum + sign * firstMove - 1].color != oppositeColor() || cellsArray[cellNum + sign * secondMove - 1].color != oppositeColor()))
                return
        }
        //-s
        //8ayyart di l array ma3lesh 3ashan a3raf elindex elli hayda5alni elhome

       val possibleCells: Array<MutableList<Int>> = Array(2) { mutableListOf<Int>() }
        var homeFirstTime = false
        for (cellNum in playersCells[currentColor.value!! - 1]) {
            for (i in 0..1) {
                if ((cellNum + sign * movesList[i]) in 1..24 && cellsArray[cellNum + sign * movesList[i] - 1].color != oppositeColor()) {
                    possibleCells[i].add(cellNum)
                    if (((cellNum + sign * movesList[i]) in 19..24 && currentColor.value == 1) || ((cellNum + sign * movesList[i]) in 1..6 && currentColor.value == 2)) {
                        if (piecesAtHomePlayer[currentColor.value!! - 1] == 14)
                            return
                        val firstCellNumber =
                            (currentColor.value!! * (23 / 3.0) - sign * (23 / 3.0)).toInt()
                        if (cellsArray[firstCellNumber].numberOfPieces.value == 14 && cellsArray[firstCellNumber + sign *  movesList[i xor 1]].color == 0)
                            homeFirstTime = true
                    }
                }
            }
        }
        if (piecesAtHomePlayer[currentColor.value!! - 1] == 0 && !homeFirstTime
        ) {
            possibleCells[1].remove(1)
            possibleCells[0].remove(1)
            possibleCells[1].remove(24)
            possibleCells[0].remove(24)
        }
        // Bt2kd Eno my7sbsh el pieces elly fe el home lw lsa mraw7sh haga
        //m-
        // note that expression ((( currentColor * (23/3) - sign * (23/3) ))) gets the start cell for each player just it compresses the code
        //-s
        //5alletha 3.0 bas 3ashan kan bye3mel integer division w beyragga3 21 fi 7alet player 2

        if (possibleCells[0].size == 0 || possibleCells[1].size == 0) {
            if (possibleCells[0].size == 0 && possibleCells[1].size == 0) {
                uiScope.launch {
                    waitTime = 4500
                    switchTurns()
                }
            }
            if (possibleCells[0].size == 0) {
                Toast.makeText(getApplication(), "YOU CAN'T MOVE " + firstMove, Toast.LENGTH_SHORT).show()
                movesList.remove(firstMove)
            }

            if (possibleCells[1].size == 0) {
                Toast.makeText(getApplication(), "YOU CAN'T MOVE " + secondMove, Toast.LENGTH_SHORT).show()
                movesList.remove(secondMove)
            }
            isMoved.value = true
        } else {
            //law nafs elpossible move feletnein w mafeesh 8eir piece wa7daa
            if (possibleCells[0].size == 1 && possibleCells[1].size == 1
                && possibleCells[0][0] == possibleCells[1][0] && cellsArray[possibleCells[0][0] - 1].numberOfPieces.value == 1
            ) {
                Toast.makeText(getApplication(), "YOU CAN ONLY PLAY ONE MOVE", Toast.LENGTH_SHORT)
                    .show()
                oneMoveOnly = true
            }
        }
    }

    private fun checkFourMoves() {
        var movesNum = 0
        val move = movesList[0]
        var homeComing = 0
        var startMoves = 0
        for (cellNum in playersCells[currentColor.value!! - 1]) {
            var temp = 0
            for (i in 1..4) {
                if ((cellNum + sign * move * i) in 1..24 && cellsArray[cellNum + sign * move * i - 1].color != oppositeColor()) {
                    temp += cellsArray[cellNum - 1].numberOfPieces.value!!
                   //amk
                            if (((cellNum + sign * move * i in 19..24 && currentColor.value == 1) || (cellNum + sign * move * i in 1..6 && currentColor.value == 2))
                        && !(cellNum == (currentColor.value!! * (23 / 3.0) - sign * (23 / 3.0)).toInt()
                        && cellsArray[(currentColor.value!! * (23 / 3.0) - sign * (23 / 3.0)).toInt()].numberOfPieces.value == 14)) {
                        homeComing += cellsArray[cellNum - 1].numberOfPieces.value!!
                    }
                } else {
                    break
                }
            }
            movesNum += temp
            if (cellNum == 1 || cellNum == 24) {
                startMoves = temp
            }
        }

        if (piecesAtHomePlayer[currentColor.value!! - 1] == 0 && cellsArray[(currentColor.value!! * (23 / 3.0) - sign * (23 / 3.0)).toInt()].numberOfPieces.value == 14 && homeComing < 1) {
            println(movesNum)
            movesNum -= startMoves
            println(movesNum)
        }

        if (piecesAtHomePlayer[currentColor.value!! - 1] + homeComing == 15) {
            return
        }

        if (movesNum < 4) {
            Toast.makeText(
                getApplication(),
                "YOU CAN'T PLAY " + (4 - movesNum) + " MOVE",
                Toast.LENGTH_SHORT
            ).show()
            for (i in 1..(4 - movesNum)) {
                movesList.remove(move)
            }
            isMoved.value = true
        }
        if (movesNum == 0) {
            uiScope.launch {
                waitTime = 4500
                switchTurns()
            }

        }
    }

    fun move(cell: Cell) {
        unhighlightMove()
        if (!(cell.cellNumber in playersCells[currentColor.value!!-1])) {
            playersCells[currentColor.value!!-1].add(cell.cellNumber)
        }
        if (sourceCell.numberOfPieces.value == 1) {
            playersCells[currentColor.value!! - 1].remove(sourceCell.cellNumber)
        }
        undoList.add(MovePlayed(sourceCell.cellNumber, cell.cellNumber, false))
        _isUndoEnabled.value = true

        if (currentColor.value == 1 && sourceCell.cellNumber < 19 && cell.cellNumber >= 19) {
            piecesAtHomePlayer[0]++
            if (piecesAtHomePlayer[0] == 15 && !collectionStarted[currentColor.value!! - 1].value!!) {
                collectionStarted[currentColor.value!! - 1].value = true
                waitTime = 3000
                println("Added Time 3000")
            }
            undoList.peek().pieceMovedToHome = true
        }
        else if (currentColor.value == 2 && sourceCell.cellNumber > 6 && cell.cellNumber <= 6) {
            piecesAtHomePlayer[1] ++
            if (piecesAtHomePlayer[1] == 15 && !collectionStarted[currentColor.value!! - 1].value!!) {
                collectionStarted[currentColor.value!! - 1].value = true
            }
            undoList.peek().pieceMovedToHome = true
        }
        addPiece(cell, currentColor.value!!)
        removePiece(sourceCell)
    }

    private fun getPossibleMoves(selectedCell: Cell): Boolean {
        var isPossible = false
        println("hn")
        for (move in movesList) {
            println("h0")
            if (selectedCell.cellNumber + sign * move in 1..24) {
                val destinationCell = cellsArray[selectedCell.cellNumber + sign * move - 1]
                println("h1" + destinationCell.cellNumber)
                println("h1" + destinationCell.color)
                if (destinationCell.color != oppositeColor()) {
                    println("h2")
                    destinationCell.isCellHighlighted.value = true
                    cellsArray[selectedCell.cellNumber + sign * move - 1] = destinationCell
                    isPossible = true
                }
            }
        }
        return isPossible
    }

    open suspend fun switchTurns() {
        delay(waitTime.toLong())
        undoList.clear()
        movesList.clear()
        _isUndoEnabled.value = false
        diceRolled = false
        currentColor.value = oppositeColor()
        sign *=-1
        if (piecesAtHomePlayer[currentColor.value!! - 1] == 15) {
            collectPieces()
        }
        waitTime = 0
    }

    private fun addPiece(cell: Cell,color : Int) {
        startingPointSelected = false
        cell.color = color
        cell.numberOfPieces.value = cell.numberOfPieces.value!! + 1
        movedToCell.value = cell.cellNumber
    }

    private fun removePiece(cell: Cell){
        Log.i("Moved_From", cell.cellNumber.toString())
        movedFromCell.value = cell.cellNumber
        cell.numberOfPieces.value = cell.numberOfPieces.value!! - 1
        if (cell.numberOfPieces.value == 0)
            cell.color = 0
    }

    fun undo() {
        val movePlayed = undoList.pop()
        if (startingPointSelected) {
            unhighlightMove()
        }
        addPiece(cellsArray[movePlayed.sourceCellNo - 1], currentColor.value!!)
        removePiece(cellsArray[movePlayed.destCellNo - 1])
        if (!(movePlayed.sourceCellNo in playersCells[currentColor.value!! -1])) {
            playersCells[currentColor.value!! - 1].add(movePlayed.sourceCellNo)
        }
        if (cellsArray[movePlayed.destCellNo - 1].numberOfPieces.value == 0) {
            playersCells[currentColor.value!! - 1].remove(movePlayed.destCellNo)
        }
        if (movePlayed.pieceMovedToHome) {
            piecesAtHomePlayer[currentColor.value!!-1] --
        }
        movesList.add(abs(movePlayed.destCellNo - movePlayed.sourceCellNo))
        if (undoList.empty()) {
            _isUndoEnabled.value = false
        }
        isMoved.value = false
    }

    fun collectPieces() {
        if (currentColor.value == 1) {
            for (move in movesList) {
                if (cellsArray[24 - move].numberOfPieces.value!! > 0 && cellsArray[24 - move].color == currentColor.value) {
                    removePiece(cellsArray[24 - move])
                    piecesCollectedPlayer[0]++
                    continue
                }
                var isPlayed = false
                for (i in (24 - move) downTo 19) {
                    if (i >= 19 && i + move < 25 && cellsArray[i - 1].numberOfPieces.value!! > 0
                        && cellsArray[i - 1].color == currentColor.value && cellsArray[i + move - 1].color != 2
                    ) {
                        addPiece(cellsArray[i + move - 1],currentColor.value!!)
                        removePiece(cellsArray[i - 1])
                        isPlayed = true
                        break
                    }
                }
                if (!isPlayed) {
                    for (i in (25 - move)..24) {
                        if (i <= 24 && cellsArray[i - 1].numberOfPieces.value!! > 0 && cellsArray[i - 1].color == currentColor.value) {
                            removePiece(cellsArray[i - 1])
                            println("The cell " + i + " is collected")
                            piecesCollectedPlayer[0]++
                            break
                        }
                    }
                }
            }
        } else {
            for (move in movesList) {
                if (cellsArray[move - 1].numberOfPieces.value!! > 0 && cellsArray[move - 1].color == currentColor.value) {
                    removePiece(cellsArray[move - 1])
                    println("The cell " + move + " is collected")
                    piecesCollectedPlayer[1]++
                    continue
                }
                var isPlayed = false
                for (i in (move + 1)..6) {
                    if (i - move > 0 && cellsArray[i - 1].numberOfPieces.value!! > 0 && cellsArray[i - 1].color == currentColor.value
                        && cellsArray[i - move - 1].color != 1
                    ) {
                        addPiece(cellsArray[i - move - 1],currentColor.value!!)
                        removePiece(cellsArray[i - 1])
                        isPlayed = true
                        break
                    }
                }
                if (!isPlayed) {
                    for (i in (move - 1) downTo 1) {
                        println("cell " + i + "no of pieces " + cellsArray[i - 1].numberOfPieces.value)
                        if (i > 0 && cellsArray[i - 1].numberOfPieces.value!! > 0 && cellsArray[i - 1].color == currentColor.value) {
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
        println(piecesCollectedPlayer[currentColor.value!! - 1])
        if (piecesCollectedPlayer[currentColor.value!! - 1] == 15) {
            endRound()
        }
        else {
            uiScope.launch {
                waitTime = 4500
                switchTurns()
            }
        }
    }

    private fun unhighlightMove() {
        for (move in movesList) {
            if (sourceCell.cellNumber + sign * move in 1..24) {
                cellsArray[sourceCell.cellNumber + sign * move - 1].isCellHighlighted.value = false
            }
        }
        sourceCell.isPieceHighlighted.value = false
    }

    private fun resetGame() {
        piecesAtHomePlayer[0] = 0
        piecesAtHomePlayer[1] = 0
        piecesCollectedPlayer[0] = 0
        piecesCollectedPlayer[1] = 0
        collectionStarted[0].value = false
        collectionStarted[1].value = false
        for (cell in cellsArray) {
            println("Reset Step " + cell.cellNumber)
            cell.numberOfPieces.value = 0
            cell.color = 0
            cell.isCellHighlighted.value = false
            cell.isPieceHighlighted.value = false
        }
        movesList.clear()
        diceRolled = false

//        currentColor.value = 2
//        sign = -1
        addPiece(cellsArray[23], 2)
        //ooooooooooooo
        cellsArray[23].numberOfPieces.value = 15


//        sign = 1
        //  collectPiecesNGameFinishTest(0)

        //ooooooooo

//        currentColor.value = 1
        addPiece(cellsArray[0],1)

        //ooooooooooooo
        cellsArray[0].numberOfPieces.value = 15
//        sign = -1
        // collectPiecesNGameFinishTest(23)
        sign = 1
        //oooooooooo
        playersCells[0].clear()
        playersCells[1].clear()
        playersCells[0].add(1)
        playersCells[1].add(24)
        println("Game Reset")
    }

    private fun endRound() {
        val winner: Int
        if (currentColor.value == 1) {
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
        currentColor.value = winner
        //-s
        sign = 3- 2*currentColor.value!!
        computerTurn = winner == 2

    }
    private fun collectPiecesNGameFinishTest (insertFrom : Int){ // 23 or 0
        //hawaza3 14 piece 3ala elhome w al3ab a5er wa7da ana 3ashan a test el collect felcomputer bas asra3
        for(i in 1 .. 3){
            addPiece(cellsArray[insertFrom + sign * i],currentColor.value!!)
            cellsArray[insertFrom + sign * i].numberOfPieces.value = 4
            playersCells[(currentColor.value!!-1)].add(insertFrom + sign * i +1)
    }
        addPiece(cellsArray[insertFrom + sign * 4], currentColor.value!!)
        cellsArray[insertFrom + sign * 4].numberOfPieces.value = 2
        playersCells[(currentColor.value!!-1)].add(insertFrom + sign * 4 +1)
        piecesAtHomePlayer[(currentColor.value!!-1)] = 14
        Log.i("MAMAAA", playersCells[currentColor.value!!-1].toString())
        Log.i("MAMAAA", currentColor.value.toString())
    }
}
