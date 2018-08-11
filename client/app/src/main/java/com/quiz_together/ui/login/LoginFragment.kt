package com.quiz_together.ui.login

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.quiz_together.R
import com.quiz_together.ui.loading.LoadingActivity
import com.quiz_together.util.setTouchable
import com.quiz_together.util.setVisibilityFromBoolean
import kotlinx.android.synthetic.main.frag_login.*

class LoginFragment : Fragment(), LoginContract.View {

    val TAG = "LoginFragment"

    override lateinit var presenter: LoginContract.Presenter

    override var isActive: Boolean = false
        get() = isAdded

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.frag_login, container, false)

        return root
    }

    override fun setLoadingIndicator(active: Boolean) {
        activity?.getWindow()?.setTouchable(active)
        pb.setVisibilityFromBoolean(active)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btStart.setOnClickListener { v->
            presenter.checkTask(etId.text.toString())
        }
    }

    override fun showLoadingUi() {

        activity?.run{
            startActivity(Intent(context, LoadingActivity::class.java))
            finish()
        }
    }

    override fun isCheckSuccess(isSuccess: Boolean) {

        if(isSuccess) {
            presenter.signupTask(etId.text.toString())
        } else {
            //TODO 다이얼로그로
//            "duplicated id".toast()
        }
    }

    override fun showFailLoginTxt() {
        //   "error".toast()
    }

    companion object {
        fun newInstance() = LoginFragment()
    }

}