package com.quiz_together.data.local

import android.content.Context
import com.quiz_together.App
import com.quiz_together.BuildConfig


class AppPreferenceHelper : PreferenceHelper {

    private val PFRK_IS_FIRST_LAUNCH = "PFRK_IS_FIRST_LAUNCH"

    val mPrefs = App.instance.getSharedPreferences(BuildConfig.PREF_FILE_NAME, Context.MODE_PRIVATE)

    override fun isFirstLaunch() : Boolean {
        return mPrefs.getBoolean(PFRK_IS_FIRST_LAUNCH,true)
    }

    override fun setIsFirst(isFirst: Boolean) {
        mPrefs.edit().putBoolean(PFRK_IS_FIRST_LAUNCH,isFirst).commit()

    }


}


