package com.quiz_together.data

import com.quiz_together.data.local.AppPreferenceHelper
import com.quiz_together.data.local.PreferenceHelper
import com.quiz_together.data.model.Broadcast
import com.quiz_together.data.model.ReqEndBroadcast
import com.quiz_together.data.model.ReqSendAnswer
import com.quiz_together.data.model.ReqStartBroadcast
import com.quiz_together.data.remote.ApiHelper
import com.quiz_together.data.remote.AppApiHelper


object Repository : PreferenceHelper, ApiHelper {


    private val preferenceHelper = AppPreferenceHelper()
    private val apiHelper = AppApiHelper()

    override fun login(name: String, cb: ApiHelper.LoginCallback) {
        apiHelper.login(name,cb)
    }

    override fun findUserByName(name: String, cb: ApiHelper.GetSuccessCallback) {
        apiHelper.findUserByName(name,cb)
    }

    override fun getUserProfile(userId: String, cb: ApiHelper.GetUserCallback) {
        apiHelper.getUserProfile(userId,cb)
    }

    override fun createBroadcast(broadcast: Broadcast, cb: ApiHelper.GetSuccessCallback) {
        apiHelper.createBroadcast(broadcast,cb)
    }

    override fun getBroadcastList(cb: ApiHelper.GetBroadcastsCallback) {
        apiHelper.getBroadcastList(cb)
    }

    override fun getBroadcastById(broadcastId: String, cb: ApiHelper.GetBroadcastCallback) {
        apiHelper.getBroadcastById(broadcastId,cb)
    }

    override fun updateBroadcast(broadcast: Broadcast, cb: ApiHelper.GetSuccessCallback) {
        apiHelper.updateBroadcast(broadcast,cb)
    }

    override fun sendAnswer(reqSendAnswer: ReqSendAnswer, cb: ApiHelper.GetSuccessCallback) {
        apiHelper.sendAnswer(reqSendAnswer,cb)
    }

    override fun endBroadcast(reqEndBroadcast: ReqEndBroadcast, cb: ApiHelper.GetSuccessCallback) {
        apiHelper.endBroadcast(reqEndBroadcast,cb)
    }

    override fun startBroadcast(reqStartBroadcast: ReqStartBroadcast, cb: ApiHelper.GetSuccessCallback) {
        apiHelper.startBroadcast(reqStartBroadcast,cb)
    }

    override fun getBroadcastForUpdateById(broadcastId: String, cb: ApiHelper.GetBroadcastCallback) {
        apiHelper.getBroadcastForUpdateById(broadcastId,cb)
    }

    override fun getEvents(cb: ApiHelper.GetEventsCallback) {
        apiHelper.getEvents(cb)
    }


    // shared prefrenece

    override fun isFirstLaunch(): Boolean {
        return preferenceHelper.isFirstLaunch()
    }

    override fun setIsFirst(isFirst: Boolean) {
        preferenceHelper.setIsFirst(isFirst)
    }

    override fun setUserId(userId: String) {
        preferenceHelper.setUserId(userId)
    }

    override fun getUserId(): String? = preferenceHelper.getUserId()



}
