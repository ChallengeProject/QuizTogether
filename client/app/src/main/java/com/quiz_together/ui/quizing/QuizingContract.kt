package com.quiz_together.ui.quizing

import com.quiz_together.BasePresenter
import com.quiz_together.BaseView
import com.quiz_together.data.model.*


interface QuizingContract {

    interface View : BaseView<Presenter> {

        fun setLoadingIndicator(active: Boolean)
        var isActive: Boolean

        fun initQuizCalledByPresenter()

        fun setQuestionCnt(cnt: Int)

        fun showAdminMsg(adminMsg: AdminMsg)
        fun showChatMsg(chatMsg: ChatMsg)
        fun showQuestionView(questionMsg: QuestionMsg)
        fun showAnswerView(answerMsg: AnswerMsg)
        fun showWinnerView(winnersMsg: WinnersMsg)
        fun endQuiz(endMsg: EndMsg)
//        fun showBroadcastPlayInfo(brodcastInfoMsg:BrodcastInfoMsg)
        fun setMemberCount(cnt:Int)

        fun endQuizFromErr()
        fun finishActivity()
        fun setHeartCnt(cnt:Int)
        fun setUseHeartResult()
    }

    interface Presenter : BasePresenter {


        fun sendAnswer(step :Int,  answerNo: Int)

        fun sendMsg(msg:String)

        fun unsubscribeFirebase(isSendLeaveBroadcast :Boolean )

        fun updateBroadcastStatus(broadcastStatus: BroadcastStatus)

        fun openAnswer(step:Int)
        fun openQuestion(step:Int)
        fun openWinners()
        fun endBroadcast()

        fun saveCurBroadcastInfo()

        fun registFirbaseSubscribe()
        fun loadCurBroadcastInfo()

        fun existBroadcastInfo() : Boolean
        fun polling()

        fun useHeart(step:Int)

    }


}
