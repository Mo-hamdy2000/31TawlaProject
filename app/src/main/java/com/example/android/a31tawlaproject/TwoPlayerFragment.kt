package com.example.android.a31tawlaproject

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.android.a31tawlaproject.databinding.GameFragmentBinding

class TwoPlayerFragment : GameFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = super.onCreateView(inflater, container, savedInstanceState)
        val application = requireNotNull(this.activity).application
        val gameViewModel = TwoPlayerViewModel(application, binding)
        this.initializationWithViewModel(gameViewModel, this)
        return root
    }
}