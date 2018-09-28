package com.quiz_together.ui.quizing

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.JsonObject
import com.quiz_together.data.Repository
import com.quiz_together.data.model.AdminMsg
import com.quiz_together.data.model.AnswerMsg
import com.quiz_together.data.model.BroadcastJoinInfo
import com.quiz_together.data.model.BroadcastStatus
import com.quiz_together.data.model.BrodcastInfoMsg
import com.quiz_together.data.model.ChatMsg
import com.quiz_together.data.model.CurBroadcastInfo
import com.quiz_together.data.model.EndMsg
import com.quiz_together.data.model.PushType
import com.quiz_together.data.model.QuestionMsg
import com.quiz_together.data.model.ReqEndBroadcast
import com.quiz_together.data.model.ReqSendAnswer
import com.quiz_together.data.model.ReqStartBroadcast
import com.quiz_together.data.model.ResStartBroadcast
import com.quiz_together.data.model.UserProfileView
import com.quiz_together.data.model.WinnersMsg
import com.quiz_together.data.remote.ApiHelper
import com.quiz_together.util.SC

class QuizingPresenter(
        private val broadcastId:String,
        private val isAdmin:Boolean,
        private val repository: Repository,
        private val view: QuizingContract.View
) : QuizingContract.Presenter {

    var curBroadcastInfo = CurBroadcastInfo(0,"",0,"")

    val TAG = "QuizingPresenter#$#"

    init {
        view.presenter = this
    }

    var isRemove = false // when endbroadcast, this value set true

    override fun start() {

        if(isAdmin && existBroadcastInfo()) {
            Log.i(TAG,"isAdmin && existBroadcastInfo()")
            loadCurBroadcastInfo()
            return
        }

        if (!isAdmin) {
//            Log.i(TAG,"repository.joinBroadcast broadcastId : ${broadcastId}  SC.USER_ID : ${SC.USER_ID}")

            repository.joinBroadcast(broadcastId, SC.USER_ID, object : ApiHelper.GetJoinBroadcastInfoCallback {
                override fun onJoinBroadcastInfoLoaded(broadcastJoinInfo: BroadcastJoinInfo) {
                    registFirbaseSubscribe()

                    view.setQuestionCnt(broadcastJoinInfo.broadcastView.questionCount)

                    view.setHeartCnt(broadcastJoinInfo.userProfileView.heartCount)

                    Log.i(TAG,"!@#$ playUserStatus : ${broadcastJoinInfo} !@#$")
                    Log.i(TAG,"!@#$ playUserStatus : ${broadcastJoinInfo.playUserStatus.name} !@#$")

                    managerJoinInfo(broadcastJoinInfo)
                }

                override fun onDataNotAvailable() {
                    Log.i(TAG, "joinBroadcast - onDataNotAvailable")
                    view.endQuizFromErr()
                }

            })
        } else {

            curBroadcastInfo.broadcastId = broadcastId

            view.setHeartCnt(0)

            repository.startBroadcast(ReqStartBroadcast(broadcastId, SC.USER_ID, "", ""), object : ApiHelper.GetBroadcastViewCallback {
                override fun onBroadcastViewLoaded(resStartBroadcast: ResStartBroadcast) {
                    curBroadcastInfo.lastQuestionNum = resStartBroadcast.broadcastView.questionCount
                    view.setQuestionCnt(resStartBroadcast.broadcastView.questionCount)

                    registFirbaseSubscribe()

                }


                override fun onDataNotAvailable() {
                    Log.i(TAG, "startBroadcast - onDataNotAvailable")
                    view.endQuizFromErr()
                }

            })

        }
    }


    fun managerJoinInfo(broadcastJoinInfo: BroadcastJoinInfo) {

//        curBroadcastInfo.broadcastId
//        curBroadcastInfo.lastQuestionNum
//        curBroadcastInfo.fcmMsg
//        curBroadcastInfo.viewerCount

//        broadcastId = broadcastJoinInfo.broadcastView.broadcastId
        view.setQuestionCnt(broadcastJoinInfo.broadcastView.questionCount)
        view.setMemberCount(broadcastJoinInfo.viewerCount)


        var pushType = PushType.QUESTION_MESSAGE // dummy init

        when(broadcastJoinInfo.broadcastView.broadcastStatus) {
            BroadcastStatus.OPEN_QUESTION -> {
                pushType = PushType.QUESTION_MESSAGE
                val questionMsg = QuestionMsg(pushType, broadcastJoinInfo.questionProp, broadcastJoinInfo.step)
                onFcmListener(SC.gson.toJson(questionMsg).toString())
            }
            BroadcastStatus.OPEN_ANSWER -> {


//                // TODO 엔서 받고 넘기기

//                pushType = PushType.ANSWER_MESSAGE
//                val answerMsg = AnswerMsg(pushType,broadcastJoinInfo.questionProp,broadcastJoinInfo.answerNo,broadcastJoinInfo.step,broadcastJoinInfo.questionProp)
            }
//            BroadcastStatus.WATING-> pushType = PushType.QUESTION_MESSAGE // 딱히 로직 없음
//            BroadcastStatus.CREATED -> pushType = PushType.QUESTION_MESSAGE // 딱히 로직 없음
        }

//        data class AnswerMsg(val pushType: PushType, val questionProp: QuestionProp, val answerNo:Int, val step:Int, val questionStatistics:Map<String,Int>)

    }


    override fun registFirbaseSubscribe(){

        FirebaseMessaging.getInstance().unsubscribeFromTopic(broadcastId)
        FirebaseMessaging.getInstance().subscribeToTopic(broadcastId).addOnSuccessListener {
            Log.i(TAG,"success regist topic >> ${broadcastId}") // empty same key
        }.addOnCompleteListener {
            Log.i(TAG,"complete regist topic >> ${broadcastId}") // duplicate
            view.initQuizCalledByPresenter()

        }
    }

    /**
     * firebase msgs
     * insert msg from fcm or local broadcastinfo's fcmMsg(admin) or joinInfo(none admin)
     */
    fun onFcmListener(fcmMsg:String ) {

//        Log.i(TAG,"## FCM_MSG ##")
//        Log.i(TAG,fcmMsg)

        // catch error that create broadcast and onPause and onResume
        if(fcmMsg.isNullOrEmpty()) return

        val gsObj = SC.gson.fromJson(fcmMsg,JsonObject::class.java)


        when(gsObj.get("pushType").asString) {
            PushType.ADMIN_MESSAGE.name -> view.showAdminMsg(SC.gson.fromJson(fcmMsg, AdminMsg::class.java))
            PushType.CHAT_MESSAGE.name -> view.showChatMsg(SC.gson.fromJson(fcmMsg, ChatMsg::class.java))
            PushType.ANSWER_MESSAGE.name -> {
                if(isAdmin) curBroadcastInfo.fcmMsg = fcmMsg
                view.showAnswerView(SC.gson.fromJson(fcmMsg, AnswerMsg::class.java))
            }
            PushType.END_MESSAGE.name -> {
                if(isAdmin) curBroadcastInfo.fcmMsg = fcmMsg
//                if(!isAdmin) // 0904 어드민을 왜 제외시켰었는지 했었는지 의문
                view.endQuiz(SC.gson.fromJson(fcmMsg, EndMsg::class.java))
            }
            PushType.QUESTION_MESSAGE.name -> {
                if(isAdmin) curBroadcastInfo.fcmMsg = fcmMsg
                view.showQuestionView(SC.gson.fromJson(fcmMsg, QuestionMsg::class.java))
            }
            PushType.WINNERS_MESSAGE.name -> {
                if(isAdmin) curBroadcastInfo.fcmMsg = fcmMsg
                view.showWinnerView(SC.gson.fromJson(fcmMsg, WinnersMsg::class.java))
            }
            PushType.BROADCAST_PLAY_INFO.name -> {
                val brodcastInfoMsg = SC.gson.fromJson(fcmMsg, BrodcastInfoMsg::class.java)

                view.setMemberCount(brodcastInfoMsg.viewerCount)

//                view.showBroadcastPlayInfo(brodcastInfoMsg)
            }
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

    override fun sendMsg(msg: String) {

        if(isAdmin)
            repository.sendAdminChatMsg( broadcastId,SC.USER_ID,msg,
                    object : ApiHelper.GetSuccessCallback {
                        override fun onSuccessLoaded() {
                            Log.i(TAG,"sendChatMsg - onSuccessLoaded()")
                        }

                        override fun onDataNotAvailable() {
                            Log.i(TAG,"sendChatMsg - onDataNotAvailable()")
                        }

                    })
        else
            repository.sendChatMsg( broadcastId,SC.USER_ID,msg,
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

        Log.i(TAG,"unsubscribeFirebase $broadcastId")
        FirebaseMessaging.getInstance().unsubscribeFromTopic(broadcastId)

        if(!isAdmin && isSendLeaveBroadcast)
        {
            //send leaveBroadcast
            repository.leaveBroadcast(broadcastId,SC.USER_ID,
                    object : ApiHelper.GetSuccessCallback{
                        override fun onSuccessLoaded() {
                            Log.i(TAG,"leaveBroadcast onSuccessLoaded")
                        }

                        override fun onDataNotAvailable() {
                            Log.i(TAG,"leaveBroadcast onDataNotAvailable")
                        }
                    })

        }
    }

    override fun updateBroadcastStatus(broadcastStatus: BroadcastStatus) {

        Log.i(TAG,"## updateBroadcastStatus ##")
        Log.i(TAG,"SC.USER_ID : " + SC.USER_ID + "   broadcastId : " + broadcastId + "   broadcastStatus : " + broadcastStatus)

        repository.updateBroadcastStatus(broadcastId,SC.USER_ID,broadcastStatus,
                object : ApiHelper.GetSuccessCallback{
                    override fun onSuccessLoaded() {
                        Log.i(TAG,"updateBroadcastStatus onSuccessLoaded")
                    }

                    override fun onDataNotAvailable() {
                        Log.i(TAG,"updateBroadcastStatus onDataNotAvailable")
                    }
                })

    }

    override fun openAnswer(step: Int) {
        repository.openAnswer(broadcastId,SC.USER_ID,step,
                object : ApiHelper.GetSuccessCallback {
                    override fun onSuccessLoaded() {
                        Log.i(TAG,"openAnswer onSuccessLoaded")
                    }

                    override fun onDataNotAvailable() {
                        Log.i(TAG,"openAnswer onDataNotAvailable")
                    }
                })
    }

    override fun openQuestion(step: Int) {
        repository.openQuestion(broadcastId,SC.USER_ID,step,
                object : ApiHelper.GetSuccessCallback {
                    override fun onSuccessLoaded() {
                        Log.i(TAG,"openQuestion onSuccessLoaded")
                    }

                    override fun onDataNotAvailable() {
                        Log.i(TAG,"openQuestion onDataNotAvailable")
                    }
                })
    }

    override fun openWinners() {
        repository.openWinners(broadcastId,SC.USER_ID,
                object : ApiHelper.GetSuccessCallback {
                    override fun onSuccessLoaded() {
                        Log.i(TAG,"openWinners onSuccessLoaded")
                    }

                    override fun onDataNotAvailable() {
                        Log.i(TAG,"openWinners onDataNotAvailable")
                    }
                })
    }

    override fun endBroadcast() {

        repository.endBroadcast(ReqEndBroadcast(broadcastId,SC.USER_ID,"dummy","dummy"),
                object : ApiHelper.GetSuccessCallback {
                    override fun onSuccessLoaded() {
                        Log.i(TAG,"endBroadcast onSuccessLoaded")
                        isRemove = true
                        if(isAdmin) repository.removeCurBroadcstInfo()

                        view.finishActivity()
                    }

                    override fun onDataNotAvailable() {
                        Log.i(TAG,"endBroadcast onDataNotAvailable")
                    }
                })
    }

    override fun saveCurBroadcastInfo(){

        if(!isAdmin || isRemove) return

        val json = SC.gson.toJson(curBroadcastInfo)
        repository.setCurBroadcstInfo(json)

        Log.i(TAG,"saveCurBroadcastInfo $json")

    }

    override fun existBroadcastInfo() : Boolean {

        val rst = !repository.getCurBroadcstInfo().isNullOrEmpty()
        Log.i(TAG,"existBroadcastInfo $rst")
        return rst
//
//        val tmpVal = repository.getCurBroadcstInfo()
//
//        val retVal = tmpVal.isNullOrEmpty()
//
////        Log.i(TAG,"existBroadcastInforetVal : $retVal")
//
//        if(!retVal) repository.removeCurBroadcstInfo()
//        return retVal

    }

    override fun loadCurBroadcastInfo(){

        if(!isAdmin) return

        registFirbaseSubscribe()
        curBroadcastInfo = SC.gson.fromJson(repository.getCurBroadcstInfo(), CurBroadcastInfo::class.java)

        Log.i(TAG,"loadCurBroadcastInfo $repository.getCurBroadcstInfo()")


        onFcmListener(curBroadcastInfo.fcmMsg)
        view.setQuestionCnt( curBroadcastInfo.lastQuestionNum)
        view.setMemberCount(curBroadcastInfo.viewerCount)
    }

    override fun polling(){

        repository.sendBroadcastPlayInfo(broadcastId,SC.USER_ID,
                object:ApiHelper.GetSuccessCallback{
                    override fun onSuccessLoaded() {
                        Log.i(TAG,"sendBroadcastPlayInfo onSuccessLoaded")
                   }

                    override fun onDataNotAvailable() {
                        Log.e(TAG,"sendBroadcastPlayInfo onDataNotAvailable")
                    }
                })

    }

    override fun useHeart(step:Int) {

        view.setUseHeartResult() // 임시 로컬로
        //TODO 서버에서 받아와서 진행해야함
//        Log.i(TAG,"$step, ${SC.USER_ID}, $broadcastId")
//        repository.sendHeart(step,SC.USER_ID,broadcastId,object  :ApiHelper.GetSuccessCallback{
//            override fun onSuccessLoaded() {
//
//                Log.i(TAG,"sendHeart onSuccessLoaded")
//                view.setUseHeartResult()
//            }
//
//            override fun onDataNotAvailable() {
//                Log.e(TAG,"sendHeart onDataNotAvailable")
//            }
//
//
//        })


    }




}