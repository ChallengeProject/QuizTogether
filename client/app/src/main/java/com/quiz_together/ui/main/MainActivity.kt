package com.quiz_together.ui.main

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.quiz_together.R
import com.quiz_together.ui.create.CreateActivity
import com.quiz_together.ui.main.event.EventFragment
import com.quiz_together.ui.main.home.HomeFragment
import com.quiz_together.ui.main.profile.ProfileFragment
import com.quiz_together.ui.main.search.SearchFragment
import com.quiz_together.ui.quizing.QuizingActivity
import com.quiz_together.util.SC
import com.quiz_together.util.disableShiftMode
import com.quiz_together.util.replace
import com.quiz_together.util.setupActionBar
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity#$#"
    private val FINISH_INTERVAL_TIME: Long = 2000
    private var backPressedTime: Long = 0

    private val eventFragment: EventFragment by lazy {
        EventFragment()
    }
    private val homeFragment: HomeFragment by lazy {
        HomeFragment()
    }
    private val profileFragment: ProfileFragment by lazy {
        ProfileFragment()
    }
    private val searchFragment: SearchFragment by lazy {
        SearchFragment()
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.menu_bot_nav_1 -> {
                replace(R.id.fl_content, homeFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_bot_nav_2 -> {
                replace(R.id.fl_content, searchFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_bot_nav_3 -> {
                replace(R.id.fl_content, eventFragment)
                return@OnNavigationItemSelectedListener true
            }

            R.id.menu_bot_nav_4 -> {
                replace(R.id.fl_content, profileFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun initToolbar(){
        setupActionBar(R.id.toolbar) {
            setDisplayShowHomeEnabled(true)
            setTitle("퀴즈홈")
        }
    }

    private fun initListener(){

        ibCreate.setOnClickListener {
            val intent = Intent(applicationContext, CreateActivity::class.java)
            startActivity(intent)
        }

        ibSetting.setOnClickListener {
            Log.i(TAG,"ibSetting.setOnClickListener")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        replace(R.id.fl_content, homeFragment)
        initToolbar()
        initListener()
        bnv.disableShiftMode()
        bnv.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }

    override fun onResume() {
        super.onResume()

        // get broadcast from firebase for start broadcast
        var broadcastId = intent.getStringExtra(BROADCAST_ID)
        var userId = intent.getStringExtra(USER_ID)

        broadcastId?.run {

            val intent = Intent(applicationContext, QuizingActivity::class.java)
            intent.putExtra(QuizingActivity.BROADCAST_ID, this)
            if (userId != SC.USER_ID)
                intent.putExtra(QuizingActivity.IS_ADMIN, false)
            else
                return@run
            startActivity(intent)
        }

    }

    override fun onBackPressed() {
        val tempTime = System.currentTimeMillis()
        val intervalTime = tempTime - backPressedTime

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed()
        } else {
            backPressedTime = tempTime
            Toast.makeText(applicationContext, "한번 더 뒤로가기 누르면 앱이 종료됩니다", Toast.LENGTH_SHORT).show()
        }

    }


    companion object {
        const val BROADCAST_ID = "BROADCAST_ID"
        const val USER_ID = "USER_ID"
    }

}
