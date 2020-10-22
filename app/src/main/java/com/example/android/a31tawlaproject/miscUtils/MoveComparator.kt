package com.example.android.a31tawlaproject.miscUtils

class MoveComparator : Comparator<MovePlayed> {
    override fun compare(move1: MovePlayed, move2: MovePlayed): Int {
        return move2.sourceCellNo.compareTo(move1.sourceCellNo)
    }
}