package com.example.android.a31tawlaproject

import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

data class Cell(
    val cellNumber : Int,
    var numberOfPieces: Int,
    var color: Int,
    val cellID: ConstraintLayout,
    val cellText: TextView
)
