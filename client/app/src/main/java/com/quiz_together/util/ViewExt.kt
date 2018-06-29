package com.quiz_together.util

import android.content.Context
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import com.quiz_together.App

fun String.toast( duration: Int = Toast.LENGTH_LONG): Toast {
    return Toast.makeText(App.instance, this, duration).apply { show() }
}

fun ProgressBar.setVisibilityFromBoolean(active:Boolean) {
    this.visibility = if(active) View.VISIBLE else View.INVISIBLE
}

fun Window.setTouchable(active:Boolean) {
    if(active)
        this.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    else
        this.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
}
