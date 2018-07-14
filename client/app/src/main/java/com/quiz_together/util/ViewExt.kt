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
import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.opengl.ETC1.getHeight
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.util.Log

val TAG = "ViewExt##"

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

@SuppressLint("RestrictedApi")
fun BottomNavigationView.disableShiftMode() {
    val menuView = getChildAt(0) as BottomNavigationMenuView
    try {
        val shiftingMode = menuView::class.java.getDeclaredField("mShiftingMode")
        shiftingMode.isAccessible = true
        shiftingMode.setBoolean(menuView, false)
        shiftingMode.isAccessible = false
        for (i in 0 until menuView.childCount) {
            val item = menuView.getChildAt(i) as BottomNavigationItemView
            item.setShiftingMode(false)
            // set once again checked value, so view will be updated
            item.setChecked(item.itemData.isChecked)
        }
    } catch (e: NoSuchFieldException) {
        Log.e(TAG, "Unable to get shift mode field", e)
    } catch (e: IllegalStateException) {
        Log.e(TAG, "Unable to change value of shift mode", e)
    }
}