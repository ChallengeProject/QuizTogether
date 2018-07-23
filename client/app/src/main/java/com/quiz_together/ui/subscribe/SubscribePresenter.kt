package com.quiz_together.ui.subscribe

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.quiz_together.data.Repository
import com.quiz_together.data.model.AdminMsg
import com.quiz_together.data.model.AnswerMsg
import com.quiz_together.data.model.BroadcastJoinInfo
import com.quiz_together.data.model.ChatMsg
import com.quiz_together.data.model.EndMsg
import com.quiz_together.data.model.PushType
import com.quiz_together.data.model.QuestionMsg
import com.quiz_together.data.model.WinnersMsg
import com.quiz_together.data.remote.ApiHelper
import com.quiz_together.util.SC


class SubscribePresenter(
        private val broadcastId:String,
        private val repository: Repository,
        private val view: SubscribeContract.View
) : SubscribeContract.Presenter {

    val TAG = "SubscribeContract#$#"

    init {
        view.presenter = this
    }

    override fun start() {

        regestFirbaseSubscribe()

        //TODO need to remove, now using test
        return

        repository.joinBroadcast(broadcastId, SC.USER_ID!!,object : ApiHelper.GetJoinBroadcastInfoCallback{
            override fun onJoinBroadcastInfoLoaded(broadcastJoinInfo: BroadcastJoinInfo) {
                regestFirbaseSubscribe()
            }

            override fun onDataNotAvailable() {

            }

        })
    }

    fun regestFirbaseSubscribe(){

//        FirebaseMessaging.getInstance().unsubscribeFromTopic(broadcastId)
        FirebaseMessaging.getInstance().subscribeToTopic(broadcastId).addOnSuccessListener {
            Log.i(TAG,"success regist topic >> ${broadcastId}") // empty same key
        }.addOnCompleteListener {
            Log.i(TAG,"complete regist topic >> ${broadcastId}") // duplicate
        }
    }

    fun onFcmListener(fcmMsg:String ) {

        val gsObj = SC.gson.fromJson(fcmMsg,JsonObject::class.java)

        when(gsObj.get("pushType").asString) {
            PushType.ADMIN_MESSAGE.name -> view.showAdminMsg(SC.gson.fromJson(fcmMsg, AdminMsg::class.java))
            PushType.ANSWER_MESSAGE.name -> view.showAnswerView(SC.gson.fromJson(fcmMsg, AnswerMsg::class.java))
            PushType.CHAT_MESSAGE.name -> view.showChatMsg(SC.gson.fromJson(fcmMsg, ChatMsg::class.java))
            PushType.END_MESSAGE.name -> view.endQuiz(SC.gson.fromJson(fcmMsg, EndMsg::class.java))
            PushType.QUESTION_MESSAGE.name -> view.showQuestionView(SC.gson.fromJson(fcmMsg, QuestionMsg::class.java))
            PushType.WINNERS_MESSAGE.name -> view.showWinnerView(SC.gson.fromJson(fcmMsg, WinnersMsg::class.java))
        }








    }

}