package com.quiz_together.data.local

interface PreferenceHelper {

    fun isFirstLaunch() : Boolean
    fun setIsFirst(isFirst:Boolean)

    fun setUserId(userId:String)
    fun getUserId() : String?
}