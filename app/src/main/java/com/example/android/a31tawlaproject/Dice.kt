package com.example.android.a31tawlaproject

import android.graphics.drawable.AnimationDrawable
import android.view.View
import android.widget.ImageView
import java.util.*
fun rollDice(diceOne : ImageView,diceTwo : ImageView) {
    // animate(diceOne)
    //animate(diceTwo)
    diceOne.setBackgroundResource(createRandomNumber())
    diceTwo.setBackgroundResource(createRandomNumber())
}
fun createRandomNumber(): Int {
    return when(Random().nextInt(6)+1) {
        1 -> R.drawable.dice1
        2 -> R.drawable.dice2
        3 -> R.drawable.dice3
        4 -> R.drawable.dice4
        5 -> R.drawable.dice5
        else -> R.drawable.dice6
    }
}
/*  private fun animate(dice:ImageView) {
      dice.setBackgroundResource(R.drawable.dice_animation);
      val frameAnimation: AnimationDrawable = dice.background as AnimationDrawable

      frameAnimation.start()
  }

*/
