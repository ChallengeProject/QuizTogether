package com.quiz_together.data.model

// dummy method
data class Event(val id: Int, val topics: String, val thumbnail: String)

data class Events(val events: List<Event>)


// login
class RespEmpty()

data class ReqSignup(val name: String, val pushToken: String)

data class ReqLogin(val userId: String)

//updateUserProfile
data class User(val userId: String, val name: String, val profilePath: String, val money: Long,
                val broadcastBeforeStarting: BroadcastBeforeStarting)

data class BroadcastBeforeStarting(val broadcastId: String, val scheduledTime: Long)

data class QuestionProp(var title: String = "", var options: ArrayList<String> = ArrayList(3))

data class Question(var answerNo: Int = -1, var questionId: String? = null,
                    var questionProp: QuestionProp = QuestionProp(), var category: CategoryType = CategoryType.NORMAL) {
    fun isValidate(): Boolean {
        return !(answerNo == -1 || questionProp.title.isBlank() || questionProp.options.any(String::isBlank))
    }
}

data class UserView(val userId: String, val name: String)

data class Broadcast(
        val broadcastId: String?,
        val title: String,
        val description: String,
        val scheduledTime: Long?,
        val remainingStartSeconds: Long?,
        val giftType: GiftType = GiftType.NONE,
        val prize: Long?,
        val giftDescription: String?,
        val userId: String?,
        val broadcastStatus: BroadcastStatus?,
        val winnerMessage: String,
        val userInfoView: UserView?,
        val questionList: ArrayList<Question>,
        val questionCount: Int,
        var roomOutputType: RoomOutputType?
)

data class ResGetPagingBroadcastList(val myBroadcastList: List<Broadcast>,
                                     val currentBroadcastList: List<Broadcast>)

data class ResStartBroadcast(val broadcastView: Broadcast)

data class BroadcastJoinInfo(val broadcastView: Broadcast, val userInfoView: UserView,
                             val question: String, val step: Int, val answerNo: Int,
                             val playUserStatus: PlayUserStatus, val viewerCount: Int)

data class ReqSendAnswer(val step: Int, val userId: String, val broadcastId: String,
                         val answerNo: Int)

data class ReqEndBroadcast(val broadcastId: String, val userId: String, var streamId: String,
                           var chatId: String)

data class ReqStartBroadcast(val broadcastId: String, val userId: String, var streamingUrl: String,
                             var scheduledTime: String)

data class ReqSendChatMsg(val broadcastId: String, val userId: String, val message: String)

data class ReqUpdateBroadcast(val broadcastId: String, val userId: String,
                              var broadcastStatus: BroadcastStatus)

data class ReqBrdIdAndUsrId(val broadcastId: String, val userId: String)

data class ReqFollow(val userId: String, val follower: String)

data class ResFollowList(val userFollowerList: List<Follower>)

data class Follower(val follower: String)
