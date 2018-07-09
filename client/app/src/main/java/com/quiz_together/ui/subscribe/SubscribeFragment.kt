package com.quiz_together.ui.subscribe

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.messaging.FirebaseMessaging
import com.quiz_together.R
import com.quiz_together.util.setTouchable
import com.quiz_together.util.setVisibilityFromBoolean
import kotlinx.android.synthetic.main.frag_subscribe.*

class SubscribeFragment : Fragment(), SubscribeContract.View {

    val TAG = "BaseFragment"

    override lateinit var presenter: SubscribeContract.Presenter

    lateinit var asdf : String

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
        val root = inflater.inflate(R.layout.frag_subscribe, container, false)

        return root
    }

    override fun setLoadingIndicator(active: Boolean) {
        activity?.getWindow()?.setTouchable(active)
        pb.setVisibilityFromBoolean(active)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btSendMsg.setOnClickListener{ v->
            // 메세지 보내기
        }



    }

    companion object {
        fun newInstance() = SubscribeFragment()
    }

}