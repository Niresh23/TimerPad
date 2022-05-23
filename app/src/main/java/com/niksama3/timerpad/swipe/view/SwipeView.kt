package com.niksama3.timerpad.swipe.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.constraintlayout.motion.widget.*
import androidx.constraintlayout.motion.widget.MotionScene.Transition
import androidx.constraintlayout.widget.ConstraintSet
import com.niksama3.timerpad.R
import com.niksama3.timerpad.swipe.SwipeCardModel

class SwipeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MotionLayout(context, attrs, defStyleAttr) {

    private val likeButtonView = ImageView(context)
    private val passButtonView = ImageView(context)


    @SuppressLint("ResourceType", "UseCompatLoadingForDrawables")
    fun setAdapter(adapter: BaseAdapter) {

        val scene = MotionScene(this)

        val views = mutableListOf<View>()

        val transitions = mutableListOf<Transition>()
        val restSetId = View.generateViewId()
        val restSet = ConstraintSet()
        val lastSets = mutableListOf<LastSet>()

        likeButtonView.id = generateViewId()
        passButtonView.id = generateViewId()

        likeButtonView.setImageDrawable(context.getDrawable(R.drawable.favorite))
        passButtonView.setImageDrawable(context.getDrawable(R.drawable.cancel))
        likeButtonView.setBackgroundResource(R.drawable.background_circle)
        passButtonView.setBackgroundResource(R.drawable.background_circle)

        for(i in 0 until adapter.count) {
            val view = adapter.getView(i, null, this)
            view.id = View.generateViewId()
            views.add(view)
        }

        views.add(likeButtonView)
        views.add(passButtonView)

        views.forEachIndexed { index, topView ->

            if(index == 0) {

                connectTopCardRestSet(topView, restSet)

                views.subList(index + 1, views.size).forEach { bottomView ->
                    connectBottomCardRestSet(bottomView, restSet)
                }

                val lastSet = createSetsForTopCard(
                    scene,
                    topView,
                    if (index < views.size - 1)
                        views.subList(index + 1, views.size)
                    else null,
                    restSetId,
                    restSet,
                    transitions
                )

                lastSets.add(lastSet)
            } else {

                val tempList = mutableListOf<LastSet>()

                lastSets.forEach { lastSet ->
                    tempList.add(
                        createSetsForTopCard(
                            scene,
                            topView,
                            if (index < views.size - 1)
                                views.subList(index + 1, views.size)
                            else null,
                            lastSet.likeScreenOffSetId,
                            lastSet.likeScreenOffSet,
                            transitions
                        )
                    )

                    tempList.add(
                        createSetsForTopCard(
                            scene,
                            topView,
                            if (index < views.size - 1)
                                views.subList(index + 1, views.size)
                            else null,
                            lastSet.passScreenOffSetId,
                            lastSet.passScreenOffSet,
                            transitions
                        )
                    )
                }

                lastSets.clear()
                lastSets.addAll(tempList)
            }
        }

        views.asReversed().forEach {
            addView(it)
        }

        transitions.forEach {
            scene.addTransition(it)
        }

        setScene(scene)
        setTransition(transitions.first())
    }

    private fun createSetsForTopCard(
        scene: MotionScene,
        topView: View,
        bottomViews: List<View>?,
        lastSetId: Int,
        lastSet: ConstraintSet,
        transitions: MutableList<Transition>
    ) : LastSet {

        val passSetId = View.generateViewId()
        val passSet = ConstraintSet()
        connectPassTopCardSet(topView, passSet)

        val likeSetId = View.generateViewId()
        val likeSet = ConstraintSet()
        connectLikeTopCard(topView, likeSet)

        val passScreenOffSetId = View.generateViewId()
        val passScreenOffSet = ConstraintSet()
        connectPassScreenOffTopCardSet(topView, passScreenOffSet)

        val likeScreenOffSetId = View.generateViewId()
        val likeScreenOffSet = ConstraintSet()
        connectLikeScreenOffSet(topView, likeScreenOffSet)

        bottomViews?.let {
            it.forEachIndexed { index, bottomView ->
                if (index == 0) {
                    connectPassBottomCardSet(bottomView, passSet)
                    connectPassBottomCardSet(bottomView, likeSet)
                    connectPassBottomCardSet(bottomView, passScreenOffSet)
                    connectPassBottomCardSet(bottomView, likeScreenOffSet)
                } else {
                    connectPassBottomCardSet(bottomView, passSet)
                    connectPassBottomCardSet(bottomView, likeSet)
                    connectBottomCardRestSet(bottomView, passScreenOffSet)
                    connectBottomCardRestSet(bottomView, likeScreenOffSet)
                }
            }
        }

        val passTransition = createPassTransition(
            scene,
            topView,
            lastSetId, lastSet,
            passSetId, passSet
        )

        val likeTransition = createLikeTransition(
            scene,
            topView,
            lastSetId, lastSet,
            likeSetId, likeSet
        )

        val passScreenOffTransition = createScreenOffTransition(
            scene,
            passSetId, passSet,
            passScreenOffSetId, passScreenOffSet
        )

        val likeScreenOffTransition = createScreenOffTransition(
            scene,
            likeSetId, likeSet,
            likeScreenOffSetId, likeScreenOffSet
        )

        transitions.add(passTransition)
        transitions.add(likeTransition)
        transitions.add(likeScreenOffTransition)
        transitions.add(passScreenOffTransition)

        addTransitionListener(object : TransitionAdapter() {
            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                when(currentId) {
                    likeScreenOffSetId,
                    passScreenOffSetId -> {
                        removeView(topView)
                    }
                }
            }
        })

        return LastSet(likeScreenOffSetId, likeScreenOffSet, passScreenOffSetId, passScreenOffSet)
    }

    private fun createScreenOffTransition(
        scene: MotionScene,
        startSetId: Int, startSet: ConstraintSet,
        endSetId: Int, endSet: ConstraintSet
    ): Transition = TransitionBuilder.buildTransition(
        scene,
        View.generateViewId(),
        startSetId, startSet,
        endSetId, endSet
    ).apply {
        duration = 150
        autoTransition = Transition.AUTO_ANIMATE_TO_END
    }

    private fun createPassTransition(
        scene: MotionScene,
        view: View,
        startSetId: Int, startSet: ConstraintSet,
        endSetId: Int, endSet: ConstraintSet
    ) : Transition = TransitionBuilder.buildTransition(
            scene,
            View.generateViewId(),
            startSetId, startSet,
            endSetId, endSet
        ).apply {
            duration = 300
            setOnSwipe(OnSwipe().apply {
                dragDirection = OnSwipe.DRAG_LEFT
                touchRegionId = view.id
            })

        }


    private fun createLikeTransition(
        scene: MotionScene,
        view: View,
        startSetId: Int, startSet: ConstraintSet,
        endSetId: Int, endSet: ConstraintSet
    ) : Transition = TransitionBuilder.buildTransition(
        scene,
        View.generateViewId(),
        startSetId, startSet,
        endSetId, endSet
    ).apply {
        duration = 300
        setOnSwipe(OnSwipe().apply {
            dragDirection = OnSwipe.DRAG_RIGHT
            touchRegionId = view.id
        })
        val keyFrames = KeyFrames()
        keyFrames.addKey(KeyPosition().apply {
            framePosition = 50
            setType(KeyPosition.TYPE_PATH)
            setValue(KeyPosition.PERCENT_X, 0.2f)
            setValue(KeyPosition.PERCENT_Y, -0.1f)
            setViewId(view.id)
        })
        addKeyFrame(keyFrames)
    }

    private fun connectTopCardRestSet(
        view: View,
        constraintSet: ConstraintSet
    ) {

        constraintSet.constrainHeight(view.id, ConstraintSet.MATCH_CONSTRAINT)

        constraintSet.connect(
            view.id,
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP,
            100
        )

        constraintSet.connect(
            view.id,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM,
            100
        )

        constraintSet.connect(
            view.id,
            ConstraintSet.START,
            ConstraintSet.PARENT_ID,
            ConstraintSet.START,
            100
        )

        constraintSet.connect(
            view.id,
            ConstraintSet.END,
            ConstraintSet.PARENT_ID,
            ConstraintSet.END,
            100
        )
    }

    private fun connectBottomCardRestSet(
        view: View,
        constraintSet: ConstraintSet
    ) {

        constraintSet.constrainHeight(view.id, ConstraintSet.MATCH_CONSTRAINT)

        constraintSet.connect(
            view.id,
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP,
            100
        )

        constraintSet.connect(
            view.id,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM,
            100
        )

        constraintSet.connect(
            view.id,
            ConstraintSet.START,
            ConstraintSet.PARENT_ID,
            ConstraintSet.START,
            100
        )

        constraintSet.connect(
            view.id,
            ConstraintSet.END,
            ConstraintSet.PARENT_ID,
            ConstraintSet.END,
            100
        )

        constraintSet.setScaleX(view.id, 0.9f)
        constraintSet.setScaleY(view.id, 0.9f)
    }

    private fun connectPassTopCardSet(
        view: View,
        constraintSet: ConstraintSet
    ) {
        constraintSet.constrainDefaultHeight(view.id, ConstraintSet.MATCH_CONSTRAINT)
        constraintSet.constrainDefaultWidth(view.id, ConstraintSet.MATCH_CONSTRAINT_WRAP)

        constraintSet.connect(
            view.id,
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP,
            20
        )

        constraintSet.connect(
            view.id,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM,
            180
        )

        constraintSet.connect(
            view.id,
            ConstraintSet.END,
            ConstraintSet.PARENT_ID,
            ConstraintSet.START,
            -250
        )
    }

    private fun connectPassBottomCardSet(
        view: View,
        constraintSet: ConstraintSet
    ) {
        constraintSet.constrainHeight(view.id, ConstraintSet.MATCH_CONSTRAINT)
        constraintSet.constrainWidth(view.id, ConstraintSet.MATCH_CONSTRAINT)

        constraintSet.connect(
            view.id,
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP,
            100
        )

        constraintSet.connect(
            view.id,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM,
            100
        )

        constraintSet.connect(
            view.id,
            ConstraintSet.START,
            ConstraintSet.PARENT_ID,
            ConstraintSet.START,
            100
        )

        constraintSet.connect(
            view.id,
            ConstraintSet.END,
            ConstraintSet.PARENT_ID,
            ConstraintSet.END,
            100
        )

        constraintSet.setScaleX(view.id, 1f)
        constraintSet.setScaleY(view.id, 1f)
    }

    private fun connectPassScreenOffTopCardSet(
        view: View,
        constraintSet: ConstraintSet
    ) {
        constraintSet.constrainWidth(view.id, ConstraintSet.MATCH_CONSTRAINT)
        constraintSet.constrainHeight(view.id, ConstraintSet.MATCH_CONSTRAINT)
        constraintSet.connect(
            view.id,
            ConstraintSet.END,
            ConstraintSet.PARENT_ID,
            ConstraintSet.START
        )
    }

    private fun connectScreenOffBottomCardSet(
        view: View,
        constraintSet: ConstraintSet
    ) {
        constraintSet.constrainWidth(view.id, ConstraintSet.MATCH_CONSTRAINT)
        constraintSet.constrainHeight(view.id, ConstraintSet.MATCH_CONSTRAINT)

        constraintSet.connect(
            view.id,
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP,
            100
        )

        constraintSet.connect(
            view.id,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM,
            100
        )

        constraintSet.connect(
            view.id,
            ConstraintSet.START,
            ConstraintSet.PARENT_ID,
            ConstraintSet.START,
            100
        )

        constraintSet.connect(
            view.id,
            ConstraintSet.END,
            ConstraintSet.PARENT_ID,
            ConstraintSet.END,
            100
        )
    }

    private fun connectLikeTopCard(
        view: View,
        constraintSet: ConstraintSet
    ) {
        constraintSet.constrainDefaultHeight(view.id, ConstraintSet.MATCH_CONSTRAINT)
        constraintSet.constrainDefaultWidth(view.id, ConstraintSet.MATCH_CONSTRAINT_WRAP)

        constraintSet.connect(
            view.id,
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP,
            20
        )

        constraintSet.connect(
            view.id,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM,
            180
        )

        constraintSet.connect(
            view.id,
            ConstraintSet.START,
            ConstraintSet.PARENT_ID,
            ConstraintSet.START,
            300
        )

        constraintSet.connect(
            view.id,
            ConstraintSet.END,
            ConstraintSet.PARENT_ID,
            ConstraintSet.END,
            -100
        )
    }

    private fun connectLikeScreenOffSet(
        view: View,
        constraintSet: ConstraintSet
    ) {
        constraintSet.constrainWidth(view.id, ConstraintSet.MATCH_CONSTRAINT)
        constraintSet.constrainHeight(view.id, ConstraintSet.MATCH_CONSTRAINT)
        constraintSet.connect(
            view.id,
            ConstraintSet.START,
            ConstraintSet.PARENT_ID,
            ConstraintSet.END
        )
    }

    data class LastSet(
        val likeScreenOffSetId: Int,
        val likeScreenOffSet: ConstraintSet,
        val passScreenOffSetId: Int,
        val passScreenOffSet: ConstraintSet
    )
}

class SwipeableStackViewAdapter(
    private val context: Context,
    private val layout: Int,
    private val models: List<SwipeCardModel>
) : BaseAdapter() {
    override fun getCount(): Int = models.size

    override fun getItem(position: Int): SwipeCardModel = models[position]

    override fun getItemId(position: Int): Long = position.toLong()

    @SuppressLint("ResourceType", "ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(layout, parent, false)
        view.findViewById<View>(R.id.card_view).setBackgroundColor(context.getColor(models[position].backgroundColor))
        return view
    }

}