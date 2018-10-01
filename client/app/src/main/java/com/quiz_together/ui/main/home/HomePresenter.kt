package com.quiz_together.ui.main.home

import android.util.Log
import android.widget.ProgressBar
import com.google.firebase.messaging.FirebaseMessaging
import com.quiz_together.data.Repository
import com.quiz_together.data.model.Broadcast
import com.quiz_together.data.model.CurBroadcastInfo
import com.quiz_together.data.model.Follower
import com.quiz_together.data.model.ReqEndBroadcast
import com.quiz_together.data.model.ResFollowList
import com.quiz_together.data.model.ResGetPagingBroadcastList
import com.quiz_together.data.remote.ApiHelper
import com.quiz_together.util.SC

class HomePresenter(val view: HomeFragment, val pb: ProgressBar, val repository: Repository) : HomeContract.Presenter {

    private val TAG = "HomePresenter#$#"


    override fun start() {
        loadBroadcasts()
        startBroadcastWhenCreated()
    }


    override fun loadBroadcasts() {

        if (repository.isFirstLaunch()) {
            repository.setIsFirst(false)
            repository.getFollowerList(SC.USER_ID, object : ApiHelper.GetFollowerListCallback {
                override fun onFollowerList(followList: ResFollowList) {

//                    Log.i(TAG, "onFollowerList " + followList.toString())
                    getPagingListAndProcWithFollowerList(followList.userFollowerList)
                }

                override fun onDataNotAvailable() {
                    Log.i(TAG, "getFollowerList onDataNotAvailable")
                }
            })
        } else {
            getPagingListAndProcWithFollowerList(
                    repository.getSavedFollowerList()
                            .map { Follower(it) }
            )
        }

    }

    fun getPagingListAndProcWithFollowerList(followList: List<Follower>) {

//        Log.i(TAG,"getPagingListAndProcWithFollowerList SC.USER_ID " + SC.USER_ID )

        repository.getPagingBroadcastList(SC.USER_ID, object : ApiHelper.GetPagingBroadcastList {
            override fun onPagingBroadcastListLoaded(resGetPagingBroadcastList: ResGetPagingBroadcastList) {
                view.run {
                    if (!isActive) return@run

                    Log.i(TAG, resGetPagingBroadcastList.toString())

                    setLoadingIndicator(false)

                    showBroadcasts(resGetPagingBroadcastList, followList)
                }
            }

            override fun onDataNotAvailable() {
                view.run {
                    if (!isActive) return@onDataNotAvailable

                    Log.i(TAG, "onPagingBroadcastListLoaded onDataNotAvailable")
                    setLoadingIndicator(false)
                }
            }

        })

    }


    override fun insertFollower(userId: String, followerId: String) {


        val tmpFollowList = repository.getSavedFollowerList()
                .toMutableSet()
        tmpFollowList.add(followerId)
        repository.setFollowerList(tmpFollowList)

        repository.insertFollower(userId, followerId, object : ApiHelper.GetSuccessCallback {
            override fun onSuccessLoaded() {

                FirebaseMessaging.getInstance().subscribeToTopic(followerId)

                loadBroadcasts()
            }

            override fun onDataNotAvailable() {
                Log.i(TAG, "insertFollower onDataNotAvailable")
            }
        })
    }

    override fun deleteFollower(userId: String, followerId: String) {

        val tmpFollowList = repository.getSavedFollowerList()
                .toMutableSet()
        tmpFollowList.remove(followerId)
        repository.setFollowerList(tmpFollowList)


        repository.deleteFollower(userId, followerId, object : ApiHelper.GetSuccessCallback {
            override fun onSuccessLoaded() {
                loadBroadcasts()

                FirebaseMessaging.getInstance().unsubscribeFromTopic(userId)
            }

            override fun onDataNotAvailable() {
                Log.i(TAG, "deleteFollower onDataNotAvailable")
            }
        })
    }

    override fun tmpEndBroadcast(broadcastId: String) {

        repository.removeCurBroadcstInfo()

        repository.endBroadcast(ReqEndBroadcast(broadcastId, SC.USER_ID, "", ""),
                object : ApiHelper.GetSuccessCallback {
                    override fun onSuccessLoaded() {
                        Log.i(TAG, "endBroadcast onSuccessLoaded")
                        loadBroadcasts()
                    }

                    override fun onDataNotAvailable() {
                        Log.i(TAG, "endBroadcast onDataNotAvailable")
                    }

                })

    }

    fun startBroadcastWhenCreated() {


        val jsonStr = repository.getCurBroadcstInfo()

        if(jsonStr.isNullOrEmpty()) {
            repository.removeCurBroadcstInfo()
            return
        }

        Log.i(TAG,"in startBroadcastWhenCreated  : $jsonStr")

        val curBroadcastInfo = SC.gson.fromJson(jsonStr, CurBroadcastInfo::class.java)

        if(curBroadcastInfo.fcmMsg.contains("END_MESSAGE")){
            Log.i(TAG,"catch END_MESSAGE, not getBroadcastById")
            repository.removeCurBroadcstInfo()
            return
        }

        repository.getBroadcastById(curBroadcastInfo.broadcastId,
                object :ApiHelper.GetBroadcastCallback{
                    override fun onBroadcastLoaded(broadcast: Broadcast) {
                        view.startBroadcast(broadcast.broadcastId,true)
                    }

                    override fun onDataNotAvailable() {
                        Log.i(TAG, "getBroadcastById onDataNotAvailable")
                        repository.removeCurBroadcstInfo()
                    }
                })




    }


}