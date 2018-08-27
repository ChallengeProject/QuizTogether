package com.quiz_together.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import com.facebook.Profile
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.quiz_together.R
import com.quiz_together.ui.loading.LoadingActivity
import com.quiz_together.util.setTouchable
import com.quiz_together.util.setVisibilityFromBoolean
import com.quiz_together.util.toast
import kotlinx.android.synthetic.main.frag_login.*
import org.json.JSONObject

class LoginFragment : Fragment(), LoginContract.View {

    val TAG = "LoginFragment#$#"

    override lateinit var presenter: LoginContract.Presenter

    override var isActive: Boolean = false
        get() = isAdded

    var callbackManager : CallbackManager? = null
    var accTkn : AccessToken? = null

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



        initFb()

    }

    fun initFb(){

        btFb.setOnClickListener { v ->
            lbtFb.performClick()
        }


        callbackManager = CallbackManager.Factory.create()

        lbtFb.setReadPermissions("email");
        lbtFb.setFragment(this);
        lbtFb.registerCallback(callbackManager, object: FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                Log.i(TAG,"override fun onSuccess")

                accTkn = result!!.accessToken

                getFbUserProfile(accTkn!!)

            }

            override fun onCancel() {
                Log.i(TAG,"override fun onCancel")
            }

            override fun onError(error: FacebookException?) {
                Log.i(TAG,"override fun onError")
            }

        })

        tmptmpTEST.setOnClickListener { v ->
            Log.i(TAG,Profile.getCurrentProfile().id)
        }

        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired

        Log.i(TAG,"Fb isLogin $isLoggedIn")
        if(isLoggedIn) Log.i(TAG,Profile.getCurrentProfile().id)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager!!.onActivityResult(requestCode, resultCode, data) // for fb login
        super.onActivityResult(requestCode, resultCode, data)
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
           "error".toast()
    }


    override fun getAct() : Activity {
        return activity!!
    }

    fun getFbUserProfile(currentAccessToken: AccessToken ) {
        GraphRequest.newMeRequest(
                currentAccessToken, object :  GraphRequest.GraphJSONObjectCallback {
            override fun onCompleted(js: JSONObject?, response: GraphResponse?) {

                Log.i(TAG,js!!.get("id").toString())
            }
        }).run {
            val parameters = Bundle()
            parameters.putString("fields", "id");
            setParameters(parameters);
            executeAsync();
        }
    }

    companion object {
        fun newInstance() = LoginFragment()
    }
}