package com.example.android.a31tawlaproject.miscUtils

import java.util.*


var diceOneVal = 0
var diceTwoVal = 0
fun rollDice(movesList: MutableList<Int>) : Array<Int> {
    // animate(diceOne)
    //animate(diceTwo)
    diceOneVal = Random().nextInt(6) + 1
    diceTwoVal = Random().nextInt(6) + 1
    movesList.add(diceOneVal)

    if (diceOneVal == diceTwoVal) {
        for (i in 1..3) {
            movesList.add(diceOneVal)
        }
    }
    else {
        movesList.add(diceTwoVal)
    }
    movesList.sort()
    return arrayOf(diceOneVal,diceTwoVal)
    //na2alt elli betset elimage felgamefragment
}

