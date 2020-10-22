package com.example.android.a31tawlaproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.android.a31tawlaproject.databinding.GameFragmentBinding
import com.example.android.a31tawlaproject.game.GameViewModel
import com.example.android.a31tawlaproject.miscUtils.*
import java.lang.Integer.min
import java.util.*
import kotlin.concurrent.schedule

abstract class GameFragment : Fragment() {

    lateinit var binding: GameFragmentBinding

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
        val rollButton : Button = binding.rollButton
        val undoButton : Button = binding.undoButton
        val diceOne : ImageView = binding.diceImg1
        val diceTwo : ImageView = binding.diceImg2
        if(GameViewModel.read){
            setDiceImg(arrayOf(diceOneVal, diceTwoVal), arrayOf(diceOne, diceTwo))
        }
        for (i in 1..24) {
            // When cell change observer will update all its data to view
            //using class not instance :(
            GameViewModel.cellsArray[i - 1].numberOfPieces.observe(lifeCycleOwner, Observer {
                if (it > 0) {
                    // update text
                    getCellText(i).text = it.toString()
                    // update piece image
                    if (getCell(i).childCount == 1) {
                        val piece = ImageView(this.requireContext()) //kan 3amelli error 3ala context!! :O

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
            binding.scoreOneText.text = it.toString()
        })

        GameViewModel.scoreTwo.observe(viewLifecycleOwner, Observer {
            binding.scoreTwoText.text = it.toString()
        })

        gameViewModel.isUndoEnabled.observe(viewLifecycleOwner, Observer {
            if (it) {
                undoButton.isEnabled = true
                undoButton.alpha = 1.0f
            } else {
                undoButton.isEnabled = false
                undoButton.alpha = 0.5f
            }
        })

        GameViewModel.endGame.observe(viewLifecycleOwner, Observer {
            if (it) {
                Navigation.findNavController(requireView()).navigate(R.id.action_twoPlayerFragment_to_scoreFragment)
            }
        })

        GameViewModel.movedFromCell.observe(viewLifecycleOwner, Observer {
            if (it > 0 && it <= 24) {
                if (GameViewModel.movedFromCell.value!! <= 12) {
                    val piece = getCell(GameViewModel.movedFromCell.value!!).get(0)
                    piece.animation = AnimationUtils.loadAnimation(this.requireContext(), R.anim.bottom_fade_out_animation)
                } else {
                    val piece = getCell(GameViewModel.movedFromCell.value!!).get(1)
                    piece.animation = AnimationUtils.loadAnimation(this.requireContext(), R.anim.top_fade_out_animation)
                }
            }
        })

        GameViewModel.movedToCell.observe(viewLifecycleOwner, Observer {
            if (it > 0 && it <= 24) {
                if (GameViewModel.movedToCell.value!! <= 12) {
                    val piece = getCell(GameViewModel.movedToCell.value!!).get(0)
                    piece.animation = AnimationUtils.loadAnimation(this.requireContext(), R.anim.top_fade_in_animation)
                } else {
                    val piece = getCell(GameViewModel.movedToCell.value!!).get(1)
                    piece.animation = AnimationUtils.loadAnimation(this.requireContext(), R.anim.bottom_fade_in_animation)
                }
            }
        })

        rollButton.setOnClickListener {
            println("Aywa wsl")
            if (!GameViewModel.diceRolled) {

                GameViewModel.diceRolled = true
                setDiceImg(rollDice(GameViewModel.movesList), arrayOf(diceOne, diceTwo))
                animateRollMessage(GameViewModel.movesList[0], GameViewModel.movesList[1])
                if (GameViewModel.piecesAtHomePlayer[GameViewModel.currentColor - 1] == 15) {
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

    protected fun setDiceImg(diceVal: Array<Int>, dice: Array<ImageView>) {
        for (i in 0..1) {
            val imgSrc = when (diceVal[i]) {
                1 -> R.drawable.dice1
                2 -> R.drawable.dice2
                3 -> R.drawable.dice3
                4 -> R.drawable.dice4
                5 -> R.drawable.dice5
                else -> R.drawable.dice6
            }
            dice[i].setBackgroundResource(imgSrc)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        save()
        GameViewModel.read = false // 3ashan lamma mabye2felsh el app w yerga3 wara ygeeb single player tani law kan 3amel resume ablaha btefdal read true
    }

    fun animateRollMessage(minNum: Int, maxNum: Int) {
        val upper_mssg = binding.upperMssgImageView
        val lower_mssg = binding.lowerMssgImageView
        val mssg_screen = binding.messagesScreen
        upper_mssg.setImageResource(android.R.color.transparent)
        when (maxNum) {
            6 -> when (minNum) {
                1 -> lower_mssg.setImageResource(R.drawable.shish_yck)
                2 -> lower_mssg.setImageResource(R.drawable.shish_du)
                3 -> lower_mssg.setImageResource(R.drawable.shish_seh)
                4 -> lower_mssg.setImageResource(R.drawable.shish_gohar)
                5 -> lower_mssg.setImageResource(R.drawable.shish_bish)
                6 -> {
                    lower_mssg.setImageResource(R.drawable.douch_lower)
                    upper_mssg.setImageResource(R.drawable.douch_upper)
                }
            }
            5 -> when (minNum) {
                1 -> lower_mssg.setImageResource(R.drawable.bang_yck)
                2 -> lower_mssg.setImageResource(R.drawable.bang_du)
                3 -> lower_mssg.setImageResource(R.drawable.bang_seh)
                4 -> lower_mssg.setImageResource(R.drawable.bang_gohar)
                5 -> {
                    lower_mssg.setImageResource(R.drawable.dabsh_lower)
                    upper_mssg.setImageResource(R.drawable.dabsh_upper)
                }
            }
            4 -> when (minNum) {
                1 -> lower_mssg.setImageResource(R.drawable.gohar_yck)
                2 -> lower_mssg.setImageResource(R.drawable.gohar_du)
                3 -> lower_mssg.setImageResource(R.drawable.gohar_seh)
                4 -> {
                    lower_mssg.setImageResource(R.drawable.dabsh)
                    upper_mssg.setImageResource(R.drawable.durgy_upper)
                }
            }
            3 -> when (minNum) {
                1 -> lower_mssg.setImageResource(R.drawable.seh_yck)
                2 -> lower_mssg.setImageResource(R.drawable.seh_du)
                3 -> {
                    lower_mssg.setImageResource(R.drawable.dosa_lower)
                    upper_mssg.setImageResource(R.drawable.dosa_upper)
                }
            }
            2 -> when (minNum) {
                1 -> lower_mssg.setImageResource(R.drawable.du_yck)
                2 -> {
                    lower_mssg.setImageResource(R.drawable.dosa_lower)
                    upper_mssg.setImageResource(R.drawable.dobara_upper)
                }
            }
            1 -> {
                lower_mssg.setImageResource(R.drawable.dosa_lower)
                upper_mssg.setImageResource(R.drawable.hibyck_upper)
            }
        }
        mssg_screen.visibility = View.VISIBLE
        val top_anim = AnimationUtils.loadAnimation(this.requireContext() ,R.anim.top_animation)
        top_anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {
            }
            override fun onAnimationStart(p0: Animation?) {
            }
            override fun onAnimationEnd(p0: Animation?) {
                Timer().schedule(1000) {
                    binding.mainLayout.alpha = 1.0f
                    mssg_screen.visibility = View.INVISIBLE
                }
            }
        })
        binding.mainLayout.alpha = 0.7f
        upper_mssg.animation = top_anim
        lower_mssg.animation = AnimationUtils.loadAnimation(this.requireContext() ,R.anim.bottom_animation)

    }

}