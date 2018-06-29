package com.quiz_together.data

import com.quiz_together.data.local.AppPreferenceHelper
import com.quiz_together.data.local.PreferenceHelper
import com.quiz_together.data.remote.ApiHelper
import com.quiz_together.data.remote.AppApiHelper


object Repository : PreferenceHelper, ApiHelper {
    private val preferenceHelper = AppPreferenceHelper()
    private val apiHelper = AppApiHelper()

    override fun login(id: String, pw: String, cb: ApiHelper.LoginCallback) {
        apiHelper.login(id,pw,cb)
    }

    override fun getEvents(cb: ApiHelper.GetEventsCallback) {
        apiHelper.getEvents(cb)
    }


    override fun isFirstLaunch(): Boolean {
        return preferenceHelper.isFirstLaunch()
    }

    override fun setIsFirst(isFirst: Boolean) {
        preferenceHelper.setIsFirst(isFirst)
    }



}
