package com.example.android.a31tawlaproject.score

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel(winner: String, winnerScore: Int, opponentScore: Int): ViewModel() {

    private val _winner = MutableLiveData<String>()
    val winner: LiveData<String>
        get() = _winner

    private val _winnerScore = MutableLiveData<Int>()
    val winnerScore: LiveData<Int>
        get() = _winnerScore

    private val _opponentScore = MutableLiveData<Int>()
    val opponentScore: LiveData<Int>
        get() = _opponentScore


    private val _playAgain = MutableLiveData<Boolean>()
    val playAgain: LiveData<Boolean>
        get() = _playAgain


    init {
        _winner.value = winner
        _winnerScore.value = winnerScore
        _opponentScore.value = opponentScore
        _playAgain.value = false
    }

    fun playAgain() {
        _playAgain.value = true
    }
}