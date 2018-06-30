package com.quiz_together.ui.base

import android.os.Handler
import com.quiz_together.data.Repository


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

            view.setLoadingIndicator(false)
            view.showLogin()

        }, 2000)

    }



}