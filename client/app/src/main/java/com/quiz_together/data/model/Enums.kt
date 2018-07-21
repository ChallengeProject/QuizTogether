package com.quiz_together.data.model

enum class GiftType(value:Int) {
    PRIZE(100),
    GIFT(200)
}

enum class BroadcastType (value:Int) {
    PUBLIC(100),
    PRIVATE(200);
}

enum class CategoryType (value:Int){
    NORMAL(0);
}

enum class UserStatus(value:Int = 0) {
    NORMAL(0),
    DELETED(1)
}

enum class BroadcastStatus(value:Int) {
    CREATED(100),
    WATING(200),
    OPEN_QUESTION(300),
    OPEN_ANSWER(400)
}