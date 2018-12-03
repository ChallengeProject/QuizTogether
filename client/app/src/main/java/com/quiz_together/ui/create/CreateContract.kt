package com.quiz_together.ui.create

import com.quiz_together.BasePresenter
import com.quiz_together.BaseView
import com.quiz_together.data.model.Broadcast


interface CreateContract {

    interface View : BaseView<Presenter> {
        var isActive: Boolean

        fun setLoadingIndicator(active: Boolean)
        fun extractBroadcast(): Broadcast
        fun loadSavedBroadcast(savedBroadcast: Broadcast?)
        fun onSuccessCreatedBroadcast(broadcastId: String)
        fun showToast(text: String)
    }

    interface Presenter : BasePresenter {

        fun loadBroadcastIfHasSavedBroadcast()
        fun createBroadcast(broadcast: Broadcast): Boolean
        fun updateBroadcast(broadcast: Broadcast)
        fun checkValidationForBroadcast(broadcast: Broadcast): String
        fun saveQuiz(broadcast: Broadcast)
    }
}
