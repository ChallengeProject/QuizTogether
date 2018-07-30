package com.quiz_together.ui.main.home

import android.util.Log
import android.widget.ProgressBar
import com.quiz_together.data.Repository
import com.quiz_together.data.model.Broadcast
import com.quiz_together.data.model.Events
import com.quiz_together.data.remote.ApiHelper

class HomePresenter  (val view: HomeFragment,val pb: ProgressBar,val repository:Repository) : HomeContract.Presenter {

    private val TAG = "HomePresenter#$#"

    override fun start() {

        loadBroadcasts()
    }

    override fun loadBroadcasts() {

        repository.getPagingBroadcastList(object : ApiHelper.GetBroadcastsCallback{
            override fun onBroadcastsLoaded(broadcasts: List<Broadcast>) {
                view.run{
                    if(!isActive) return@onBroadcastsLoaded

                    Log.i(TAG,broadcasts.toString())

                    setLoadingIndicator(false)

                    showBroadcasts(broadcasts)
                }
            }


            override fun onDataNotAvailable() {
                view.run {
                    if (!isActive) return@onDataNotAvailable

                    Log.i(TAG,"onDataNotAvailable")
                    setLoadingIndicator(false)

                    //TODO #############################
                    //TODO need to delete ( for test )

                    val broadcasts = ArrayList<Broadcast>()
//                    broadcasts.add(Broadcast("aa","라인프렌즈의하루1","브라운이연애를한다던데? 1",11111111,
//                            1,1000,"giftDescription","LINE+",0,"winnerMessage",null,null,0))
//
//                    broadcasts.add(Broadcast("bb","라인프렌즈의하루2","브라운이연애를한다던데? 2",11111111,
//                            1,1000,"giftDescription","NAVER+",0,"winnerMessage",null,null,0))
//
//                    broadcasts.add(Broadcast("cc","라인프렌즈의하루3","브라운이연애를한다던데? 3",11111111,
//                            1,1000,"giftDescription","SAMSUNG+",0,"winnerMessage",null,null,0))

                    showBroadcasts(broadcasts)

                    //TODO #############################
                }
            }

        })


    }

}