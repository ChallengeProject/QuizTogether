package com.quiz_together.util

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.quiz_together.App
import java.text.SimpleDateFormat
import java.util.*
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v4.graphics.drawable.RoundedBitmapDrawable
import android.opengl.ETC1.getWidth
import android.R.attr.bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.opengl.ETC1.getHeight



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

fun Long.getDateTime() :String =SimpleDateFormat("MM/dd/yyyy HH:mm").format(Date(this))

