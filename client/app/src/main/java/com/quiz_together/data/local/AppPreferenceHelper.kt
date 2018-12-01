package com.quiz_together.data.local

import android.content.Context
import com.google.gson.Gson
import com.quiz_together.App
import com.quiz_together.BuildConfig
import com.quiz_together.data.model.Broadcast


class AppPreferenceHelper : PreferenceHelper {

    private val PFRK_IS_FIRST_LAUNCH = "PFRK_IS_FIRST_LAUNCH"
    private val PFRK_USER_ID = "PFRK_USER_ID"
    private val BROADCAST_IN_WRITTING = "BROADCAST_IN_WRITTING"
    private val HAS_SAVED_QUIZ = "HAS_SAVED_QUIZ"

    private val PFRK_FOLLOW_LIST = "PFRK_FOLLOW_LIST"


    val mPrefs = App.instance.getSharedPreferences(BuildConfig.PREF_FILE_NAME, Context.MODE_PRIVATE)

    override fun isFirstLaunch(): Boolean {
        return mPrefs.getBoolean(PFRK_IS_FIRST_LAUNCH, true)
    }

    override fun setIsFirst(isFirst: Boolean) {
        mPrefs.edit().putBoolean(PFRK_IS_FIRST_LAUNCH, isFirst).apply()
    }

    override fun setUserId(userId: String) {
        mPrefs.edit().putString(PFRK_USER_ID, userId).apply()
    }

    override fun getUserId(): String? = mPrefs.getString(PFRK_USER_ID, null)

    override fun hasSavedBroadcast(): Boolean {
        return mPrefs.getBoolean(HAS_SAVED_QUIZ, false)
    }

    override fun saveBroadcast(broadcast: Broadcast) {
        val json = Gson().toJson(broadcast)
        mPrefs.edit().putString(BROADCAST_IN_WRITTING, json).apply()
        mPrefs.edit().putBoolean(HAS_SAVED_QUIZ, true).apply()
    }

    override fun getSavedBroadcast(): Broadcast {
        val json = mPrefs.getString(BROADCAST_IN_WRITTING, null)
        mPrefs.edit().putBoolean(HAS_SAVED_QUIZ, false).apply()
        return Gson().fromJson<Broadcast>(json, Broadcast::class.java)
    }

    override fun getSavedFollowerList(): List<String> = mPrefs.getStringSet(PFRK_FOLLOW_LIST, setOf<String>()).toList()

    override fun setFollowerList(setStr: Set<String>) {
        mPrefs.edit().putStringSet(PFRK_FOLLOW_LIST, setStr).apply()
    }

}



