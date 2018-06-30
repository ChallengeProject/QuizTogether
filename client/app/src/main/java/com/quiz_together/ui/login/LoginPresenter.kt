package com.quiz_together.ui.login

import com.quiz_together.data.Repository
import com.quiz_together.data.model.RespLogin
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

        repository.login(id, object : ApiHelper.LoginCallback {
            override fun onLoginLoaded(respLogin: RespLogin) {

                loginView.run{
                    if(!isActive) return@onLoginLoaded

                    setLoadingIndicator(false)

//                    respLogin.name


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