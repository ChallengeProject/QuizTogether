package com.quiz_together.ui.profile

import com.quiz_together.data.Repository
import com.quiz_together.data.model.User
import com.quiz_together.data.remote.ApiHelper
import com.quiz_together.util.SC


class ProfilePresenter(
        private val repository: Repository,
        private val view: ProfileContract.View
) : ProfileContract.Presenter {

    init {
        view.presenter = this
    }

    override fun start() {
        getProfileTask()
    }

    fun getProfileTask() {

        view.setLoadingIndicator(true)
        SC.USER_ID?.run {
            repository.getUserProfile(this,object:ApiHelper.GetUserCallback {
                override fun onUserLoaded(user: User) {

                    view.run{
                        if(!isActive) return@onUserLoaded
                        setLoadingIndicator(false)
                        updateUserView(user)
                    }
                }

                override fun onDataNotAvailable() {
                    view.run{
                        if(!isActive) return@onDataNotAvailable
                        setLoadingIndicator(false)
                        showFailGetProfileTxt()
                    }
                }

            })
        }?:let {
            throw Exception("SC.USER_ID is null")
        }

    }


}