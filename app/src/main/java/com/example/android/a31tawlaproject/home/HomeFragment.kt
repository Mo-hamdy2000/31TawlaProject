package com.example.android.a31tawlaproject.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation.findNavController
import com.example.android.a31tawlaproject.MainActivity
import com.example.android.a31tawlaproject.R
import com.example.android.a31tawlaproject.databinding.HomeFragmentBinding
import com.example.android.a31tawlaproject.game.GameViewModel
import com.example.android.a31tawlaproject.miscUtils.load

class HomeFragment : Fragment() {
    companion object{
        var isMusicMuted = false
        var isSoundMuted = false
    }
    private lateinit var binding : HomeFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.home_fragment,container,false)
        val singlePlayerButton : ImageView = binding.onePlayerImageView
        val twoPlayerButton : ImageView = binding.twoPlayersImageView
        val resume : ImageView = binding.resumeImageView
        val sound : ImageView = binding.soundImageView
        val music : ImageView = binding.musicImageView
        val about : ImageView = binding.aboutImageView
        singlePlayerButton.setOnClickListener {
            GameViewModel.gameMode = 1
            findNavController(it).navigate(R.id.action_home_to_singlePlayerFragment)
        }
        twoPlayerButton.setOnClickListener {
            GameViewModel.gameMode = 2
            findNavController(it).navigate(R.id.action_home_to_twoPlayerFragment)
        }
        resume.setOnClickListener{
            GameViewModel.readArray(load())
            if(GameViewModel.gameMode == 1)
                findNavController(it).navigate(R.id.action_home_to_singlePlayerFragment)
            else if(GameViewModel.gameMode == 2)
                findNavController(it).navigate(R.id.action_home_to_twoPlayerFragment)
        }

        music.setOnClickListener {
            if (MainActivity.homeMusic.isPlaying) {
                music.setImageResource(R.drawable.music_off)
                MainActivity.homeMusic.pause()
                isMusicMuted = true
            }
            else {
                music.setImageResource(R.drawable.music_on)
                MainActivity.homeMusic.start()
            }
        }

        sound.setOnClickListener{
            isSoundMuted = !isSoundMuted
            if(isSoundMuted)
                sound.setImageResource(R.drawable.sound_off)
            else
                sound.setImageResource(R.drawable.sound_on)

        }
        about.setOnClickListener {
            findNavController(it).navigate(R.id.action_home_to_aboutFragment)
        }


        return binding.root
    }

    override fun onPause() {
        super.onPause()
        MainActivity.homeMusic.pause()
        if(!isMusicMuted)
        MainActivity.gameMusic.start()
    }


}