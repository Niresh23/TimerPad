package com.niksama3.timerpad.swipe

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SwipeViewModel: ViewModel() {

    val liveData: LiveData<SwipeModel>
        get() = mutableLiveData

    private val mutableLiveData = MutableLiveData<SwipeModel>()

    private val data = listOf(
        SwipeCardModel(backgroundColor = Color.parseColor("#E91E63")),
        SwipeCardModel(backgroundColor = Color.parseColor("#2196F3")),
        SwipeCardModel(backgroundColor = Color.parseColor("#F44336")),
        SwipeCardModel(backgroundColor = Color.parseColor("#9E9E9E"))
    )

    private var currentIndex = 0

    private val topCard
        get() = data[currentIndex % data.size]

    private val bottomCard
        get() = data[(currentIndex + 1) % data.size]

    init {
        update()
    }

    fun swipe() {
        currentIndex++
        update()
    }

    private fun update() {
        mutableLiveData.value = SwipeModel(
            top = topCard,
            bottom = bottomCard
        )
    }



}