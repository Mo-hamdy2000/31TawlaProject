package com.example.android.a31tawlaproject.miscUtils

import androidx.lifecycle.MutableLiveData

data class Cell(
    val cellNumber : Int,
    var numberOfPieces: MutableLiveData<Int>,
    var color: Int,
    var isCellHighlighted: MutableLiveData<Boolean>,
    var isPieceHighlighted: MutableLiveData<Boolean>
)
