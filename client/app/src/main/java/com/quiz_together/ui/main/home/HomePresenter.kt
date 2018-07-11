package com.quiz_together.ui.main.home

import android.widget.ProgressBar
import com.quiz_together.data.Repository
import com.quiz_together.data.model.Broadcast
import com.quiz_together.data.model.Events
import com.quiz_together.data.remote.ApiHelper

class HomePresenter  (val view: HomeFragment,val pb: ProgressBar,val repository:Repository) : HomeContract.Presenter {
    override fun start() {

        loadBroadcasts()
    }

    override fun loadBroadcasts() {

        repository.getBroadcastList(object : ApiHelper.GetBroadcastsCallback{
            override fun onBroadcastsLoaded(broadcasts: List<Broadcast>) {
                view.run{
                    if(!isActive) return@onBroadcastsLoaded

                    setLoadingIndicator(false)

                    showBroadcasts(broadcasts)
                }
            }


            override fun onDataNotAvailable() {
                view.run {
                    if (!isActive) return@onDataNotAvailable

                    setLoadingIndicator(false)
                }
            }

        })


    }

}