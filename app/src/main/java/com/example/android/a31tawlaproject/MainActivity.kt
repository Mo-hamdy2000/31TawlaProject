package com.example.android.a31tawlaproject

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class MainActivity : AppCompatActivity() {
    companion object{
        lateinit var internalStoragePath :File
        lateinit var mediaPlayer: MediaPlayer
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        internalStoragePath = File(filesDir, "LastGame.txt")
//      val bundle = Bundle()
//       bundle.putString("path",this.filesDir.absolutePath)
//        val singlePlayer = SinglePlayerFragment ()
//         val twoPlayer = TwoPlayerFragment ()
//      singlePlayer.arguments = bundle
//      twoPlayer.arguments = bundle
        mediaPlayer = MediaPlayer.create(this.applicationContext, R.raw.amahmed)
        mediaPlayer.start()
        mediaPlayer.isLooping = true
        setContentView(R.layout.activity_main)
        //val gameViewModel = GameViewModel(application, binding)

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