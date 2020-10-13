package com.example.android.a31tawlaproject.miscUtils

import com.example.android.a31tawlaproject.MainActivity
import com.example.android.a31tawlaproject.game.GameViewModel
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
val file =
    MainActivity.internalStoragePath

fun load():String?{
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
 fun save() {
    val string : String = GameViewModel.writeArray()
    FileOutputStream(file,false).use {
        it.write(string.toByteArray(Charsets.UTF_8))
    }
}

