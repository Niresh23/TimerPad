package com.niksama3.timerpad

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.niksama3.timerpad.databinding.FragmentChooserBinding
import com.niksama3.timerpad.swipe.SwipeFragment
import com.niksama3.timerpad.swipe.SwipeFragmentProgrammatically

class FragmentChooser : Fragment() {
    private lateinit var binding: FragmentChooserBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChooserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btn1.setOnClickListener {
            openFragment<FirstFragment>()
        }

        binding.btn2.setOnClickListener {
            openFragment<SwipeFragment>()
        }

        binding.btn3.setOnClickListener {
            openFragment<SwipeFragmentProgrammatically>()
        }

        binding.third.setOnClickListener {
            openFragment<ThirdFragment>()
        }
    }

    private inline fun <reified T : Fragment> openFragment() {
        requireActivity().supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<T>(R.id.fragment_container).addToBackStack(null)
        }
    }
}