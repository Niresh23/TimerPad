package com.niksama3.timerpad.swipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.niksama3.timerpad.R
import com.niksama3.timerpad.databinding.FragmentSwipeBinding

class SwipeFragment : Fragment() {

    private lateinit var binding: FragmentSwipeBinding

    private val viewModel: SwipeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSwipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.liveData.observe(viewLifecycleOwner) {
            bindCard(it)
        }
        binding.swipeMotionLayout.setTransitionListener(object : TransitionAdapter() {
            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                when (currentId) {
                    R.id.offScreenPass,
                    R.id.offScreenLike -> {
                        motionLayout?.progress = 0f
                        motionLayout?.setTransition(R.id.rest, R.id.like)
                        viewModel.swipe()
                    }
                }
            }
        })
    }


    private fun bindCard(model: SwipeModel) {
        binding.topCard.setBackgroundColor(model.top.backgroundColor)
        binding.bottomCard.setBackgroundColor(model.bottom.backgroundColor)
    }
}