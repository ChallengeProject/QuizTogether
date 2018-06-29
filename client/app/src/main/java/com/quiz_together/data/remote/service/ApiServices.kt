package com.quiz_together.data.remote.service

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import com.quiz_together.BuildConfig
import com.quiz_together.data.model.*

interface ApiServices {

    @POST("/events/login")
    fun login(@Body body: ReqIdPw): Observable<Resp<RespIdPw>>

    @GET("/events")
    fun getEvents(): Observable<Resp<Events>>


    companion object Factory {
        fun create(): ApiServices {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BuildConfig.SERVER_URL)
                    .build()

            return retrofit.create(ApiServices::class.java);
        }
    }

}
