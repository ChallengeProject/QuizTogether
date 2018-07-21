package com.quiz_together.data.model

// dummy method
data class Event(val id:Int, val topics:String, val thumbnail:String)
data class Events(val events:List<Event>)


// login
class RespEmpty()

data class ReqSignup(val name:String, val pushToken :String)
data class ReqLogin(val userId:String)

//updateUserProfile
data class User(val userId:String, val name :String, val profilePath:String, val money:Long, val broadcastBeforeStarting:BroadcastBeforeStarting)

data class BroadcastBeforeStarting(val broadcastId:String , val scheduledTime:Long)

data class QuestionProp(val title:String, val options:List<String>)

data class QustionList(val answerNo:Int, val questionId : String?,val questionProp: QuestionProp, val category:CategoryType)

data class UserRes(val userId: String,val name:String)

data class Broadcast(
        val broadcastId:String?,
        val title:String,
        val description:String,
        val scheduledTime:Long?,
        val giftType: GiftType,
        val prize:Long,
        val giftDescription:String,

        val userId:String,
        val broadcastStatus: BroadcastStatus?,
        val winnerMessage:String,
        val userRes:UserRes?,
        val questionList:List<QustionList>,
        val questionCount:Int
)

data class ReqSendAnswer(val step :Int, val userId: String, val broadcastId: String, val answerNo: Int)

data class ReqEndBroadcast(val broadcastId: String, val userId: String, var streamId:String, var chatId:String)

data class ReqStartBroadcast(val broadcastId: String, val userId: String, var streamingUrl:String, var scheduledTime:String)

