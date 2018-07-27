package com.quiz_together.ui.subscribe

import com.quiz_together.BasePresenter
import com.quiz_together.BaseView
import com.quiz_together.data.model.AdminMsg
import com.quiz_together.data.model.AnswerMsg
import com.quiz_together.data.model.ChatMsg
import com.quiz_together.data.model.EndMsg
import com.quiz_together.data.model.QuestionMsg
import com.quiz_together.data.model.WinnersMsg


interface SubscribeContract {

    interface View : BaseView<Presenter> {

        fun setLoadingIndicator(active: Boolean)
        var isActive: Boolean

        fun initQuizCalledByPresenter()

        fun showAdminMsg(adminMsg: AdminMsg)
        fun showChatMsg(chatMsg: ChatMsg)
        fun showQuestionView(questionMsg: QuestionMsg)
        fun showAnswerView(answerMsg: AnswerMsg)
        fun showWinnerView(winnersMsg: WinnersMsg)
        fun endQuiz(endMsg: EndMsg)

    }

    interface Presenter : BasePresenter {

        fun sendAnswer(step :Int,  answerNo: Int)


    }


}
