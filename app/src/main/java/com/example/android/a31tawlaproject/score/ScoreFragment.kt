package com.example.android.a31tawlaproject.score

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.android.a31tawlaproject.R
import com.example.android.a31tawlaproject.databinding.ScoreFragmentBinding
import com.example.android.a31tawlaproject.game.GameViewModel
import com.example.android.a31tawlaproject.miscUtils.load

class ScoreFragment() : Fragment() {


    private lateinit var viewModel: ScoreViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val winner = GameViewModel.getWinner()
        val score = GameViewModel.getScore()
        viewModel = ScoreViewModel(winner, score[0], score[1])

        val binding: ScoreFragmentBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.score_fragment,
            container,
            false
        )

        viewModel.winner.observe(viewLifecycleOwner, Observer {
            binding.winnerTextView.text = it
        })

        viewModel.winnerScore.observe(viewLifecycleOwner, Observer {
            binding.playerScoreTextView.text = it.toString()
        })

        viewModel.opponentScore.observe(viewLifecycleOwner, Observer {
            binding.opponentScoreTextView.text = it.toString()
        })

        val backButton : ImageView = binding.backImageView
        val playAgainButton : ImageView = binding.playAgainImageView

        backButton.setOnClickListener{
            Navigation.findNavController(it).navigate(R.id.action_scoreFragment_to_home)
        }

        playAgainButton.setOnClickListener{
            if(GameViewModel.gameMode == 1)
                Navigation.findNavController(it).navigate(R.id.action_scoreFragment_to_singlePlayerFragment)
            else if(GameViewModel.gameMode == 2)
                Navigation.findNavController(it).navigate(R.id.action_scoreFragment_to_twoPlayerFragment)
        }

        return binding.root
    }

}
