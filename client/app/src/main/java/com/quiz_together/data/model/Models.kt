package com.quiz_together.data.model

data class ReqIdPw(val id:String, val pw:String)

data class RespIdPw(val result:String?)

data class Event(val id:Int, val topics:String, val thumbnail:String)

data class Events(val events:List<Event>)
