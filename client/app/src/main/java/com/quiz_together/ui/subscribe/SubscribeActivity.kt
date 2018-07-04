package com.quiz_together.ui.subscribe

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.quiz_together.R
import com.quiz_together.data.Repository
import com.quiz_together.util.replaceFragmentInActivity
import com.quiz_together.util.setupActionBar


class SubscribeActivity : AppCompatActivity() {

    private fun initToolbar(){
        setupActionBar(R.id.toolbar) {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscribe)

        initToolbar();

//        val broadcastId = intent.getStringExtra(BROADCAST_ID)

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
