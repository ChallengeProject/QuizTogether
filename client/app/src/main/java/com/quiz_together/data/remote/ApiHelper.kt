package com.quiz_together.data.remote

import com.quiz_together.data.model.*

interface ApiHelper {

    interface GetEventsCallback{
        fun onEventsLoaded(events : Events)
        fun onDataNotAvailable()
    }

    interface LoginCallback{
        fun onLoginLoaded(respLogin: RespLogin)
        fun onDataNotAvailable()
    }

    interface GetSuccessCallback{
        fun onSuccessLoaded()
        fun onDataNotAvailable()
    }

    interface GetUserCallback{
        fun onUserLoaded(user:User)
        fun onDataNotAvailable()
        fun onUserDataConflict()
    }

    interface GetBroadcastsCallback{
        fun onBroadcastsLoaded(broadcasts: List<Broadcast>)
        fun onDataNotAvailable()
    }

    interface GetBroadcastCallback{
        fun onBroadcastLoaded(broadcasts: Broadcast)
        fun onDataNotAvailable()
    }


    // dummy method
    fun getEvents(cb: GetEventsCallback)

    // user
    fun login(name:String , cb:LoginCallback)
    fun findUserByName(name: String , cb: ApiHelper.GetSuccessCallback)
    fun getUserProfile(userId: String , cb: ApiHelper.GetUserCallback)

    // broadcast
    fun createBroadcast(broadcast: Broadcast, cb: ApiHelper.GetSuccessCallback)
    fun getBroadcastList(cb: ApiHelper.GetBroadcastsCallback)
    fun getBroadcastById(broadcastId:String ,cb: ApiHelper.GetBroadcastCallback)
    fun updateBroadcast(broadcast: Broadcast, cb: ApiHelper.GetBroadcastCallback)
    fun sendAnswer(reqSendAnswer: ReqSendAnswer, cb: ApiHelper.GetSuccessCallback)
    fun endBroadcast(reqEndBroadcast: ReqEndBroadcast, cb: ApiHelper.GetSuccessCallback)
    fun startBroadcast(reqStartBroadcast: ReqStartBroadcast, cb: ApiHelper.GetSuccessCallback)
    fun getBroadcastForUpdateById(broadcastId:String ,cb: ApiHelper.GetBroadcastCallback)




}