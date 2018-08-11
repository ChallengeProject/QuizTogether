package com.quiz_together.ui.main.home

import com.quiz_together.BasePresenter
import com.quiz_together.data.model.ResGetPagingBroadcastList


interface HomeContract {

    interface View {

        fun setLoadingIndicator(active: Boolean)

        val isActive: Boolean

        fun showBroadcasts(resGetPagingBroadcastList: ResGetPagingBroadcastList)

    }

    interface Presenter: BasePresenter {

        fun loadBroadcasts()

    }
}