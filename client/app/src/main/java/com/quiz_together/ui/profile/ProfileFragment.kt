package com.quiz_together.ui.profile

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.quiz_together.R
import com.quiz_together.data.model.User
import com.quiz_together.util.getDateTime
import com.quiz_together.util.setTouchable
import com.quiz_together.util.setVisibilityFromBoolean
import com.quiz_together.util.toast
import kotlinx.android.synthetic.main.frag_profile.*


class ProfileFragment : Fragment(), ProfileContract.View {

    val TAG = "ProfileFragment"

    override lateinit var presenter: ProfileContract.Presenter

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
        val root = inflater.inflate(R.layout.frag_profile, container, false)

        return root
    }

    override fun setLoadingIndicator(active: Boolean) {
        activity?.getWindow()?.setTouchable(active)
        pb.setVisibilityFromBoolean(active)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.findViewById<FloatingActionButton>(R.id.fab)?.setOnClickListener {
            Log.i(TAG,"findViewById<FloatingActionButton>")
        }

        tvMyBroadcastInfo.setOnClickListener { _ ->
            //TODO start activity
        }

    }

    override fun updateUserView(user: User) {
        tvMyBroadcastInfo.text = "${user.broadcastBeforeStarting.scheduledTime.getDateTime()} 예정된 퀴즈방으로 가기"
        tvName.text = user.name
    }

    override fun showFailGetProfileTxt() {
        "fail to get profile info".toast()
    }


    companion object {
        fun newInstance() = ProfileFragment()
    }

}