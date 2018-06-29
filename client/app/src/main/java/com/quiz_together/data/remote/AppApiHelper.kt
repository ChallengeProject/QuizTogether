package com.quiz_together.data.remote

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import com.quiz_together.data.model.ReqIdPw
import com.quiz_together.data.remote.service.ApiServices

class AppApiHelper : ApiHelper {


    val TAG = "AppApiHelper"

    val apiServices = ApiServices.create()

    override fun login(id: String, pw: String, cb: ApiHelper.LoginCallback) {

        val reqBody = ReqIdPw(id, pw)

        apiServices.login(reqBody)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ it ->

                    if(it.status == 200)
                        cb.onLoginLoaded(it.data)
                    else
                        cb.onDataNotAvailable()

                }, { error ->
                    cb.onDataNotAvailable()
                })

    }

    override fun getEvents(cb: ApiHelper.GetEventsCallback) {

        Log.i(TAG,"getEvents")

        apiServices.getEvents()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ it ->

                    if(it.status == 200)
                        cb.onEventsLoaded(it.data)
                    else
                        cb.onDataNotAvailable()

                }, { error ->
                    cb.onDataNotAvailable()
                })

    }

}