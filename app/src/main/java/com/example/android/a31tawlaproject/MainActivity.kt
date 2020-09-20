package com.example.android.a31tawlaproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_fragment)
        val rollButton : Button = findViewById(R.id.rollButton)
        val diceOne : ImageView = findViewById(R.id.diceImg1)
        val diceTwo : ImageView = findViewById(R.id.diceImg2)

        rollButton.setOnClickListener{
            rollDice(diceOne,diceTwo)
        }
    }
}