package com.quiz_together.ui.profile

import com.quiz_together.data.Repository
import com.quiz_together.data.model.User
import com.quiz_together.data.remote.ApiHelper
import com.quiz_together.util.SC


class ProfilePresenter(
        private val repository: Repository,
        private val view: ProfileContract.View
) : ProfileContract.Presenter {

    init {
        view.presenter = this
    }

    override fun start() {
        getProfileTask()
    }

    fun getProfileTask() {

        SC.USER_ID?.run {
            repository.getUserProfile(this,object:ApiHelper.GetUserCallback {
                override fun onUserLoaded(user: User) {
                    view.updateUserView(user)
                }

                override fun onDataNotAvailable() {
                    view.showFailGetProfileTxt()
                }

            })
        }?:let {
            throw Exception("SC.USER_ID is null")
        }

//        repository.login(id, object : ApiHelper.LoginCallback {
//            override fun onLoginLoaded(respLogin: RespLogin) {
//
//                loginView.run{
//                    if(!isActive) return@onLoginLoaded
//                    setLoadingIndicator(false)
//
//                    SC.USER_ID = respLogin.name
//                    repository.setUserId(respLogin.userId)
//
//                    showMainUi()
//                }
//            }
//
//            override fun onDataNotAvailable() {
//
//                loginView.run {
//                    if(!isActive) return@onDataNotAvailable
//                    setLoadingIndicator(false)
//
//                    showFailLoginTxt()
//                }
//            }
//        })
    }


}