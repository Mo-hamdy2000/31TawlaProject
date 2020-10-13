package com.example.android.a31tawlaproject.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.android.a31tawlaproject.GameFragment
import com.example.android.a31tawlaproject.R
import com.example.android.a31tawlaproject.miscUtils.*

class SinglePlayerFragment: GameFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = super.onCreateView(inflater, container, savedInstanceState)
        val application = requireNotNull(this.activity).application
        val gameViewModel =
            SinglePlayerViewModel(
                application
            )
        this.initializationWithViewModel(gameViewModel, this)
        binding = DataBindingUtil.inflate(inflater,
            R.layout.game_fragment, container, false)
        gameViewModel.roll.observe(viewLifecycleOwner, Observer {
            if (it) {
                if (!GameViewModel.diceRolled) {
                    setDiceImg(rollDice(GameViewModel.movesList), arrayOf(binding.diceImg1, binding.diceImg2))
                    GameViewModel.diceRolled = true
                    if (GameViewModel.piecesAtHomePlayer[GameViewModel.currentColor - 1] == 15) {
                        gameViewModel.collectPieces()
                    }

                    gameViewModel.check()
                }
            }
        })
        return root
    }
}