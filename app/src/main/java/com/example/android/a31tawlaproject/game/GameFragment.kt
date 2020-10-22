package com.example.android.a31tawlaproject.game

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.telephony.gsm.GsmCellLocation
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.android.a31tawlaproject.R
import com.example.android.a31tawlaproject.databinding.GameFragmentBinding
import com.example.android.a31tawlaproject.miscUtils.diceOneVal
import com.example.android.a31tawlaproject.miscUtils.diceTwoVal
import com.example.android.a31tawlaproject.miscUtils.rollDice
import com.example.android.a31tawlaproject.miscUtils.save

abstract class GameFragment : Fragment() {

    private lateinit var binding: GameFragmentBinding
    private lateinit var diceImages : Array<ImageView>
    private var diceValues = arrayOf(diceOneVal, diceTwoVal)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate view and obtain an instance of the binding class.
        //m- oo is not used
        //val oo = activity?.filesDir
        binding = DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false)
        return binding.root
    }

    fun initializationWithViewModel(gameViewModel: GameViewModel, lifeCycleOwner: Fragment) {
       // val rollButton : Button = binding.rollButton
        val undoButton : Button = binding.undoButton
        diceImages = arrayOf(binding.diceImg1, binding.diceImg2)
        if(GameViewModel.read){
            setDiceImg(true)
        }
        for (i in 1..24) {
            // When cell change observer will update all its data to view
            //using class not instance :(
            GameViewModel.cellsArray[i - 1].numberOfPieces.observe(lifeCycleOwner, Observer {
                Log.i("AAAAA","OBSERVED")
                if (it > 0) {
                    // update text
                    getCellText(i).text = it.toString()
                    // update piece image
                    if (getCell(i).childCount == 1) {
                        val piece =
                            ImageView(this.requireContext())//kan 3amelli error 3ala context!! :O

                        if (GameViewModel.cellsArray[i - 1].color == 1)
                            piece.setImageResource(R.drawable.piece1)
                        else if (GameViewModel.cellsArray[i - 1].color == 2)
                            piece.setImageResource(R.drawable.piece2)

                        if (i <= 12) {
                            piece.scaleType = ImageView.ScaleType.FIT_START
                            getCell(i).addView(piece, 0)
                        } else {
                            piece.scaleType = ImageView.ScaleType.FIT_END
                            getCell(i).addView(piece)
                        }
                    }
                } else {
                    getCellText(i).text = ""
                    if (getCell(i).childCount > 1 && i <= 12) {
                        getCell(i).removeViewAt(0)
                    } else if (getCell(i).childCount > 1 && i > 12) {
                        getCell(i).removeViewAt(1)
                    }
                }
            })

            GameViewModel.cellsArray[i - 1].isPieceHighlighted.observe(lifeCycleOwner, Observer {
                // update piece image
                if (GameViewModel.cellsArray[i - 1].numberOfPieces.value!! > 0) {
                    lateinit var piece: ImageView
                    if (getCell(i).childCount > 1 && i <= 12) {
                        println(i)
                        piece = getCell(i).getChildAt(0) as ImageView
                    } else if (getCell(i).childCount > 1 && i > 12)
                        piece = getCell(i).getChildAt(1) as ImageView

                    if (GameViewModel.cellsArray[i - 1].color == 1 && !it)
                        piece.setImageResource(R.drawable.piece1)
                    else if (GameViewModel.cellsArray[i - 1].color == 2 && !it)
                        piece.setImageResource(R.drawable.piece2)
                    else if (GameViewModel.cellsArray[i - 1].color == 1 && it)
                        piece.setImageResource(R.drawable.piece1_highlighted)
                    else
                        piece.setImageResource(R.drawable.piece2_highlighted)
                }

            })
            GameViewModel.cellsArray[i - 1].isCellHighlighted.observe(lifeCycleOwner, Observer {
                if (it) {
                    println("Wslnaaaaaaaaaaaaaaa ll highlight mn hnaaaaaaaaaaaa")
                    if (i <= 12 && i % 2 != 0) {
                        getCell(i).setBackgroundResource(R.drawable.cell_gold_upper_highlighted)
                    } else if (i > 12 && i % 2 != 0) {
                        getCell(i).setBackgroundResource(R.drawable.cell_gold_lower_highlighted)
                    } else if (i <= 12 && i % 2 == 0) {
                        getCell(i).setBackgroundResource(R.drawable.cell_blue_upper_highlighted)
                    } else {
                        getCell(i).setBackgroundResource(R.drawable.cell_blue_lower_highlighted)
                    }
                } else {
                    if (i <= 12 && i % 2 != 0) {
                        getCell(i).setBackgroundResource(R.drawable.cell_gold_upper)
                    } else if (i > 12 && i % 2 != 0) {
                        getCell(i).setBackgroundResource(R.drawable.cell_gold_lower)
                    } else if (i <= 12 && i % 2 == 0) {
                        getCell(i).setBackgroundResource(R.drawable.cell_blue_upper)
                    } else {
                        getCell(i).setBackgroundResource(R.drawable.cell_blue_lower)
                    }
                }
            })
        }

        GameViewModel.scoreOne.observe(viewLifecycleOwner, Observer {
            binding.scoreText.text = it.toString() + " - " + GameViewModel.scoreTwo.value.toString()
        })

        GameViewModel.scoreTwo.observe(viewLifecycleOwner, Observer {
            binding.scoreText.text = GameViewModel.scoreOne.value.toString() + " - "+ it.toString()
        })
        gameViewModel.isUndoEnabled.observe(viewLifecycleOwner, Observer {
            println("Observed khalas")
            if (it) {
                undoButton.isEnabled = true
                undoButton.alpha = 1.0f
            } else {
                undoButton.isEnabled = false
                undoButton.alpha = 0.5f
            }
        })
GameViewModel.isMoved.observe(viewLifecycleOwner, Observer {
    if(it){
        if(!GameViewModel.movesList.contains(diceOneVal) || (diceOneVal == diceTwoVal && GameViewModel.movesList.size==2))
            removeDiceImg(diceOneVal,diceImages[0])
        else if(diceOneVal!= diceTwoVal)
            removeDiceImg(diceTwoVal,diceImages[1])
    }
    else{
        if(GameViewModel.diceRolled)
            setDiceImg(true)
    }
})
        GameViewModel.endGame.observe(viewLifecycleOwner, Observer {
            if (it) {
                Navigation.findNavController(requireView()).navigate(R.id.action_twoPlayerFragment_to_scoreFragment)
            }
        })
    GameViewModel.currentColor.observe(viewLifecycleOwner, Observer {
        binding.turnText.text = if(GameViewModel.currentColor.value == 1)
            "Yellow's Turn"
        else
            "Blue's Turn"
        if(!GameViewModel.read) {
            diceValues = rollDice(GameViewModel.movesList)
            setDiceImg(false)
        }
    })
        binding.lifecycleOwner = lifeCycleOwner
        binding.gameViewModel = gameViewModel
      //  GameViewModel.read = false
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
            else -> ConstraintLayout(this.requireContext())
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
            else -> TextView(this.requireContext())
        }
    }


    private fun setDiceImg(reset : Boolean) {
        for (i in 0..1) {
            val imgSrc = when (diceValues[i]) {
                1 -> R.drawable.dice1
                2 -> R.drawable.dice2
                3 -> R.drawable.dice3
                4 -> R.drawable.dice4
                5 -> R.drawable.dice5
                else -> R.drawable.dice6
            }
            if(reset)
                diceImages[i].setBackgroundResource(imgSrc)
            else {
                diceImages[i].setBackgroundResource(R.drawable.dice_animation)
                val frameAnimation: AnimationDrawable =
                    diceImages[i].background as AnimationDrawable
                frameAnimation.start()
                diceImages[i].postDelayed({ diceImages[i].setBackgroundResource(imgSrc) }, 12 * 100)
                GameViewModel.diceRolled = true
            }

        }
    }
    private fun removeDiceImg(diceVal: Int, dice: ImageView) {
        for (i in 0..1) {
            val imgSrc = when (diceVal) {
                1 -> R.drawable.dice1_faded
                2 -> R.drawable.dice2_faded
                3 -> R.drawable.dice3_faded
                4 -> R.drawable.dice4_faded
                5 -> R.drawable.dice5_faded
                else -> R.drawable.dice6_faded
            }
            dice.setBackgroundResource(imgSrc)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        save()
        GameViewModel.read = false // 3ashan lamma mabye2felsh el app w yerga3 wara ygeeb single player tani law kan 3amel resume ablaha btefdal read true
    }

}