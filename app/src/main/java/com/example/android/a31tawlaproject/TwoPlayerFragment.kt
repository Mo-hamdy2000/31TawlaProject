package com.example.android.a31tawlaproject

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.android.a31tawlaproject.databinding.GameFragmentBinding

class TwoPlayerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate view and obtain an instance of the binding class.
        val binding: GameFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false)
        Log.i("GameFragment", "55555")
        // Specify the current activity as the lifecycle owner.
        val application = requireNotNull(this.activity).application
        val gameViewModel = TwoPlayerViewModel(application, binding)
        val rollButton : Button = binding.rollButton
        val skipButton : Button = binding.skipButton
        val diceOne : ImageView = binding.diceImg1
        val diceTwo : ImageView = binding.diceImg2

        rollButton.setOnClickListener{
            if (!gameViewModel.diceRolled) {
                rollDice(diceOne,diceTwo, gameViewModel.movesList)
                gameViewModel.diceRolled = true
                if ((gameViewModel.currentColor == 1 &&  gameViewModel.piecesAtHomePlayer1 == 15) ||
                    (gameViewModel.currentColor == 2 &&  gameViewModel.piecesAtHomePlayer2 == 15)) {
                    gameViewModel.collectPieces()
                }
            }
        }

        skipButton.setOnClickListener{
            gameViewModel.skipTurn()
        }

        binding.lifecycleOwner = this
        binding.gameViewModel = gameViewModel
        return binding.root
    }
}