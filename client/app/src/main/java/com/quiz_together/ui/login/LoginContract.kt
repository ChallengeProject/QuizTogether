package com.quiz_together.ui.login

import com.quiz_together.BasePresenter
import com.quiz_together.BaseView

interface LoginContract {

    interface View : BaseView<Presenter> {

        fun setLoadingIndicator(active: Boolean)

        var isActive: Boolean

        fun showMainUi(id:String)

        fun showFirstLaunch()
    }

    interface Presenter : BasePresenter {
        fun loginTask(id: String, pw: String)


    }


}
