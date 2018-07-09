package com.quiz_together.ui.profile

import com.quiz_together.BasePresenter
import com.quiz_together.BaseView
import com.quiz_together.data.model.User

interface ProfileContract {

    interface View : BaseView<Presenter> {

        fun setLoadingIndicator(active: Boolean)
        var isActive: Boolean

        fun updateUserView(user: User)
        fun showFailGetProfileTxt()

    }

    interface Presenter : BasePresenter {

    }


}
