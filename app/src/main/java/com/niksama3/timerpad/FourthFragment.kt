package com.niksama3.timerpad

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.niksama3.timerpad.adapters.NameAdapter
import com.niksama3.timerpad.databinding.FourthFragmentBinding
import com.niksama3.timerpad.databinding.FragmentThirdBinding
import com.niksama3.timerpad.databinding.SwipeLayoutBinding

class FourthFragment : Fragment() {
    private lateinit var binding: SwipeLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SwipeLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }
}