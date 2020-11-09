package com.example.android.a31tawlaproject

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class MainActivity : AppCompatActivity() {
    companion object{
        lateinit var internalStoragePath :File
        lateinit var gameMusic: MediaPlayer
        lateinit var homeMusic : MediaPlayer
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        internalStoragePath = File(filesDir, "LastGame.txt")
        gameMusic = MediaPlayer.create(this.applicationContext, R.raw.amahmed)
        gameMusic.isLooping = true
        homeMusic = MediaPlayer.create(this.applicationContext, R.raw.home_fragment_music)
        homeMusic.start()

        setContentView(R.layout.activity_main)
        //val gameViewModel = GameViewModel(application, binding)

    }


    override fun onPause() {
        super.onPause()
        homeMusic.pause()
    }
    override fun onResume() {
        super.onResume()
        homeMusic.start()
    }
}