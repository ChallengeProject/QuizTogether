package com.quiz_together.ui.create

import com.quiz_together.BasePresenter
import com.quiz_together.BaseView
import com.quiz_together.data.model.Broadcast


interface CreateContract {

    interface View : BaseView<Presenter> {
        var isActive: Boolean

        fun setLoadingIndicator(active: Boolean)
        fun loadQuiz(savedBroadcast: Broadcast?)
        fun isValidatedBroadcast(): Boolean
        fun extractBroadcast(): Broadcast
        fun onSuccessCreatedBroadcast(broadcastId: String)
        fun onErrorCreatedBroadcast()
    }

    interface Presenter : BasePresenter {
        fun loadQuizIfHasSavedQuiz()
        fun saveQuiz()
        fun requestCreateBroadcast()
    }


}
