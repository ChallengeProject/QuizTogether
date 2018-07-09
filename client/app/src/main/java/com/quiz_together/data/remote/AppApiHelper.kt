package com.quiz_together.data.remote

import android.util.Log
import com.quiz_together.data.model.Broadcast
import com.quiz_together.data.model.ReqEndBroadcast
import com.quiz_together.data.model.ReqLogin
import com.quiz_together.data.model.ReqSendAnswer
import com.quiz_together.data.model.ReqSignup
import com.quiz_together.data.model.ReqStartBroadcast
import com.quiz_together.data.remote.service.ApiServices
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AppApiHelper : ApiHelper {


    val TAG = "AppApiHelper"

    val apiServices = ApiServices.create()

    // dummy method
    override fun getEvents(cb: ApiHelper.GetEventsCallback) {

        Log.i(TAG,"getEvents")

        apiServices.getEvents()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ it ->

                    if(it.code == 200)
                        cb.onEventsLoaded(it.data)
                    else
                        cb.onDataNotAvailable()

                }, { error ->
                    cb.onDataNotAvailable()
                })

    }

    /////



    override fun signup(name: String ,pushToken :String, cb: ApiHelper.UserResCallback) {

        apiServices.signup(ReqSignup(name,pushToken))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ it ->

                    if(it.code == 200)
                        cb.onLoginLoaded(it.data)
                    else
                        cb.onDataNotAvailable()

                }, { error ->
                    cb.onDataNotAvailable()
                })


    }

    override fun login(name: String , cb: ApiHelper.UserResCallback) {

        apiServices.login(ReqLogin(name))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ it ->

                    if(it.code == 200)
                        cb.onLoginLoaded(it.data)
                    else
                        cb.onDataNotAvailable()

                }, { error ->
                    cb.onDataNotAvailable()
                })


    }

    override fun findUserByName(name: String , cb: ApiHelper.GetSuccessCallback) {
        apiServices.findUserByName(name)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ it ->
                    if(it.code == 200)
                        cb.onSuccessLoaded()
                    else
                        cb.onDataNotAvailable()
                }, { error ->
                    cb.onDataNotAvailable()
                })
    }

    override fun getUserProfile(userId: String , cb: ApiHelper.GetUserCallback) {
        apiServices.getUserProfile(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ it ->
                    if(it.code == 200)
                        cb.onUserLoaded(it.data)
                    else
                        cb.onDataNotAvailable()
                }, { error ->
                    cb.onDataNotAvailable()
                })
    }

    // broadcast
    override fun createBroadcast(broadcast: Broadcast, cb: ApiHelper.GetSuccessCallback) {
        apiServices.createBroadcast(broadcast)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ it ->
                    if(it.code == 200)
                        cb.onSuccessLoaded()
                    else
                        cb.onDataNotAvailable()
                }, { error ->
                    cb.onDataNotAvailable()
                })
    }

    override fun getBroadcastList(cb: ApiHelper.GetBroadcastsCallback) {
        apiServices.getBroadcastList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ it ->
                    if(it.code == 200)
                        cb.onBroadcastsLoaded(it.data.broadcasts)
                    else
                        cb.onDataNotAvailable()
                }, { error ->
                    cb.onDataNotAvailable()
                })
    }

    override fun getBroadcastById(broadcastId:String ,cb: ApiHelper.GetBroadcastCallback) {
        apiServices.getBroadcastById(broadcastId )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ it ->
                    if(it.code == 200)
                        cb.onBroadcastLoaded(it.data)
                    else
                        cb.onDataNotAvailable()
                }, { error ->
                    cb.onDataNotAvailable()
                })
    }

    override fun updateBroadcast(broadcast: Broadcast, cb: ApiHelper.GetSuccessCallback) {
        apiServices.updateBroadcast(broadcast)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ it ->
                    if(it.code == 200)
                        cb.onSuccessLoaded()
                    else
                        cb.onDataNotAvailable()
                }, { error ->
                    cb.onDataNotAvailable()
                })
    }

    override fun sendAnswer(reqSendAnswer: ReqSendAnswer, cb: ApiHelper.GetSuccessCallback) {
        apiServices.sendAnswer(reqSendAnswer)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ it ->
                    if(it.code == 200)
                        cb.onSuccessLoaded()
                    else
                        cb.onDataNotAvailable()
                }, { error ->
                    cb.onDataNotAvailable()
                })
    }

    override fun endBroadcast(reqEndBroadcast: ReqEndBroadcast, cb: ApiHelper.GetSuccessCallback) {
        apiServices.endBroadcast(reqEndBroadcast)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ it ->
                    if(it.code == 200)
                        cb.onSuccessLoaded()
                    else
                        cb.onDataNotAvailable()
                }, { error ->
                    cb.onDataNotAvailable()
                })
    }

    override fun startBroadcast(reqStartBroadcast: ReqStartBroadcast, cb: ApiHelper.GetSuccessCallback) {
        apiServices.startBroadcast(reqStartBroadcast)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ it ->
                    if(it.code == 200)
                        cb.onSuccessLoaded()
                    else
                        cb.onDataNotAvailable()
                }, { error ->
                    cb.onDataNotAvailable()
                })
    }

    override fun getBroadcastForUpdateById(broadcastId:String ,cb: ApiHelper.GetBroadcastCallback) {
        apiServices.getBroadcastById(broadcastId )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ it ->
                    if(it.code == 200)
                        cb.onBroadcastLoaded(it.data)
                    else
                        cb.onDataNotAvailable()
                }, { error ->
                    cb.onDataNotAvailable()
                })
    }



}