package com.quiz_together.ui.write

import android.util.Log
import android.view.MotionEvent
import android.view.View

class QuizNumberSwipeListener(val cb:(Boolean)->Any) : View.OnTouchListener {

    val TAG = "QuizNumberSwi#$#"

    val MIN_DISTANCE = 300

    var downX : Float = 0.toFloat()
    var upX: Float = 0.toFloat()

    fun onLeftToRightSwipe(){
        Log.i(TAG,"onLeftToRightSwipe")
        cb(true)
    }

    fun onRightToLeftSwipe(){
        Log.i(TAG,"onRightToLeftSwipe")
        cb(false)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.getX()
                return true
            }
            MotionEvent.ACTION_UP -> {
                upX = event.getX()

                val deltaX = downX - upX

                if (Math.abs(deltaX) > MIN_DISTANCE) {
                    // left or right
                    if (deltaX < 0) {
                        this.onLeftToRightSwipe()
                        return true
                    }
                    if (deltaX > 0) {
                        this.onRightToLeftSwipe()
                        return true
                    }
                } else {
                    Log.i(TAG, "Swipe was only " + Math.abs(deltaX) + " long horizontally, need at least " + MIN_DISTANCE)
                    // return false; // We don't consume the event
                }


                return false // no swipe horizontally and no swipe vertically
            }// case MotionEvent.ACTION_UP:
        }

        Log.i(TAG, event?.x.toString())

        return false
    }
}