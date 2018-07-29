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
import com.quiz_together.data.model.ReqSendAnswer
import com.quiz_together.data.model.ReqSendChatMsg
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

        registFirbaseSubscribe()

        //TODO need to remove, now using test
        return

        repository.joinBroadcast(broadcastId, SC.USER_ID,object : ApiHelper.GetJoinBroadcastInfoCallback{
            override fun onJoinBroadcastInfoLoaded(broadcastJoinInfo: BroadcastJoinInfo) {
                registFirbaseSubscribe()
            }

            override fun onDataNotAvailable() {

            }

        })
    }

    fun registFirbaseSubscribe(){

        FirebaseMessaging.getInstance().unsubscribeFromTopic(broadcastId)
        FirebaseMessaging.getInstance().subscribeToTopic(broadcastId).addOnSuccessListener {
            Log.i(TAG,"success regist topic >> ${broadcastId}") // empty same key
        }.addOnCompleteListener {
            Log.i(TAG,"complete regist topic >> ${broadcastId}") // duplicate
            view.initQuizCalledByPresenter()
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

    override fun sendAnswer(step: Int, answerNo: Int) {
        repository.sendAnswer(ReqSendAnswer(step,SC.USER_ID,broadcastId,answerNo),
                object : ApiHelper.GetSuccessCallback{
            override fun onSuccessLoaded() {
                Log.i(TAG,"sendAnswer - onSuccessLoaded()")
            }

            override fun onDataNotAvailable() {
                Log.i(TAG,"sendAnswer - onDataNotAvailable()")
            }

        })

    }

    override fun sendUserMsg(msg: String) {

        repository.sendChatMsg( "b82d4e3b1873ef25f7264af5a2113f5a7",SC.USER_ID,msg,
                object : ApiHelper.GetSuccessCallback {
                    override fun onSuccessLoaded() {
                        Log.i(TAG,"sendChatMsg - onSuccessLoaded()")
                    }

                    override fun onDataNotAvailable() {
                        Log.i(TAG,"sendChatMsg - onDataNotAvailable()")
                    }

                })

    }

    override fun unsubscribeFirebase(isSendLeaveBroadcast :Boolean ) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(broadcastId)

        if(isSendLeaveBroadcast)
        {
            //send leaveBroadcast

        }
    }

}