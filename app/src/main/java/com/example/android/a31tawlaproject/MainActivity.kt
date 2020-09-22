package com.example.android.a31tawlaproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.example.android.a31tawlaproject.databinding.GameFragmentBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val rollButton : Button = findViewById(R.id.rollButton)
        val diceOne : ImageView = findViewById(R.id.diceImg1)
        val diceTwo : ImageView = findViewById(R.id.diceImg2)
        val cell : ConstraintLayout = findViewById(R.id.cell_24)
        //val binding: GameFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false)
        val application = requireNotNull(this).application
        //val gameViewModel = GameViewModel(application, binding)
        cell.setOnClickListener {
            Log.i("GameFragment", "here111111111111111")
            println("Hellllo")
            //gameViewModel.addPiece(12)
        }


        rollButton.setOnClickListener{
            rollDice(diceOne,diceTwo)
        }
    }
}