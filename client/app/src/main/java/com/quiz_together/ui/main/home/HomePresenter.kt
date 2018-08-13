package com.quiz_together.ui.main.home

import android.util.Log
import android.widget.ProgressBar
import com.google.firebase.messaging.FirebaseMessaging
import com.quiz_together.data.Repository
import com.quiz_together.data.model.ResGetPagingBroadcastList
import com.quiz_together.data.remote.ApiHelper
import com.quiz_together.util.SC

class HomePresenter  (val view: HomeFragment,val pb: ProgressBar,val repository:Repository) : HomeContract.Presenter {

    private val TAG = "HomePresenter#$#"


    override fun start() {
        loadBroadcasts()
    }

    override fun loadBroadcasts() {
        repository.getPagingBroadcastList(SC.USER_ID, object : ApiHelper.GetPagingBroadcastList {
            override fun onPagingBroadcastListLoaded(resGetPagingBroadcastList: ResGetPagingBroadcastList) {
                view.run{
                    if (!isActive) return@run

                    Log.i(TAG, resGetPagingBroadcastList.toString())

                    setLoadingIndicator(false)

                    showBroadcasts(resGetPagingBroadcastList)
                }
            }

            override fun onDataNotAvailable() {
                view.run {
                    if (!isActive) return@onDataNotAvailable

                    Log.i(TAG,"onDataNotAvailable")
                    setLoadingIndicator(false)

                }
            }

        })


    }

}