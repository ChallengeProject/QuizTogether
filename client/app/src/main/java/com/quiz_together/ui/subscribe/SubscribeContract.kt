package com.quiz_together.ui.subscribe

import com.quiz_together.BasePresenter
import com.quiz_together.BaseView


interface SubscribeContract {

    interface View : BaseView<Presenter> {

        fun setLoadingIndicator(active: Boolean)
        var isActive: Boolean

    }

    interface Presenter : BasePresenter {

    }


}
