package com.quiz_together.ui.login

import com.quiz_together.data.Repository
import com.quiz_together.data.model.UserView
import com.quiz_together.data.remote.ApiHelper
import com.quiz_together.util.SC

class LoginPresenter(
        private val repository: Repository,
        private val loginView: LoginContract.View
) : LoginContract.Presenter {

    init {
        loginView.presenter = this
    }

    override fun start() {

    }

    override fun checkTask(id: String) {

        loginView.setLoadingIndicator(true)

        repository.findUserByName(id, object : ApiHelper.GetSuccessCallback{
            override fun onSuccessLoaded() {
                loginView.run {
                    if(!isActive) return@onSuccessLoaded
                    setLoadingIndicator(false)

                    isCheckSuccess(true)
                }
            }

            override fun onDataNotAvailable() {
                loginView.run {
                    if (!isActive) return@onDataNotAvailable
                    setLoadingIndicator(false)

                    isCheckSuccess(false)
                }
            }
        })
    }


    override fun signupTask(name: String ) {

        loginView.setLoadingIndicator(true)

        repository.signup(name,"THIS_IS_DUMMY_DATA", object : ApiHelper.UserViewCallback{
            override fun onLoginLoaded(respLogin: UserView) {

                loginView.run{
                    if(!isActive) return@onLoginLoaded
                    setLoadingIndicator(false)

                    SC.USER_ID = respLogin.name
                    repository.setUserId(respLogin.userId)

                    showLoadingUi()
                }
            }

            override fun onDataNotAvailable() {

                loginView.run {
                    if(!isActive) return@onDataNotAvailable
                    setLoadingIndicator(false)

                    showFailLoginTxt()
                }
            }
        })
    }

}