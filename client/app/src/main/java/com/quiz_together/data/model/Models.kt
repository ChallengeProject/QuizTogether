package com.quiz_together.data.model

import com.google.gson.annotations.Expose

// dummy method
data class Event(val id:Int, val topics:String, val thumbnail:String)
data class Events(val events:List<Event>)


// login
class RespEmpty()

data class ReqLogin(val name:String)
data class RespLogin(val userId:Long, val name:String)

//updateUserProfile
data class User(val userId:Long, val name :String, val profilePath:String, val money:Long, val broadcastBeforeStarting:BroadcastBeforeStarting)
data class BroadcastBeforeStarting(val broadcastId:Long , val scheduledTime:Long)

data class Option(val option:String)
data class QustionList(val answerNo:Int, val questionTitle : String,val options:List<Option>, val category:Int)
data class Broadcast(
        val broadcastId:Long,
        val title:String,
        val description:String,
        val scheduledTime:String,
        val giftType:String,
        val prize:String,
        val giftDescription:String,

        var userId:Long,
        var broadcastStatus:String,
        var winnerMessage:String,
        var userRes:String,
        var QuestionList:List<QustionList>,
        var quizQuiestionCount:Int
)

data class ReqSendAnswer(val step :Int, val userId: String, val broadcastId: String, val answerNo: Int)

data class ReqEndBroadcast(val broadcastId: String, val userId: String, var streamId:String, var chatId:String)

data class ReqStartBroadcast(val broadcastId: String, val userId: String, var streamingUrl:String, var scheduledTime:String)

