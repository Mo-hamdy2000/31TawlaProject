package com.example.android.a31tawlaproject

import android.app.Application
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.AndroidViewModel
import com.example.android.a31tawlaproject.databinding.GameFragmentBinding
import java.util.Stack
import kotlin.math.abs

class GameViewModel(application: Application, private val binding: GameFragmentBinding) : AndroidViewModel(application) {

    //private lateinit var firstCell: Cell // -> first dice outcome
    //private lateinit var secondCell: Cell // -> second dice outcome
    private var startingPointSelected = false // -> flag for selecting source cell
    private lateinit var sourceCell : Cell
    //private var cellOnePlayed = false
    //private var cellTwoPlayed = false
    var currentColor = 1
    private var sign = 1
    private var scoreOne = 0
    private var scoreTwo = 0
    val cellsArray: Array<Cell> = Array(24) {
        Cell(it + 1, 0, 0, getCell(it + 1), getCellText(it + 1))
    }
    val movesList = mutableListOf<Int>()
    var diceRolled = false
    var piecesAtHomePlayer1 = 0
    var piecesAtHomePlayer2 = 0
    private var piecesCollectedPlayer1 = 0
    private var piecesCollectedPlayer2 = 0
    val undoList = Stack<MovePlayed>()
    private val playersCells: Array<MutableList<Int>> = Array(2) {
        mutableListOf<Int>()
    }
    private var oneMoveOnly = false


    init {
        currentColor = 2
        addPiece(cellsArray[23])
        currentColor = 1
        addPiece(cellsArray[0])

        cellsArray[0].cellText.text = 15.toString()
        cellsArray[23].cellText.text = 15.toString()
        cellsArray[0].numberOfPieces =15
        cellsArray[23].numberOfPieces = 15
        cellsArray[0].color =1
        cellsArray[23].color =2
        playersCells[0].add(1)
        playersCells[1].add(24)
        //rollDice(binding.diceImg1,binding.diceImg2)
    }

    private fun getCell(cellNum: Int): ConstraintLayout {
        return when (cellNum) {
            1 -> binding.cell1
            2 -> binding.cell2
            3 -> binding.cell3
            4 -> binding.cell4
            5 -> binding.cell5
            6 -> binding.cell6
            7 -> binding.cell7
            8 -> binding.cell8
            9 -> binding.cell9
            10 -> binding.cell10
            11 -> binding.cell11
            12 -> binding.cell12
            13 -> binding.cell13
            14 -> binding.cell14
            15 -> binding.cell15
            16 -> binding.cell16
            17 -> binding.cell17
            18 -> binding.cell18
            19 -> binding.cell19
            20 -> binding.cell20
            21 -> binding.cell21
            22 -> binding.cell22
            23 -> binding.cell23
            24 -> binding.cell24
            else -> ConstraintLayout(getApplication())
        }
    }

    private fun getCellText(cellNum: Int): TextView {
        return when (cellNum) {
            1 -> binding.textViewCell1
            2 -> binding.textViewCell2
            3 -> binding.textViewCell3
            4 -> binding.textViewCell4
            5 -> binding.textViewCell5
            6 -> binding.textViewCell6
            7 -> binding.textViewCell7
            8 -> binding.textViewCell8
            9 -> binding.textViewCell9
            10 -> binding.textViewCell10
            11 -> binding.textViewCell11
            12 -> binding.textViewCell12
            13 -> binding.textViewCell13
            14 -> binding.textViewCell14
            15 -> binding.textViewCell15
            16 -> binding.textViewCell16
            17 -> binding.textViewCell17
            18 -> binding.textViewCell18
            19 -> binding.textViewCell19
            20 -> binding.textViewCell20
            21 -> binding.textViewCell21
            22 -> binding.textViewCell22
            23 -> binding.textViewCell23
            24 -> binding.textViewCell24
            else -> TextView(getApplication())
        }
    }

    private fun highlightCell(cell: Cell) {
        if (cell.cellNumber <= 12 && cell.cellNumber % 2 != 0) {
            cell.cellID.setBackgroundResource(R.drawable.cell_gold_upper_highlighted)
        }
        else if (cell.cellNumber > 12 && cell.cellNumber % 2 != 0) {
            cell.cellID.setBackgroundResource(R.drawable.cell_gold_lower_highlighted)
        }
        else if (cell.cellNumber <= 12 && cell.cellNumber % 2 == 0) {
            cell.cellID.setBackgroundResource(R.drawable.cell_blue_upper_highlighted)
        }
        else {
            cell.cellID.setBackgroundResource(R.drawable.cell_blue_lower_highlighted)
        }
    }

    private fun unhighlightCell(cell: Cell) {
        if (cell.cellNumber <= 12 && cell.cellNumber % 2 != 0) {
            cell.cellID.setBackgroundResource(R.drawable.cell_gold_upper)
        }
        else if (cell.cellNumber > 12 && cell.cellNumber % 2 != 0) {
            cell.cellID.setBackgroundResource(R.drawable.cell_gold_lower)
        }
        else if (cell.cellNumber <= 12 && cell.cellNumber % 2 == 0) {
            cell.cellID.setBackgroundResource(R.drawable.cell_blue_upper)
        }
        else {
            cell.cellID.setBackgroundResource(R.drawable.cell_blue_lower)
        }
    }

    private fun highlightPiece(cell: Cell) {
        if (cell.cellID.childCount > 1) {
            lateinit var piece: ImageView
            if (cell.cellNumber <= 12) {
                piece = cell.cellID.getChildAt(0) as ImageView
            }
            else {
                piece = cell.cellID.getChildAt(1) as ImageView
            }
            if (currentColor == 1) {
                piece.setImageResource(R.drawable.piece1_highlighted)
            }
            else {
                piece.setImageResource(R.drawable.piece2_highlighted)
            }
        }
    }

    private fun unhighlightPiece(cell: Cell) {
        if (cell.cellID.childCount > 1) {
            lateinit var piece: ImageView
            if (cell.cellNumber <= 12) {
                piece = cell.cellID.getChildAt(0) as ImageView
            }
            else {
                piece = cell.cellID.getChildAt(1) as ImageView
            }
            if (currentColor == 1) {
                piece.setImageResource(R.drawable.piece1)
            }
            else {
                piece.setImageResource(R.drawable.piece2)
            }
        }


    }

    fun selectCell(cell: Cell) {
        if (!diceRolled || (currentColor == 1 && piecesAtHomePlayer1 == 15) || (currentColor == 2 && piecesAtHomePlayer2 == 15)) {
            return
        }
        if (!startingPointSelected) {
            // Zwdt condition 3ashan awl stage fe el l3ba
            // El la3b lazem yl3b b piece w7da w ywslha el n7ia el tania
            // Fa hwa msh hy2dr y7rk piece mn el home elly feha 14 piece l8ayt ma ykhls el first move dy
            if ((piecesAtHomePlayer1 == 0 && cell.numberOfPieces == 14 && currentColor == 1) || (piecesAtHomePlayer2 == 0 && cell.numberOfPieces == 14 && currentColor == 2)) {
                return
            }
            if (cell.numberOfPieces > 0 && cell.color == currentColor) {
                // awwel ma ydous 3ala piece at2akked ennaha bta3to w fi pieces fel 5ana di
                if (getPossibleMoves(cell)) {
                    startingPointSelected = true
                    highlightPiece(cell)
                    sourceCell = cell
                }
            }
        } else {
            var isMoved = false
            for (move in movesList) {
                if (sourceCell.cellNumber + sign * move in 1..24) {
                    val destinationCell = cellsArray[sourceCell.cellNumber + sign * move - 1]
                    if (cell.cellNumber == destinationCell.cellNumber && (destinationCell.color == currentColor || destinationCell.color == 0)) {
                        // lamma ydous 3ala cell tania ba3d elawwalaneyya at2akkedd ennaha men el highlighted
                        move(cell)
                        movesList.remove(move)
                        isMoved = true
                        if (((currentColor == 1 && piecesAtHomePlayer1 == 15) || (currentColor == 2 && piecesAtHomePlayer2 == 15)) && !movesList.isEmpty()) {
                            collectPieces()
                            return
                        }
                        break
                    }
                }
            }
            //law e5tar cell tania 5ales ya3ni 3ayez y8ayyer el source cell
            if (!isMoved && cell.numberOfPieces > 0 && cell.color == currentColor) {
                for (move in movesList) {
                    if (sourceCell.cellNumber + sign * move in 1..24) {
                        unhighlightCell(cellsArray[sourceCell.cellNumber + sign * move - 1])
                    }
                }
                unhighlightPiece(sourceCell)
                getPossibleMoves(cell)
                startingPointSelected = true
                highlightPiece(cell)
                sourceCell = cell
            }
            if (oneMoveOnly) {
                oneMoveOnly = false
                movesList.clear()
            }
            if (movesList.size == 0)
                switchTurns()
        }

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
            if ((cellNum == 1 && currentColor == 1 && piecesAtHomePlayer1 == 0 && cellsArray[cellNum - 1].numberOfPieces == 14) ||
                (cellNum == 24 && currentColor == 2 && piecesAtHomePlayer2 == 0 && cellsArray[cellNum - 1].numberOfPieces == 14)) {
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

        if (homeFlag && ((currentColor == 1 && piecesAtHomePlayer1 == 14) || (currentColor == 2 && piecesAtHomePlayer2 == 14))) {
            return
        }
        println("##############")
        println(firstMovePossibleCells.toString())
        println(secondMovePossibleCells.toString())
        // Bt2kd Eno my7sbsh el pieces elly fe el home lw lsa mraw7sh haga
        if (((currentColor == 1 && piecesAtHomePlayer1 == 0 && cellsArray[0].numberOfPieces == 14) ||
            (currentColor == 2 && piecesAtHomePlayer2 == 0 && cellsArray[23].numberOfPieces == 14)) && !homeFlag) {
            firstMovePossibleCells.remove(1)
            secondMovePossibleCells.remove(1)
            firstMovePossibleCells.remove(24)
            secondMovePossibleCells.remove(24)
        }

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
            if (firstMovePossibleCells.size == 1 && secondMovePossibleCells.size == 1  && firstMovePossibleCells[0] == secondMovePossibleCells[0] && cellsArray[firstMovePossibleCells[0] - 1].numberOfPieces == 1) {
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
        for (cellNum in playersCells[currentColor-1]) {
            var temp = 0
            for (i in 1..4) {
                if ((cellNum + sign * move * i) in 1..24 && cellsArray[cellNum + sign * move * i - 1].color != oppositeColor()) {
                    temp += cellsArray[cellNum - 1].numberOfPieces
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

        if (((currentColor == 1 && piecesAtHomePlayer1 == 0 && cellsArray[0].numberOfPieces == 14) ||
            (currentColor == 2 && piecesAtHomePlayer2 == 0 && cellsArray[23].numberOfPieces == 14)) && homeComing < 1) {
            movesNum -= startMoves
        }

        if ((currentColor == 1 && piecesAtHomePlayer1 + homeComing == 15) || (currentColor == 2 && piecesAtHomePlayer2 + homeComing == 15)) {
            return
        }

        if (movesNum < 4) {
            Toast.makeText(getApplication(), "YOU CAN'T PLAY " + (4 - movesNum) + " MOVE", Toast.LENGTH_SHORT).show()
            for (i in 1..(4-movesNum)) {
                movesList.remove(move)
            }
        }
        if (movesNum == 0) {
            switchTurns()
        }
    }

    private fun move(cell: Cell) {
        for (move in movesList) {
            if (sourceCell.cellNumber + sign * move in 1..24) {
                unhighlightCell(cellsArray[sourceCell.cellNumber + sign * move - 1])
            }
        }
        unhighlightPiece(sourceCell)
        addPiece(cell)
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
        if (currentColor == 1 && sourceCell.cellNumber < 19 && cell.cellNumber >= 19) {
            piecesAtHomePlayer1 += 1
            undoList.peek().pieceMovedToHome = true
        }
        else if (currentColor == 2 && sourceCell.cellNumber > 6 && cell.cellNumber <= 6) {
                piecesAtHomePlayer2 += 1
                undoList.peek().pieceMovedToHome = true
        }
    }

    private fun getPossibleMoves(selectedCell: Cell): Boolean {
        var isPossible = false
        for (move in movesList) {
            if (selectedCell.cellNumber + sign * move in 1..24) {
                val destinationCell = cellsArray[selectedCell.cellNumber + sign * move - 1]
                if (destinationCell.color == currentColor || destinationCell.color == 0) {
                    highlightCell(destinationCell)
                    isPossible = true
                }
            }
        }
        return isPossible
    }

    private fun switchTurns() {
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

    private fun addPiece(cell: Cell){

        if (cell.numberOfPieces >0) {
            cell.numberOfPieces+=1
            cell.cellText.text = (cell.numberOfPieces).toString()
            startingPointSelected = false
            cell.color = currentColor
        }
        else if (cell.numberOfPieces==0)
        {
            cell.cellText.text = "1"
            val image = ImageView(getApplication())
            if(currentColor == 1)
                image.setImageResource(R.drawable.piece1)
            else if(currentColor== 2)
                image.setImageResource(R.drawable.piece2)
            if (cell.cellNumber <= 12) {
                image.scaleType = ImageView.ScaleType.FIT_START
                cell.cellID.addView(image, 0)
            }
            else {
                image.scaleType = ImageView.ScaleType.FIT_END
                cell.cellID.addView(image)
            }
            cell.numberOfPieces +=1
            startingPointSelected = false
            cell.color = currentColor
        }

    }

    private fun removePiece(cell: Cell){
        if (cell.numberOfPieces > 1) {
            cell.cellText.text = (cell.cellText.text.toString().toInt() - 1).toString()
            cell.numberOfPieces-=1
        }
        else if(cell.numberOfPieces == 1) {
            cell.cellText.text = ""
            cell.color = 0
            if (cell.cellNumber <= 12) {
                cell.cellID.removeViewAt(0)
            }
            else {
                cell.cellID.removeViewAt(1)
            }
            cell.numberOfPieces -=1
        }
    }

    fun undo() {
        val movePlayed = undoList.pop()
        if (startingPointSelected) {
            for (move in movesList) {
                if (sourceCell.cellNumber + sign * move in 1..24) {
                    unhighlightCell(cellsArray[sourceCell.cellNumber + sign * move - 1])
                }
            }
            unhighlightPiece(sourceCell)
        }
        addPiece(cellsArray[movePlayed.sourceCellNo - 1])
        removePiece(cellsArray[movePlayed.destCellNo - 1])
        if (!(movePlayed.sourceCellNo in playersCells[currentColor-1])) {
            playersCells[currentColor-1].add(movePlayed.sourceCellNo)
        }
        if (cellsArray[movePlayed.destCellNo - 1].numberOfPieces == 0) {
            playersCells[currentColor-1].remove(movePlayed.destCellNo)
        }
        movesList.add(abs(movePlayed.destCellNo - movePlayed.sourceCellNo))
        if (undoList.empty()) {
            binding.undoButton.isEnabled = false
        }
    }

    fun collectPieces() {
        if (currentColor == 1) {
            for (move in movesList) {
                if (cellsArray[24 - move].numberOfPieces > 0 && cellsArray[24 - move].color == currentColor) {
                    removePiece(cellsArray[24 - move])
                    piecesCollectedPlayer1++
                    continue
                }
                var isPlayed = false
                for (i in (24 - move) downTo 19) {
                    if (i >= 19 && i + move < 25 && cellsArray[i-1].numberOfPieces > 0 && cellsArray[i-1].color == currentColor && cellsArray[i + move -1].color != 2) {
                        addPiece(cellsArray[i + move - 1])
                        removePiece(cellsArray[i - 1])
                        isPlayed = true
                        break
                    }
                }
                if (!isPlayed) {
                    for (i in (25 - move) .. 24) {
                        if (i <= 24 && cellsArray[i - 1].numberOfPieces > 0 && cellsArray[i - 1].color == currentColor) {
                            removePiece(cellsArray[i - 1])
                            println("The cell " + i + " is collected")
                            piecesCollectedPlayer1++
                            break
                        }
                    }
                }
            }
        } else {
            for (move in movesList) {
                if (cellsArray[move - 1].numberOfPieces > 0 && cellsArray[move - 1].color == currentColor) {
                    removePiece(cellsArray[move - 1])
                    println("The cell " + move + " is collected")
                    piecesCollectedPlayer2++
                    continue
                }
                var isPlayed = false
                for (i in (move + 1) .. 6) {
                    if (i - move > 0 && cellsArray[i-1].numberOfPieces > 0 && cellsArray[i-1].color == currentColor && cellsArray[i - move -1].color != 1) {
                        addPiece(cellsArray[i - move -1])
                        removePiece(cellsArray[i - 1])
                        isPlayed = true
                        break
                    }
                }
                if (!isPlayed) {
                    for (i in (move - 1) downTo 1) {
                        println("cell " + i + "no of pieces " + cellsArray[i - 1].numberOfPieces)
                        if (i > 0 && cellsArray[i - 1].numberOfPieces > 0 && cellsArray[i - 1].color == currentColor) {
                            removePiece(cellsArray[i - 1])
                            println("The cell " + i + " is collected")
                            piecesCollectedPlayer2++
                            break
                        }
                    }
                }
            }
        }
        movesList.clear()
        switchTurns()
    }

    fun skipTurn() {
        for (move in movesList) {
            if (sourceCell.cellNumber + sign * move in 1..24) {
                unhighlightCell(cellsArray[sourceCell.cellNumber + sign * move - 1])
            }
        }
        movesList.clear()
        startingPointSelected = false
        switchTurns()
    }

}