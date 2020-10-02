package com.example.android.a31tawlaproject

import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

data class MovePlayed(
    val sourceCellNo : Int,
    val destCellNo: Int,
    var pieceMovedToHome: Boolean
)