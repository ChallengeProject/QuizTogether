package com.quiz_together.ui.main

import com.quiz_together.data.Repository
import com.quiz_together.data.model.Broadcast
import com.quiz_together.data.model.Broadcasts
import com.quiz_together.data.remote.ApiHelper


class MainPresenter(
        private val repository: Repository,
        private val view: MainContract.View
) : MainContract.Presenter {

    val TAG = "MainPresenter"

    init {
        view.presenter = this
    }

    override fun start() {
        loadBroadcastTask()
    }

    override fun loadBroadcastTask() {

        view.setLoadingIndicator(true)

        repository.getBroadcastList(object : ApiHelper.GetBroadcastsCallback {
            override fun onBroadcastsLoaded(broadcasts: List<Broadcast>) {

                view.run {
                    if(!isActive) return@onBroadcastsLoaded
                    setLoadingIndicator(false)

                    showBroadcasts(broadcasts)
                }


            }

//            override fun onEventsLoaded(events: Events) {
//                eventView.run{
//                    if(!isActive) return@onEventsLoaded
//
//                    setLoadingIndicator(false)
//
//                    showEvents(events.events)
//                }
//            }

            override fun onDataNotAvailable() {

                view.run {
                    if (!isActive) return@onDataNotAvailable
                    setLoadingIndicator(false)

                    showErrorMsg()
                }
            }

        })





    }



}