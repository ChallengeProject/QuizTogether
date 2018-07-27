package com.quiz_together.ui.subscribe

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Window
import android.widget.Toast
import com.quiz_together.R
import com.quiz_together.data.Repository
import com.quiz_together.data.remote.FirebaseHelper
import com.quiz_together.util.replaceFragmentInActivity


class SubscribeActivity : AppCompatActivity() {

    val TAG = "SubscribeActi#$#"

    lateinit var subscribePresenter:SubscribePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_subscribe)

        //TODO using dummy [broadcastId] now
//        val broadcastId = intent.getStringExtra(BROADCAST_ID)
        val broadcastId = "thisisdummy-asdfasdf"

        val fragment = supportFragmentManager
                .findFragmentById(R.id.fl_content) as SubscribeFragment? ?:
        SubscribeFragment.newInstance().also {

            replaceFragmentInActivity(it, R.id.fl_content)
        }

        subscribePresenter = SubscribePresenter(broadcastId,Repository ,fragment)

    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, IntentFilter(FirebaseHelper.FMC_ACTION))
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                mMessageReceiver)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private val mMessageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            val fcmMsg = intent.getStringExtra(FirebaseHelper.FMC_IN_QUIZING)
            Log.i(TAG, "onReceive : $fcmMsg")
            Toast.makeText(applicationContext, fcmMsg, Toast.LENGTH_LONG).show()

            subscribePresenter.onFcmListener(fcmMsg)

        }
    }

    companion object {
        const val BROADCAST_ID = "BROADCAST_ID"
    }



}
