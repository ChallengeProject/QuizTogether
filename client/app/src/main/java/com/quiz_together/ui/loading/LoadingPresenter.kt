package com.quiz_together.ui.base

import android.os.Handler
import com.quiz_together.data.Repository
import com.quiz_together.data.model.User
import com.quiz_together.data.remote.ApiHelper


class LoadingPresenter(
        private val repository: Repository,
        private val view: LoadingContract.View
) : LoadingContract.Presenter {

    init {
        view.presenter = this
    }

    override fun start() {
        view.setLoadingIndicator(true)

        Handler().postDelayed({
            checkLogin()
        }, 2000)

    }

    fun checkLogin() {

        view.setLoadingIndicator(true)

        repository.getUserId()?.let {
            repository.getUserProfile(it, object :ApiHelper.GetUserCallback {
                override fun onUserLoaded(user: User) {
                    view.run {
                        if(!isActive) return@onUserLoaded
                        setLoadingIndicator(false)

                        showMainUi()
                    }
                }
                override fun onDataNotAvailable() {
                    view.run {
                        if(!isActive) return@onDataNotAvailable
                        setLoadingIndicator(false)

                        showErrorTxt()
                    }
                }

            })
        }.let {
            view.run {
                if(!isActive) return@run
                setLoadingIndicator(false)

                showLoginUi()
            }
        }

    }



}