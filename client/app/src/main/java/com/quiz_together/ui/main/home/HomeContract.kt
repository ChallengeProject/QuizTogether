package com.quiz_together.ui.main.home

import com.quiz_together.BasePresenter
import com.quiz_together.BaseView
import com.quiz_together.data.model.Broadcast


interface HomeContract {

    interface View {

        fun setLoadingIndicator(active: Boolean)

        val isActive: Boolean

        fun showBroadcasts(broadcasts: List<Broadcast>)

    }

    interface Presenter: BasePresenter {

        fun loadBroadcasts()

    }
}