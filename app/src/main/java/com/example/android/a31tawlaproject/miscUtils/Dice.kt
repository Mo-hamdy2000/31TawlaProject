package com.example.android.a31tawlaproject.miscUtils

import android.util.Log
import java.util.*

val diceValues = arrayOf(0,0)
var test = 0
val diceTest = arrayOf(5,1,3,5,3,6)
fun rollDice(movesList: MutableList<Int>) {

    diceValues[0] = Random().nextInt(6) + 1
    diceValues[1] = Random().nextInt(6) + 1
//    diceValues[0] = diceTest[test]
//    test++
//    diceValues[1] = diceTest[test]
//    test++
    Log.i("DICE", "${diceValues[0]} and ${diceValues[1]}")
    movesList.add(diceValues[0])

    if (diceValues[0] == diceValues[1]) {
        for (i in 1..3) {
            movesList.add(diceValues[0])
        }
    }
    else {
        movesList.add(diceValues[1])
    }
    movesList.sort()
    //na2alt elli betset elimage felgamefragment
}

