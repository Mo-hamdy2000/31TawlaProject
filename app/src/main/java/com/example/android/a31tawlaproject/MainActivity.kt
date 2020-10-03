package com.example.android.a31tawlaproject

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button

import androidx.appcompat.app.AppCompatActivity
import com.example.android.a31tawlaproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaPlayer = MediaPlayer.create(this.applicationContext, R.raw.amahmed)
        mediaPlayer.start()
        mediaPlayer.isLooping = true
        setContentView(R.layout.activity_main)
        //val gameViewModel = GameViewModel(application, binding)
        val binding : ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        val singlePlayerButton : Button = binding.singlePlayerButton
        val twoPlayerButton : Button = binding.twoPlayerButton
        supportFragmentManager.beginTransaction().add(R.id.root, SinglePlayerFragment()).commit()

//        singlePlayerButton.setOnClickListener {
//            supportFragmentManager.beginTransaction().add(R.id.root, SinglePlayerFragment()).commit()
//        }
//        twoPlayerButton.setOnClickListener {
//            supportFragmentManager.beginTransaction().add(R.id.root, TwoPlayerFragment()).commit()
//        }

    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.pause()
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer.start()
    }
}