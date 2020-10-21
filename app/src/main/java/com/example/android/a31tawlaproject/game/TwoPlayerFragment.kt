package com.example.android.a31tawlaproject.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class TwoPlayerFragment : GameFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = super.onCreateView(inflater, container, savedInstanceState)
        val application = requireNotNull(this.activity).application
        val gameViewModel =
            TwoPlayerViewModel(
                application
            )
        this.initializationWithViewModel(gameViewModel, this)
        return root
    }
}