package com.example.android.a31tawlaproject

import android.app.Application
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.AndroidViewModel
import com.example.android.a31tawlaproject.databinding.GameFragmentBinding

class GameViewModel(application: Application, private val binding: GameFragmentBinding) : AndroidViewModel(application) {

        private lateinit var firstCell: Cell // -> first dice outcome
        private lateinit var secondCell: Cell // -> second dice outcome
        private var startingPointSelected = false // -> flag for selecting source cell
        private lateinit var sourceCell : Cell
        private var cellOnePlayed = false
        private var cellTwoPlayed = false
        private var currentColor = 1
        private var scoreOne = 0
        private var scoreTwo = 0
         val cellsArray: Array<Cell> = Array(24) {
            Cell(it + 1, 0, 0, getCell(it + 1), getCellText(it + 1))
        }


    init {
        currentColor = 2
        addPiece(cellsArray[12])
        currentColor = 1
        addPiece(cellsArray[0])

        cellsArray[0].cellText.text = 12.toString()
        cellsArray[12].cellText.text = 12.toString()
        cellsArray[0].numberOfPieces =12
        cellsArray[12].numberOfPieces =12
        cellsArray[0].color =1
        cellsArray[12].color =2
        rollDice(binding.diceImg1,binding.diceImg2)
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

        if (cell.cellNumber <= 12 && cell.cellNumber % 2 == 0) {
            cell.cellID.setBackgroundResource(R.drawable.cell_gold_upper_highlighted)
        }
        else if (cell.cellNumber > 12 && cell.cellNumber % 2 == 0) {
            cell.cellID.setBackgroundResource(R.drawable.cell_gold_lower_highlighted)
        }
        else if (cell.cellNumber <= 12 && cell.cellNumber % 2 != 0) {
            cell.cellID.setBackgroundResource(R.drawable.cell_blue_upper_highlighted)
        }
        else {
            cell.cellID.setBackgroundResource(R.drawable.cell_blue_lower_highlighted)
        }
    }

    private fun unhighlightCell(cell: Cell) {
        if (cell.cellNumber <= 12 && cell.cellNumber % 2 == 0) {
            cell.cellID.setBackgroundResource(R.drawable.cell_gold_upper)
        }
        else if (cell.cellNumber > 12 && cell.cellNumber % 2 == 0) {
            cell.cellID.setBackgroundResource(R.drawable.cell_gold_lower)
        }
        else if (cell.cellNumber <= 12 && cell.cellNumber % 2 != 0) {
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
        println("Wslnaaaaaaaaaaaaaaa")
        if (!startingPointSelected) {
            if (cell.numberOfPieces > 0 && cell.color == currentColor) {
                // awwel ma ydous 3ala piece at2akked ennaha bta3to w fi pieces fel 5ana di
                Toast.makeText(getApplication(), "YOU SELECTED ME", Toast.LENGTH_LONG).show()
                getPossibleMoves(cell)
                startingPointSelected = true
                highlightPiece(cell)
                sourceCell = cell
            }
        } else {
            if (cell == firstCell && !cellOnePlayed && (firstCell.color == currentColor || cell.color == 0)) {
                // lamma ydous 3ala cell tania ba3d elawwalaneyya at2akkedd ennaha men el highlighted
                addPiece(cell)
                cellOnePlayed = true
                unhighlightCell(firstCell)
                unhighlightCell(secondCell)
                unhighlightPiece(sourceCell)
                removePiece(sourceCell)


            } else if (cell == secondCell && !cellTwoPlayed && (secondCell.color == currentColor || cell.color == 0)) {
                addPiece(cell)
                cellTwoPlayed = true
                unhighlightCell(firstCell)
                unhighlightCell(secondCell)
                unhighlightPiece(sourceCell)
                removePiece(sourceCell)


            }
            //law e5tar cell tania 5ales ya3ni 3ayez y8ayyer el source cell
            else if (cell != firstCell && cell != secondCell) {
                getPossibleMoves(cell)
                startingPointSelected = true
            }

            if (cellOnePlayed && cellTwoPlayed)
                switchTurns()
        }

    }
    private fun getPossibleMoves(selectedCell: Cell) {
        // gets two possible moves given by dice output
        // a3mel flag enn elcell tenfa3 ?? badal el condition elli fooo2
        firstCell = cellsArray[selectedCell.cellNumber + diceOneVal - 1]
        secondCell = cellsArray[selectedCell.cellNumber + diceTwoVal - 1]
        if (!cellOnePlayed && (firstCell.color == currentColor || firstCell.color == 0) ) {
             highlightCell(firstCell)
        }
        if (!cellTwoPlayed && (secondCell.color == currentColor || secondCell.color == 0)) {
            highlightCell(secondCell)
        }
    }

    private fun switchTurns() {
        currentColor = if (currentColor == 1)
            2
        else
            1
        cellOnePlayed = false
        cellTwoPlayed = false
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
                // If statement will be added here so that piece image depends on player
                if(currentColor ==1)
                    image.setImageResource(R.drawable.piece1)
                else if(currentColor==2)
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
                if (cell.cellNumber <= 12) {
                    cell.cellID.removeViewAt(0)
                }
                else {
                    cell.cellID.removeViewAt(1)
                }
                cell.numberOfPieces -=1
            }




    }
}