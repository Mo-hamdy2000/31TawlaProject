package com.example.android.a31tawlaproject.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.android.a31tawlaproject.R
import com.example.android.a31tawlaproject.databinding.AboutFragmentBinding

class AboutFragment() : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: AboutFragmentBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.about_fragment,
            container,
            false
        )
        binding.textView3.animation = AnimationUtils.loadAnimation(this.requireContext(), R.anim.top_animation)

        return binding.root
    }

}
