package com.quiz_together.ui.main.event

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.quiz_together.R
import com.quiz_together.ui.quizing.QuizingActivity
import com.quiz_together.util.SC
import kotlinx.android.synthetic.main.fragm_event.*

class EventFragment : Fragment(), EventContract.View {

    private val TAG = "EventFragment#$#"
    private lateinit var eventPresenter : EventPresenter



    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.frag_event_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menu_setting -> {
                Log.i(TAG, "menu_setting")
            }
        }
        return true
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater?.inflate(R.layout.fragm_event, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        initView()

    }



    private fun initView() {
        eventPresenter = EventPresenter(this@EventFragment, pb)

        tmpEtBroadcastId.setText("b6c49ed11e370dc6b7d55f3dfa7f1cf2d")
        tmpEtUserId.setText("ue43a7a1a934ed910fe157639ad484e71")

        tmpBtTest.setOnClickListener { v ->

            val intent = Intent(activity?.applicationContext , QuizingActivity::class.java)
            intent.putExtra(QuizingActivity.BROADCAST_ID,tmpEtBroadcastId.text.toString())
//            intent.putExtra(QuizingActivity.LAST_QUESTION_NUM,tmpEtLastQuestionNum.text.toString().toInt())
            intent.putExtra(QuizingActivity.IS_ADMIN, tmpCbIsadmin.isChecked)
            SC.USER_ID = tmpEtUserId.text.toString()
            // greate

            startActivity(intent)

        }

    }



}