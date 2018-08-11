package com.quiz_together.ui.main.home

import android.util.Log
import android.widget.ProgressBar
import com.google.firebase.messaging.FirebaseMessaging
import com.quiz_together.data.Repository
import com.quiz_together.data.model.ResGetPagingBroadcastList
import com.quiz_together.data.remote.ApiHelper
import com.quiz_together.data.remote.FirebaseHelper.Companion.FIREBASE_KEY_FOR_EVERYONE
import com.quiz_together.util.SC

class HomePresenter  (val view: HomeFragment,val pb: ProgressBar,val repository:Repository) : HomeContract.Presenter {

    private val TAG = "HomePresenter#$#"


    override fun start() {
        loadBroadcasts()
        registFirebase()
    }

    fun registFirebase() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(FIREBASE_KEY_FOR_EVERYONE)
        FirebaseMessaging.getInstance().subscribeToTopic(FIREBASE_KEY_FOR_EVERYONE).addOnSuccessListener {
            Log.i(TAG, "success regist topic >> ${FIREBASE_KEY_FOR_EVERYONE}") // empty same key
        }.addOnCompleteListener {
            Log.i(TAG, "complete regist topic >> ${FIREBASE_KEY_FOR_EVERYONE}") // duplicate
        }
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

//            override fun onBroadcastsLoaded(broadcasts: List<Broadcast>) {
//                view.run{
//                    if(!isActive) return@onBroadcastsLoaded
//
//                    Log.i(TAG,broadcasts.toString())
//
//                    setLoadingIndicator(false)
//
//                    showBroadcasts(broadcasts)
//                }
//            }


            override fun onDataNotAvailable() {
                view.run {
                    if (!isActive) return@onDataNotAvailable

                    Log.i(TAG,"onDataNotAvailable")
                    setLoadingIndicator(false)

                    //TODO #############################
                    //TODO need to delete ( for test )

//                    val broadcasts = ArrayList<Broadcast>()
//                    broadcasts.add(Broadcast("aa","라인프렌즈의하루1","브라운이연애를한다던데? 1",11111111,
//                            1,1000,"giftDescription","LINE+",0,"winnerMessage",null,null,0))
//
//                    broadcasts.add(Broadcast("bb","라인프렌즈의하루2","브라운이연애를한다던데? 2",11111111,
//                            1,1000,"giftDescription","NAVER+",0,"winnerMessage",null,null,0))
//
//                    broadcasts.add(Broadcast("cc","라인프렌즈의하루3","브라운이연애를한다던데? 3",11111111,
//                            1,1000,"giftDescription","SAMSUNG+",0,"winnerMessage",null,null,0))

//                    showBroadcasts(broadcasts)

                    //TODO #############################
                }
            }

        })


    }

}