package com.quiz_together.ui.main

import com.quiz_together.BasePresenter
import com.quiz_together.BaseView
import com.quiz_together.data.model.Broadcast


interface MainContract {

    interface View : BaseView<Presenter> {

        fun setLoadingIndicator(active: Boolean)
        var isActive: Boolean

        fun showBroadcasts(broadcasts: List<Broadcast>)
        fun showErrorMsg()

    }

    interface Presenter : BasePresenter {
        fun loadBroadcastTask()
    }


}
