package com.quiz_together.ui.create

import com.quiz_together.data.Repository
import com.quiz_together.data.remote.ApiHelper


class CreatePresenter(
        private val repository: Repository,
        private val view: CreateContract.View
) : CreateContract.Presenter {
    init {
        view.presenter = this
    }

    override fun start() {

    }

    override fun requestCreateBroadcast() {
        if (!view.isValidatedBroadcast()) {
            return
        }

        val broadcast = view.extractBroadcast()
        Repository.createBroadcast(broadcast, object : ApiHelper.GetSuccessBroadcastIdCallback {
            override fun onSuccessLoaded(broadcastId: String) {
                view.onSuccessCreatedBroadcast(broadcastId)
            }

            override fun onDataNotAvailable() {
                view.onErrorCreatedBroadcast()
            }
        })
    }

    override fun loadQuizIfHasSavedQuiz() {
        if (Repository.hasSavedQuiz()) {
            view.setLoadingIndicator(true)
            loadQuiz()
        }
        view.setLoadingIndicator(false)
    }

    private fun loadQuiz() {
        val savedBroadcast = Repository.getSavedQuiz()
        view.loadQuiz(savedBroadcast)
        view.setLoadingIndicator(false)
    }

    override fun saveQuiz() {
        if (view.isValidatedBroadcast()) {
            val broadcast = view.extractBroadcast()
            Repository.saveQuiz(broadcast)
        } else {
            // toast(isValidatedBroadcast 에서 해주고 있음) or 예외 타입 이넘 클래스 생성해서 예외에 따른 처리 해주기
        }
    }

}