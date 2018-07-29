package com.quiz_together.data.remote

import com.quiz_together.data.model.Broadcast
import com.quiz_together.data.model.BroadcastJoinInfo
import com.quiz_together.data.model.Events
import com.quiz_together.data.model.ReqEndBroadcast
import com.quiz_together.data.model.ReqSendAnswer
import com.quiz_together.data.model.ReqSendChatMsg
import com.quiz_together.data.model.ReqStartBroadcast
import com.quiz_together.data.model.User
import com.quiz_together.data.model.UserRes


interface ApiHelper {

    interface GetEventsCallback{
        fun onEventsLoaded(events : Events)
        fun onDataNotAvailable()
    }

    interface UserResCallback{
        fun onLoginLoaded(respLogin: UserRes)
        fun onDataNotAvailable()
    }

    interface GetSuccessCallback{
        fun onSuccessLoaded()
        fun onDataNotAvailable()
    }

    interface GetUserCallback{
        fun onUserLoaded(user: User)
        fun onDataNotAvailable()
    }

    interface GetBroadcastsCallback{
        fun onBroadcastsLoaded(broadcasts: List<Broadcast>)
        fun onDataNotAvailable()
    }

    interface GetBroadcastCallback{

        fun onBroadcastLoaded(broadcasts: Broadcast)
        fun onDataNotAvailable()
    }

    interface GetJoinBroadcastInfoCallback{

        fun onJoinBroadcastInfoLoaded(broadcastJoinInfo: BroadcastJoinInfo)
        fun onDataNotAvailable()
    }


    // dummy method
    fun getEvents(cb: GetEventsCallback)

    // user
    fun signup(name: String ,pushToken :String, cb: ApiHelper.UserResCallback)
    fun login(name:String , cb:UserResCallback)
    fun findUserByName(name: String , cb: ApiHelper.GetSuccessCallback)
    fun getUserProfile(userId: String , cb: ApiHelper.GetUserCallback)

    // broadcast
    fun createBroadcast(broadcast: Broadcast, cb: ApiHelper.GetSuccessCallback)
    fun getPagingBroadcastList(cb: ApiHelper.GetBroadcastsCallback)
    fun getBroadcastById(broadcastId:String ,cb: ApiHelper.GetBroadcastCallback)
    fun updateBroadcast(broadcast: Broadcast, cb: ApiHelper.GetSuccessCallback)
    fun sendAnswer(reqSendAnswer: ReqSendAnswer, cb: ApiHelper.GetSuccessCallback)
    fun endBroadcast(reqEndBroadcast: ReqEndBroadcast, cb: ApiHelper.GetSuccessCallback)
    fun startBroadcast(reqStartBroadcast: ReqStartBroadcast, cb: ApiHelper.GetSuccessCallback)
    fun getBroadcastForUpdateById(broadcastId:String ,cb: ApiHelper.GetBroadcastCallback)
    fun joinBroadcast(broadcastId:String, userId:String, cb: ApiHelper.GetJoinBroadcastInfoCallback)
    fun sendChatMsg(broadcastId:String, userId:String,msg:String, cb: ApiHelper.GetSuccessCallback)
    fun sendAdminChatMsg(broadcastId: String, userId: String, msg: String, cb: ApiHelper.GetSuccessCallback)


}