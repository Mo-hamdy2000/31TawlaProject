package com.example.android.a31tawlaproject

import android.widget.ImageView
import java.util.*
var diceOneVal = 0
var diceTwoVal = 0
fun rollDice(diceOne : ImageView,diceTwo : ImageView, movesList: MutableList<Int>) {
    // animate(diceOne)
    //animate(diceTwo)
    diceOneVal = Random().nextInt(6)+1
    movesList.add(diceOneVal)
    diceOne.setBackgroundResource(createRandomNumber(diceOneVal))
    diceTwoVal = Random().nextInt(6)+1
    if (diceOneVal == diceTwoVal) {
        for (i in 1..3) {
            movesList.add(diceOneVal)
        }
    }
    else {
        movesList.add(diceTwoVal)
    }
    diceTwo.setBackgroundResource(createRandomNumber(diceTwoVal))
}
fun createRandomNumber(diceVal: Int): Int {
    return when(diceVal) {
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
