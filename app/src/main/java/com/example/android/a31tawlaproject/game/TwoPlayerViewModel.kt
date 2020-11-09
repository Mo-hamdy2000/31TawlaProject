package com.example.android.a31tawlaproject.game

import android.app.Application

class TwoPlayerViewModel(application: Application) : GameViewModel(application) {
    override suspend fun switchTurns() {
        super.switchTurns()
        check()
    }
}