package com.example.android.a31tawlaproject.game

import android.app.Application
import com.example.android.a31tawlaproject.miscUtils.Cell
import com.example.android.a31tawlaproject.miscUtils.MovePlayed

class TwoPlayerViewModel(application: Application) : GameViewModel(application) {
    
    override suspend fun switchTurns() {
        super.switchTurns()
        check()
    }

}