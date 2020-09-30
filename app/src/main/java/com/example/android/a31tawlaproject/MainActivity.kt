package com.example.android.a31tawlaproject

import android.media.MediaPlayer
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaPlayer = MediaPlayer.create(this.applicationContext, R.raw.amahmed)
        mediaPlayer.start()
        mediaPlayer.isLooping = true
        setContentView(R.layout.activity_main)
        //val gameViewModel = GameViewModel(application, binding)


    }
}