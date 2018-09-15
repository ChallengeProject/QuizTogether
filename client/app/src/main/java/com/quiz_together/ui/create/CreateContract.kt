package com.quiz_together.ui.create

import com.quiz_together.BasePresenter
import com.quiz_together.BaseView
import com.quiz_together.data.model.Broadcast
import com.quiz_together.data.model.Question


interface CreateContract {

    interface View : BaseView<Presenter> {
        var isActive: Boolean

        fun setLoadingIndicator(active: Boolean)
        fun loadQuiz(savedBroadcast: Broadcast?)
        fun extractBroadcast(): Broadcast
        fun onSuccessCreatedBroadcast(broadcastId: String)
        fun onErrorCreatedBroadcast()
        fun showToast(text: String)
    }

    interface Presenter : BasePresenter {
        fun loadQuizIfHasSavedQuiz()
        fun saveQuiz()
        fun createBroadcast()
        fun updateQuestion(position: Int, item: Question)
    }


}
