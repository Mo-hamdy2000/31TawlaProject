package com.example.android.a31tawlaproject

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.a31tawlaproject.databinding.GameFragmentBinding

class GameFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate view and obtain an instance of the binding class.
        println("Hoooooooooolllla")
        val binding: GameFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false)
        Log.i("GameFragment", "55555")
        // Specify the current activity as the lifecycle owner.
        val application = requireNotNull(this.activity).application
        val gameViewModel = GameViewModel(application, binding)
        /*binding.cell24.setOnClickListener {
            //Toast.makeText(this, "IT WORKS !!!", Toast.LENGTH_SHORT).show()
            Log.i("GameFragment", "here111111111111111")
            println("Hellllo")
            gameViewModel.addPiece(12)
        }*/
        binding.setLifecycleOwner(this)
        binding.gameViewModel = gameViewModel
        return binding.root
    }
}