package com.quiz_together.ui.main.home

import android.util.Log
import android.widget.ProgressBar
import com.google.firebase.messaging.FirebaseMessaging
import com.quiz_together.data.Repository
import com.quiz_together.data.model.Follower
import com.quiz_together.data.model.ResFollowList
import com.quiz_together.data.model.ResGetPagingBroadcastList
import com.quiz_together.data.remote.ApiHelper
import com.quiz_together.util.SC

class HomePresenter  (val view: HomeFragment,val pb: ProgressBar,val repository:Repository) : HomeContract.Presenter {

    private val TAG = "HomePresenter#$#"


    override fun start() {
        loadBroadcasts()
    }



    override fun loadBroadcasts() {
        repository.getFollowerList(SC.USER_ID,object :ApiHelper.GetFollowerListCallback{
            override fun onFollowerList(followList: ResFollowList) {

                Log.i(TAG,followList.toString())

                tmpLoadBroadcast(followList.userFollowerList)
            }

            override fun onDataNotAvailable() {
                Log.i(TAG,"getFollowerList onDataNotAvailable")
            }
        })
    }

    //TODO 추후 최초한번만 호출하고 추후에는 로컬로 비교할 수 있게 바꿔야함
    fun tmpLoadBroadcast(followList: List<Follower>) {
        repository.getPagingBroadcastList(SC.USER_ID, object : ApiHelper.GetPagingBroadcastList {
            override fun onPagingBroadcastListLoaded(resGetPagingBroadcastList: ResGetPagingBroadcastList) {
                view.run{
                    if (!isActive) return@run

                    Log.i(TAG, resGetPagingBroadcastList.toString())

                    setLoadingIndicator(false)

                    showBroadcasts(resGetPagingBroadcastList,followList)
                }
            }

            override fun onDataNotAvailable() {
                view.run {
                    if (!isActive) return@onDataNotAvailable

                    Log.i(TAG,"onPagingBroadcastListLoaded onDataNotAvailable")
                    setLoadingIndicator(false)
                }
            }

        })

    }


    override fun insertFollower(userId: String, followerId: String) {

        repository.insertFollower(userId,followerId, object  : ApiHelper.GetSuccessCallback{
            override fun onSuccessLoaded() {

                FirebaseMessaging.getInstance().subscribeToTopic(userId)

                loadBroadcasts()
            }

            override fun onDataNotAvailable() {
                Log.i(TAG,"insertFollower onDataNotAvailable")
            }
        })
    }

    override fun deleteFollower(userId: String, followerId: String) {
        repository.deleteFollower(userId,followerId, object  : ApiHelper.GetSuccessCallback{
            override fun onSuccessLoaded() {
                loadBroadcasts()

                FirebaseMessaging.getInstance().unsubscribeFromTopic(userId)
            }

            override fun onDataNotAvailable() {
                Log.i(TAG,"deleteFollower onDataNotAvailable")
            }
        })
    }



}