package com.quiz_together.data.model

data class CurBroadcastInfo(
        var viewerCount:Int,
        var fcmMsg:String,
        var lastQuestionNum:Int,
        var broadcastId:String
)