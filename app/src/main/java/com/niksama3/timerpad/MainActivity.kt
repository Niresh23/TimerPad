package com.niksama3.timerpad

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.niksama3.timerpad.databinding.ActivityFullscreenBinding
import com.niksama3.timerpad.swipe.SwipeFragment
import com.niksama3.timerpad.swipe.SwipeFragmentProgrammatically

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFullscreenBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace<FragmentChooser>(binding.fragmentContainer.id).addToBackStack(null)
            }
        }
    }


}