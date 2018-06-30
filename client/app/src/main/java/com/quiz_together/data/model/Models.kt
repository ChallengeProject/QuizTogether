package com.quiz_together.data.model

import com.google.gson.annotations.Expose

// dummy method
data class Event(val id:Int, val topics:String, val thumbnail:String)
data class Events(val events:List<Event>)


// login
class RespEmpty()

data class ReqLogin(val name:String)
data class RespLogin(val userId:String, val name:String)

//updateUserProfile
data class User(val userId:String, val name :String, val profilePath:String, val money:Long, val broadcastBeforeStarting:BroadcastBeforeStarting)
data class BroadcastBeforeStarting(val broadcastId:String , val scheduledTime:Long)

data class QustionList(val answerNo:Int, val questionTitle : String,val options:List<String>, val category:Int)

data class userRes(val userId: String,val name:String)

data class Broadcast(
        val broadcastId:String,
        val title:String,
        val description:String,
        val scheduledTime:Long,
        val giftType:Int,
        val prize:Long,
        val giftDescription:String,

        var userId:String,
        var broadcastStatus:Int,
        var winnerMessage:String,
        var userRes:userRes,
        var QuestionList:List<QustionList>,
        var questionCount:Int
)

data class ReqSendAnswer(val step :Int, val userId: String, val broadcastId: String, val answerNo: Int)

data class ReqEndBroadcast(val broadcastId: String, val userId: String, var streamId:String, var chatId:String)

data class ReqStartBroadcast(val broadcastId: String, val userId: String, var streamingUrl:String, var scheduledTime:String)

