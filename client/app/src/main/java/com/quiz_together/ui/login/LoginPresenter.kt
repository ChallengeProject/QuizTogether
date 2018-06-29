package com.quiz_together.ui.login

import com.quiz_together.data.Repository
import com.quiz_together.data.model.RespIdPw
import com.quiz_together.data.remote.ApiHelper

class LoginPresenter(
        private val repository: Repository,
        private val loginView: LoginContract.View
) : LoginContract.Presenter {

    init {
        loginView.presenter = this
    }

    override fun start() {
        if(repository.isFirstLaunch()){
            repository.setIsFirst(false)
            loginView.showFirstLaunch()
        }
    }

    override fun loginTask(id: String, pw: String) {

        loginView.setLoadingIndicator(true)

        repository.login(id,pw, object : ApiHelper.LoginCallback {

            override fun onLoginLoaded(respIdPw: RespIdPw) {

                loginView.run{
                    if(!isActive) return@onLoginLoaded

                    setLoadingIndicator(false)

                    if(respIdPw.result == "success")
                        showMainUi(id)
                }

            }

            override fun onDataNotAvailable() {

                loginView.run {
                    if(!isActive) return@onDataNotAvailable

                    setLoadingIndicator(false)
                }

            }

        })
    }

}