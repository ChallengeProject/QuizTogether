package com.quiz_together.ui.subscribe

import com.quiz_together.data.Repository


class SubscribePresenter(
        private val broadcastId:String,
        private val repository: Repository,
        private val view: SubscribeContract.View
) : SubscribeContract.Presenter {

    init {
        view.presenter = this
    }

    override fun start() {
        var I_CAN_RECEIVE_ID = broadcastId

        // _TODO firebase scribe

    }



}