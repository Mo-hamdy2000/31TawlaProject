package com.example.android.a31tawlaproject.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation.findNavController
import com.example.android.a31tawlaproject.R
import com.example.android.a31tawlaproject.databinding.MainFragmentBinding
import com.example.android.a31tawlaproject.game.GameViewModel
import com.example.android.a31tawlaproject.miscUtils.load

class Home : Fragment() {
    private lateinit var binding : MainFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.main_fragment,container,false)
        val singlePlayerButton : Button = binding.singlePlayerButton
        val twoPlayerButton : Button = binding.twoPlayerButton
        val resume : Button = binding.resumeButton
        singlePlayerButton.setOnClickListener {
            GameViewModel.gameMode = 1
            findNavController(it).navigate(R.id.action_main_to_singlePlayerFragment)
        }
        twoPlayerButton.setOnClickListener {
            GameViewModel.gameMode = 2
            findNavController(it).navigate(R.id.action_main_to_twoPlayerFragment)
        }
        resume.setOnClickListener{
            GameViewModel.readArray(load())
            if(GameViewModel.gameMode == 1)
                findNavController(it).navigate(R.id.action_main_to_singlePlayerFragment)
            else if(GameViewModel.gameMode == 2)
                findNavController(it).navigate(R.id.action_main_to_twoPlayerFragment)
        }

        return binding.root
    }


}