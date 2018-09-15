package com.quiz_together.ui.create

import com.quiz_together.data.Repository
import com.quiz_together.data.model.Broadcast
import com.quiz_together.data.model.GiftType
import com.quiz_together.data.model.Question
import com.quiz_together.data.remote.ApiHelper


class CreatePresenter(private val repository: Repository, private val view: CreateContract.View)
    : CreateContract.Presenter {

    private val mQuestions = HashMap<Int, Question>()

    init {
        view.presenter = this
    }

    override fun start() {}

    override fun updateQuestion(position: Int, item: Question) {
    }

    override fun saveQuiz() {
        val broadcast = view.extractBroadcast()

        repository.saveQuiz(broadcast)
    }

    override fun createBroadcast() {
        val broadcast = view.extractBroadcast()

        if (isValidatedBroadcast(broadcast)) {
            return
        }

        repository.createBroadcast(broadcast, object : ApiHelper.GetSuccessBroadcastIdCallback {
            override fun onSuccessLoaded(broadcastId: String) {
                view.onSuccessCreatedBroadcast(broadcastId)
            }

            override fun onDataNotAvailable() {
                view.onErrorCreatedBroadcast()
            }
        })
    }

    override fun loadQuizIfHasSavedQuiz() {
        if (repository.hasSavedQuiz()) {
            view.setLoadingIndicator(true)
            loadQuiz()
        }
        view.setLoadingIndicator(false)
    }

    private fun loadQuiz() {
        val savedBroadcast = repository.getSavedQuiz()
        view.loadQuiz(savedBroadcast)
        view.setLoadingIndicator(false)
    }

    private fun isValidatedBroadcast(broadcast: Broadcast): Boolean {
        var msg: String? = null

        if (broadcast.title.isEmpty()) {
            msg = "방 제목이 입력되지 않았습니다."
        } else if (broadcast.giftType == GiftType.NONE) {
            msg = "Gift Type 을 설정해 주세요."
        } else if (broadcast.giftType == GiftType.GIFT && broadcast.giftDescription == null
                || broadcast.giftDescription!!.isEmpty()) {
            msg = "상품을 입력해 주세요"
        } else if (broadcast.giftType == GiftType.PRIZE && broadcast.prize == null
                || broadcast.prize!! == 0L) {
            msg = "상금을 입력해 주세요."
        } else if (broadcast.winnerMessage.isEmpty()) {
            msg = "우승자 메시지를 입력해 주세요."
        } else if (mQuestions.size == 0) {
            msg = "현재 작성된 문제가 없습니다."
        }

        if (msg != null) {
            view.showToast(msg)
            return false
        }

        for (i in 0..12) {
            mQuestions[i]?.let {
                isValidatedQuestion(it)?.let {
                    view.showToast(it)
                    return false
                }
            }
        }

        return true
    }

    private fun isValidatedQuestion(question: Question): String? {
        return when {
            question.questionProp.title.isEmpty() -> "문제를 입력해주세요."
            question.questionProp.options[0].isEmpty() -> "보기1을 입력해주세요."
            question.questionProp.options[1].isEmpty() -> "보기2을 입력해주세요."
            question.questionProp.options[2].isEmpty() -> "보기3을 입력해주세요."
            question.answerNo < 0 -> "정답을 선택해주세요."
            else -> null
        }
    }
}