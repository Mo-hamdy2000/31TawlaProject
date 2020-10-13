package com.example.android.a31tawlaproject.score

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.android.a31tawlaproject.R
import com.example.android.a31tawlaproject.databinding.ScoreFragmentBinding
import com.example.android.a31tawlaproject.game.GameViewModel

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

        return binding.root
    }

}
