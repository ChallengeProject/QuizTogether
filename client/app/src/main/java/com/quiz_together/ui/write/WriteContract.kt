package com.quiz_together.ui.write

import com.quiz_together.BasePresenter
import com.quiz_together.BaseView


interface WriteContract {

    interface View : BaseView<Presenter> {

        fun setLoadingIndicator(active: Boolean)
        var isActive: Boolean

    }

    interface Presenter : BasePresenter {

    }


}
