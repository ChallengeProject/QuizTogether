package com.quiz_together.data.remote

import com.quiz_together.data.model.Event
import com.quiz_together.data.model.Events
import com.quiz_together.data.model.Resp
import com.quiz_together.data.model.RespIdPw

interface ApiHelper {

    interface LoginCallback{

        fun onLoginLoaded(respIdPw: RespIdPw)

        fun onDataNotAvailable()

    }

    interface GetEventsCallback{

        fun onEventsLoaded(events : Events)

        fun onDataNotAvailable()

    }

    fun login(id:String, pw:String, cb: LoginCallback)


    fun getEvents(cb: GetEventsCallback)

}