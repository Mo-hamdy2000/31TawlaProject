package com.example.android.a31tawlaproject

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import com.example.android.a31tawlaproject.databinding.ActivityMainBinding
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var file: File
    override fun onCreate(savedInstanceState: Bundle?) {
        val path = this.filesDir
        file = File( path,"LastGame.txt")
        super.onCreate(savedInstanceState)
        mediaPlayer = MediaPlayer.create(this.applicationContext, R.raw.amahmed)
        mediaPlayer.start()
        mediaPlayer.isLooping = true
        setContentView(R.layout.activity_main)
        //val gameViewModel = GameViewModel(application, binding)
        val binding : ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        val singlePlayerButton : Button = binding.singlePlayerButton
        val twoPlayerButton : Button = binding.twoPlayerButton
        supportFragmentManager.beginTransaction().add(R.id.root, TwoPlayerFragment()).commit()
        GameViewModel.readArray(load())

        /*
        singlePlayerButton.setOnClickListener {
            supportFragmentManager.beginTransaction().add(R.id.root, SinglePlayerFragment()).commit()
        }
        twoPlayerButton.setOnClickListener {
            supportFragmentManager.beginTransaction().add(R.id.root, TwoPlayerFragment()).commit()
        }*/

    }
    private fun save() {
        val string : String = GameViewModel.writeArray()
        FileOutputStream(file,false).use {
            it.write(string.toByteArray(Charsets.UTF_8))
        }
       // Toast.makeText(this, "Saved to ${file.canonicalPath}", Toast.LENGTH_LONG).show()
         Toast.makeText(this,string , Toast.LENGTH_LONG).show()

    }
    private fun load():String?{
        val inputAsString :String
        try {
            inputAsString = FileInputStream(file).bufferedReader().use { it.readText() }
        }
        catch ( e : FileNotFoundException) {
            //fade resume button
            return null
        }
        return inputAsString
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        save()
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer.start()
    }
}