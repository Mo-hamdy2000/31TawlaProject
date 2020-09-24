package com.example.android.a31tawlaproject

import android.app.Application
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.AndroidViewModel
import com.example.android.a31tawlaproject.databinding.GameFragmentBinding


class GameViewModel(application: Application, val binding: GameFragmentBinding) : AndroidViewModel(application) {

    private fun getCell(cellNum: Int): ConstraintLayout{
        return when(cellNum) {
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

    private fun getCellText(cellNum: Int): TextView{
        return when(cellNum) {
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

    fun addPiece(cellNum: Int){
        val cell = getCell(cellNum)
        val cellText = getCellText(cellNum)
        if (cellText.text != "") {
            cellText.text = (cellText.text.toString().toInt() + 1).toString()
        }
        else
        {
            cellText.text = "1"
            val image = ImageView(getApplication())
            // If statement will be added here so that piece image depends on player
            image.setImageResource(R.drawable.piece1)
            if (cellNum <= 12) {
                image.scaleType = ImageView.ScaleType.FIT_START
                cell.addView(image, 0)
            }
            else {
                image.scaleType = ImageView.ScaleType.FIT_END
                cell.addView(image)
            }
        }
    }

    fun removePiece(cellNum: Int){
        val cell = getCell(cellNum)
        val cellText = getCellText(cellNum)
        if (cellText.text.toString().toInt() > 1) {
            cellText.text = (cellText.text.toString().toInt() - 1).toString()
        }
        else {
            cellText.text = ""
            if (cellNum <= 12) {
                cell.removeViewAt(0)
            }
            else {
                cell.removeViewAt(1)
            }

        }


    }

    fun highlightCell(cellNum: Int) {
        val cell = getCell(cellNum)
        if (cellNum <= 12 && cellNum % 2 == 0) {
            cell.setBackgroundResource(R.drawable.cell_gold_upper_highlighted)
        }
        else if (cellNum > 12 && cellNum % 2 == 0) {
            cell.setBackgroundResource(R.drawable.cell_gold_lower_highlighted)
        }
        else if (cellNum <= 12 && cellNum % 2 != 0) {
            cell.setBackgroundResource(R.drawable.cell_blue_upper_highlighted)
        }
        else {
            cell.setBackgroundResource(R.drawable.cell_blue_lower_highlighted)
        }
    }

    fun unhighlightCell(cellNum: Int) {
        val cell = getCell(cellNum)
        if (cellNum <= 12 && cellNum % 2 == 0) {
            cell.setBackgroundResource(R.drawable.cell_gold_upper)
        }
        else if (cellNum > 12 && cellNum % 2 == 0) {
            cell.setBackgroundResource(R.drawable.cell_gold_lower)
        }
        else if (cellNum <= 12 && cellNum % 2 != 0) {
            cell.setBackgroundResource(R.drawable.cell_blue_upper)
        }
        else {
            cell.setBackgroundResource(R.drawable.cell_blue_lower)
        }
    }

    fun highlightPiece(cellNum: Int, playerNum: Int) {
        val cell = getCell(cellNum)
        if (cell.childCount > 1) {
            lateinit var piece: ImageView
            if (cellNum <= 12) {
                piece = cell.getChildAt(0) as ImageView
            }
            else {
                piece = cell.getChildAt(1) as ImageView
            }
            if (playerNum == 1) {
                piece.setImageResource(R.drawable.piece1_highlighted)
            }
            else {
                piece.setImageResource(R.drawable.piece2_highlighted)
            }
        }
    }

    fun unhighlightPiece(cellNum: Int, playerNum: Int) {
        val cell = getCell(cellNum)
        if (cell.childCount > 1) {
            lateinit var piece: ImageView
            if (cellNum <= 12) {
                piece = cell.getChildAt(0) as ImageView
            }
            else {
                piece = cell.getChildAt(1) as ImageView
            }
            if (playerNum == 1) {
                piece.setImageResource(R.drawable.piece1)
            }
            else {
                piece.setImageResource(R.drawable.piece2)
            }
        }

    }
}