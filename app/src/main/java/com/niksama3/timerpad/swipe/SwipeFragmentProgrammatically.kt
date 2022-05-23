package com.niksama3.timerpad.swipe

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.niksama3.timerpad.R
import com.niksama3.timerpad.databinding.FragmentSwipeProgrammaticallyBinding
import com.niksama3.timerpad.swipe.view.SwipeableStackViewAdapter

class SwipeFragmentProgrammatically : Fragment() {
    private lateinit var binding: FragmentSwipeProgrammaticallyBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSwipeProgrammaticallyBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val models = listOf(
            SwipeCardModel(R.color.purple_200),
            SwipeCardModel(R.color.purple_500),
            SwipeCardModel(R.color.purple_700),
            SwipeCardModel(R.color.teal_200),
            SwipeCardModel(R.color.teal_700),
            SwipeCardModel(R.color.black),
            SwipeCardModel(R.color.light_blue_600),
            SwipeCardModel(R.color.light_blue_900),
            SwipeCardModel(R.color.light_blue_A200),
            SwipeCardModel(R.color.light_blue_A400),
            SwipeCardModel(R.color.black_overlay),
            SwipeCardModel(R.color.pink)
        )

        val adapter = SwipeableStackViewAdapter(
            requireContext(),
            R.layout.swipe_card_layout,
            models
        )

        binding.swipeView.setAdapter(adapter)
    }
}