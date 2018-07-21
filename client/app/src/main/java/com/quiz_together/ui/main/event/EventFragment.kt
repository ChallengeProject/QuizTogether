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

    }



}