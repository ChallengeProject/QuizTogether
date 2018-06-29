package com.quiz_together.data.model

data class Resp<T>(val status :Int, val message: String, val data :T)