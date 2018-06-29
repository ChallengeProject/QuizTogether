package com.quiz_together.ui.login

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import kotlinx.android.synthetic.main.frag_login.*
import com.quiz_together.R
import com.quiz_together.ui.event.EventActivity
import com.quiz_together.util.setTouchable
import com.quiz_together.util.setVisibilityFromBoolean
import com.quiz_together.util.toast

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

        activity?.findViewById<FloatingActionButton>(R.id.fab)?.setOnClickListener {
            //            presenter.editTask()
            Log.i(TAG,"findViewById<FloatingActionButton>")
        }

        btLogin.setOnClickListener{
            presenter.loginTask(etId.text.toString(),etPw.text.toString())
        }
    }

    override fun showMainUi(id:String) {

        val intent = Intent(context, EventActivity::class.java).apply {
            putExtra(EventActivity.EXTRA_ID, id)
        }
        startActivity(intent)
        activity?.finish()
    }

    override fun showFirstLaunch() {
        "showFirstLaunch".toast()
    }

    companion object {
        fun newInstance() = LoginFragment()
    }

}