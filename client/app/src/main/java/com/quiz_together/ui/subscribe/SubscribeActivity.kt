package com.quiz_together.ui.subscribe

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.quiz_together.R
import com.quiz_together.data.Repository
import com.quiz_together.util.replaceFragmentInActivity
import com.quiz_together.util.setupActionBar
import kotlinx.android.synthetic.main.activity_subscribe.*


class SubscribeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_subscribe)

        val broadcastId = "tmp"

        val fragment = supportFragmentManager
                .findFragmentById(R.id.fl_content) as SubscribeFragment? ?:
        SubscribeFragment.newInstance().also {

            replaceFragmentInActivity(it, R.id.fl_content)
        }

        SubscribePresenter(broadcastId,Repository ,fragment)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val BROADCAST_ID = "BROADCAST_ID"
    }



}
