package com.quiz_together.data.local

import com.quiz_together.data.model.Broadcast

interface PreferenceHelper {
    fun isFirstLaunch(): Boolean
    fun setIsFirst(isFirst: Boolean)

    fun setUserId(userId: String)
    fun getUserId(): String?

    fun hasSavedBroadcast(): Boolean
    fun saveBroadcast(broadcast: Broadcast)
    fun getSavedBroadcast(): Broadcast?

    fun getSavedFollowerList(): List<String>
    fun setFollowerList(setStr: Set<String>)
}