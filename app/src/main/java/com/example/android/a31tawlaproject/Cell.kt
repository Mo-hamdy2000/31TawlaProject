package com.example.android.a31tawlaproject

import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData

data class Cell(
    val cellNumber : Int,
    var numberOfPieces: MutableLiveData<Int>,
    var color: Int,
    var isCellHighlighted: MutableLiveData<Boolean>,
    var isPieceHighlighted: MutableLiveData<Boolean>
)
