package com.quiz_together.data.remote.service

import com.google.gson.GsonBuilder
import com.quiz_together.BuildConfig
import com.quiz_together.data.model.*
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiServices {

    // dummy method
    @GET("/events")
    fun getEvents(): Observable<Resp<Events>>

    // user
    @POST("${BuildConfig.REST_PREFIX}/user/login")
    fun login(@Body data: ReqLogin) : Observable<Resp<RespLogin>>

    @GET("${BuildConfig.REST_PREFIX}/user/findUserByName")
    fun findUserByName(@Query("name") name:String) : Observable<Resp<RespEmpty>>

    @GET("${BuildConfig.REST_PREFIX}/user/getUserProfile")
    fun getUserProfile(@Query("userId") usrId:String) : Observable<Resp<User>>

    // broadcast
    @POST("${BuildConfig.REST_PREFIX}/broadcast/createBroadcast")
    fun createBroadcast(@Body data: Broadcast) : Observable<Resp<RespEmpty>>

    @GET("${BuildConfig.REST_PREFIX}/broadcast/getBroadcastList")
    fun getBroadcastList() : Observable<Resp<List<Broadcast>>>

    @GET("${BuildConfig.REST_PREFIX}/broadcast/getBroadcastList")
    fun getBroadcastById(@Query("broadcastId") broadcastId:String) : Observable<Resp<Broadcast>>

    @POST("${BuildConfig.REST_PREFIX}/broadcast/updateBroadcast")
    fun updateBroadcast(@Body data: Broadcast) : Observable<Resp<Broadcast>>

    @POST("${BuildConfig.REST_PREFIX}/broadcast/sendAnswer")
    fun sendAnswer(@Body data: ReqSendAnswer) : Observable<Resp<RespEmpty>>

    @POST("${BuildConfig.REST_PREFIX}/broadcast/endBroadcast")
    fun endBroadcast(@Body data: ReqEndBroadcast) : Observable<Resp<RespEmpty>>

    @POST("${BuildConfig.REST_PREFIX}/broadcast/startBroadcast")
    fun startBroadcast(@Body data: ReqStartBroadcast) : Observable<Resp<RespEmpty>>

    @GET("${BuildConfig.REST_PREFIX}/broadcast/getBroadcastForUpdateById")
    fun getBroadcastForUpdateById(@Query("broadcastId") broadcastId:String) : Observable<Resp<Broadcast>>



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
