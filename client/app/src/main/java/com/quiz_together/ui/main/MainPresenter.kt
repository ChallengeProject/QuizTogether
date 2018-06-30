package com.quiz_together.ui.main

import com.quiz_together.data.Repository


class MainPresenter(
        private val repository: Repository,
        private val view: MainContract.View
) : MainContract.Presenter {

    init {
        view.presenter = this
    }

    override fun start() {

    }



}