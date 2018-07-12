package com.quiz_together.ui.write

import com.quiz_together.data.Repository

class WritePresenter(
        private val repository: Repository,
        private val view: WriteContract.View
) : WriteContract.Presenter {

    init {
        view.presenter = this
    }

    override fun start() {

    }



}