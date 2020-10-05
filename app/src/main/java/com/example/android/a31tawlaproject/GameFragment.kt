package com.example.android.a31tawlaproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.android.a31tawlaproject.databinding.GameFragmentBinding

abstract class GameFragment : Fragment() {

    lateinit var binding: GameFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate view and obtain an instance of the binding class.
        binding = DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false)
        return binding.root
    }

    fun initializationWithViewModel (gameViewModel: GameViewModel, lifeCycleOwner: Fragment) {
        val rollButton : Button = binding.rollButton
        val undoButton : Button = binding.undoButton
        val diceOne : ImageView = binding.diceImg1
        val diceTwo : ImageView = binding.diceImg2
        for (i in 1..24) {
            // When cell change observer will update all its data to view
            gameViewModel.cellsArray[i - 1].numberOfPieces.observe(lifeCycleOwner, Observer {
                if (it > 0) {
                    // update text
                    getCellText(i).text = it.toString()
                    // update piece image
                    if (getCell(i).childCount == 1) {
                        val piece = ImageView(this.context!!)

                        if(gameViewModel.currentColor == 1)
                            piece.setImageResource(R.drawable.piece1)
                        else if(gameViewModel.currentColor == 2)
                            piece.setImageResource(R.drawable.piece2)

                        if (i <= 12) {
                            piece.scaleType = ImageView.ScaleType.FIT_START
                            getCell(i).addView(piece, 0)
                        }
                        else {
                            piece.scaleType = ImageView.ScaleType.FIT_END
                            getCell(i).addView(piece)
                        }
                    }
                } else {
                    getCellText(i).text = ""
                    if (getCell(i).childCount > 1 && i <= 12) {
                        getCell(i).removeViewAt(0)
                    }
                    else if  (getCell(i).childCount > 1 && i > 12) {
                        getCell(i).removeViewAt(1)
                    }
                }
            })

            gameViewModel.cellsArray[i - 1].isPieceHighlighted.observe(lifeCycleOwner, Observer {
                    // update piece image
                if (gameViewModel.cellsArray[i - 1].numberOfPieces.value!! > 0) {
                    lateinit var piece: ImageView
                    if (getCell(i).childCount > 1 && i <= 12) {
                        println(i)
                        piece = getCell(i).getChildAt(0) as ImageView }

                    else if (getCell(i).childCount > 1 && i > 12)
                        piece = getCell(i).getChildAt(1) as ImageView

                    if(gameViewModel.currentColor == 1 && !it)
                        piece.setImageResource(R.drawable.piece1)
                    else if(gameViewModel.currentColor == 2 && !it)
                        piece.setImageResource(R.drawable.piece2)
                    else if(gameViewModel.currentColor == 1 && it)
                        piece.setImageResource(R.drawable.piece1_highlighted)
                    else
                        piece.setImageResource(R.drawable.piece2_highlighted)
                }

            })
            gameViewModel.cellsArray[i - 1].isCellHighlighted.observe(lifeCycleOwner, Observer {
                if (it) {
                    println("Wslnaaaaaaaaaaaaaaa ll highlight mn hnaaaaaaaaaaaa")
                    if (i <= 12 && i % 2 != 0) {
                        getCell(i).setBackgroundResource(R.drawable.cell_gold_upper_highlighted)
                    }
                    else if (i > 12 && i % 2 != 0) {
                        getCell(i).setBackgroundResource(R.drawable.cell_gold_lower_highlighted)
                    }
                    else if (i <= 12 && i % 2 == 0) {
                        getCell(i).setBackgroundResource(R.drawable.cell_blue_upper_highlighted)
                    }
                    else {
                        getCell(i).setBackgroundResource(R.drawable.cell_blue_lower_highlighted)
                    }
                }
                else {
                    if (i <= 12 && i % 2 != 0) {
                        getCell(i).setBackgroundResource(R.drawable.cell_gold_upper)
                    }
                    else if (i > 12 && i % 2 != 0) {
                        getCell(i).setBackgroundResource(R.drawable.cell_gold_lower)
                    }
                    else if (i <= 12 && i % 2 == 0) {
                        getCell(i).setBackgroundResource(R.drawable.cell_blue_upper)
                    }
                    else {
                        getCell(i).setBackgroundResource(R.drawable.cell_blue_lower)
                    }
                }
            })
            /*
            gameViewModel.cellsArray[i].observe(lifeCycleOwner, Observer<Cell> {
                if (it.numberOfPieces > 0) {
                    // update text
                    getCellText(it.cellNumber).text = it.numberOfPieces.toString()
                    // update piece image
                    lateinit var piece: ImageView
                    if (getCell(it.cellNumber).childCount > 1 && it.cellNumber <= 12)
                        piece = getCell(it.cellNumber).getChildAt(0) as ImageView
                    else if (getCell(it.cellNumber).childCount > 1 && it.cellNumber > 12)
                        piece = getCell(it.cellNumber).getChildAt(1) as ImageView
                    else
                        piece = ImageView(this.context!!)

                    if(gameViewModel.currentColor == 1 && !it.isPieceHighlighted)
                        piece.setImageResource(R.drawable.piece1)
                    else if(gameViewModel.currentColor == 2 && !it.isPieceHighlighted)
                        piece.setImageResource(R.drawable.piece2)
                    else if(gameViewModel.currentColor == 1 && it.isPieceHighlighted)
                        piece.setImageResource(R.drawable.piece1_highlighted)
                    else
                        piece.setImageResource(R.drawable.piece2_highlighted)

                    if (getCell(it.cellNumber).childCount == 1) {
                        if (it.cellNumber <= 12) {
                            piece.scaleType = ImageView.ScaleType.FIT_START
                            getCell(it.cellNumber).addView(piece, 0)
                        }
                        else {
                            piece.scaleType = ImageView.ScaleType.FIT_END
                            getCell(it.cellNumber).addView(piece)
                        }
                    }
                } else {
                    getCellText(it.cellNumber).text = ""
                    if (getCell(it.cellNumber).childCount == 1 && it.cellNumber <= 12) {
                        getCell(it.cellNumber).removeViewAt(0)
                    }
                    else if  (getCell(it.cellNumber).childCount == 1 && it.cellNumber > 12) {
                        getCell(it.cellNumber).removeViewAt(0)
                    }
                }
                // update cell background
                if (!it.isCellHighlighted.value!!) {
                    if (it.cellNumber <= 12 && it.cellNumber % 2 != 0) {
                        getCell(it.cellNumber).setBackgroundResource(R.drawable.cell_gold_upper)
                    }
                    else if (it.cellNumber > 12 && it.cellNumber % 2 != 0) {
                        getCell(it.cellNumber).setBackgroundResource(R.drawable.cell_gold_lower)
                    }
                    else if (it.cellNumber <= 12 && it.cellNumber % 2 == 0) {
                        getCell(it.cellNumber).setBackgroundResource(R.drawable.cell_blue_upper)
                    }
                    else {
                        getCell(it.cellNumber).setBackgroundResource(R.drawable.cell_blue_lower)
                    }
                } else {
                    println("Wslnaaaaaaaaaaaaaaa ll highlight")
                    if (it.cellNumber <= 12 && it.cellNumber % 2 != 0) {
                        getCell(it.cellNumber).setBackgroundResource(R.drawable.cell_gold_upper_highlighted)
                    }
                    else if (it.cellNumber > 12 && it.cellNumber % 2 != 0) {
                        getCell(it.cellNumber).setBackgroundResource(R.drawable.cell_gold_lower_highlighted)
                    }
                    else if (it.cellNumber <= 12 && it.cellNumber % 2 == 0) {
                        getCell(it.cellNumber).setBackgroundResource(R.drawable.cell_blue_upper_highlighted)
                    }
                    else {
                        getCell(it.cellNumber).setBackgroundResource(R.drawable.cell_blue_lower_highlighted)
                    }
                }
            })*/
        }

        gameViewModel.isUndoEnabled.observe(viewLifecycleOwner, Observer {
            println("Observed khalas")
            if (it) {
                undoButton.isEnabled = true
                undoButton.alpha = 1.0f
            } else {
                undoButton.isEnabled = true
                undoButton.alpha = 0.5f
            }
        })

        rollButton.setOnClickListener {
            if (!gameViewModel.diceRolled) {
                rollDice(diceOne,diceTwo, gameViewModel.movesList)
                gameViewModel.diceRolled = true
                if (gameViewModel.piecesAtHomePlayer[gameViewModel.currentColor - 1] == 15) {
                    gameViewModel.collectPieces()
                }
                gameViewModel.check()
            }
        }
        binding.lifecycleOwner = lifeCycleOwner
        binding.gameViewModel = gameViewModel
    }

    private fun getCell(cellNum: Int): ConstraintLayout {
        return when (cellNum) {
            1 -> binding.cell1
            2 -> binding.cell2
            3 -> binding.cell3
            4 -> binding.cell4
            5 -> binding.cell5
            6 -> binding.cell6
            7 -> binding.cell7
            8 -> binding.cell8
            9 -> binding.cell9
            10 -> binding.cell10
            11 -> binding.cell11
            12 -> binding.cell12
            13 -> binding.cell13
            14 -> binding.cell14
            15 -> binding.cell15
            16 -> binding.cell16
            17 -> binding.cell17
            18 -> binding.cell18
            19 -> binding.cell19
            20 -> binding.cell20
            21 -> binding.cell21
            22 -> binding.cell22
            23 -> binding.cell23
            24 -> binding.cell24
            else -> ConstraintLayout(this.context!!)
        }
    }

    private fun getCellText(cellNum: Int): TextView {
        return when (cellNum) {
            1 -> binding.textViewCell1
            2 -> binding.textViewCell2
            3 -> binding.textViewCell3
            4 -> binding.textViewCell4
            5 -> binding.textViewCell5
            6 -> binding.textViewCell6
            7 -> binding.textViewCell7
            8 -> binding.textViewCell8
            9 -> binding.textViewCell9
            10 -> binding.textViewCell10
            11 -> binding.textViewCell11
            12 -> binding.textViewCell12
            13 -> binding.textViewCell13
            14 -> binding.textViewCell14
            15 -> binding.textViewCell15
            16 -> binding.textViewCell16
            17 -> binding.textViewCell17
            18 -> binding.textViewCell18
            19 -> binding.textViewCell19
            20 -> binding.textViewCell20
            21 -> binding.textViewCell21
            22 -> binding.textViewCell22
            23 -> binding.textViewCell23
            24 -> binding.textViewCell24
            else -> TextView(this.context!!)
        }
    }
}