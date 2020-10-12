package com.example.android.a31tawlaproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation.findNavController
import com.example.android.a31tawlaproject.databinding.FragmentMainBinding

class Main : Fragment() {
    private lateinit var binding : FragmentMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_main,container,false)
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
            else
                findNavController(it).navigate(R.id.action_main_to_twoPlayerFragment)
        }

        return binding.root
    }


}