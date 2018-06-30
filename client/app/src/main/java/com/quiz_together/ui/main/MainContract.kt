package com.quiz_together.ui.main

import com.quiz_together.BasePresenter
import com.quiz_together.BaseView


interface MainContract {

    interface View : BaseView<Presenter> {

        fun setLoadingIndicator(active: Boolean)
        var isActive: Boolean

    }

    interface Presenter : BasePresenter {

    }


}
