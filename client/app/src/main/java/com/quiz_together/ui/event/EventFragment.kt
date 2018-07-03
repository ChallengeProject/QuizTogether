package com.quiz_together.ui.event

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.frag_event.*
import com.quiz_together.App
import com.quiz_together.R
import com.quiz_together.R.id.pb
import com.quiz_together.R.id.rvEvents
import com.quiz_together.data.model.Event
import com.quiz_together.data.model.Events
import com.quiz_together.ui.setting.SettingActivity
import com.quiz_together.util.setTouchable

class EventFragment : Fragment(), EventContract.View {

    val TAG = "EventFragment"

    override lateinit var presenter: EventContract.Presenter

    override var isActive: Boolean = false
        get() = isAdded

    private val eventAdapter: EventAdapter by lazy {
        EventAdapter(activity?.applicationContext, {
            Toast.makeText(App.instance, "get recycler view data -> ${it}" ,Toast.LENGTH_LONG).show()
        })
    }

    override fun onResume() {
        super.onResume()

        setLoadingIndicator(true)

        presenter.start()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.frag_event, container, false)

        setHasOptionsMenu(true)

        return root
    }

    override fun setLoadingIndicator(active: Boolean) {
        activity?.getWindow()?.setTouchable(active)
        ssrl.isRefreshing = active
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.findViewById<FloatingActionButton>(R.id.fab)?.setOnClickListener {
            setLoadingIndicator(true)
            presenter.loadEvents()
        }

        rvEvents.run {
            adapter = eventAdapter
            layoutManager = LinearLayoutManager(activity?.applicationContext)
        }

        ssrl.run{

            scrollUpChild = rvEvents
            setOnRefreshListener { presenter.loadEvents() }

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.frag_event_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_setting -> startActivity(Intent(activity?.applicationContext,SettingActivity::class.java))
        }
        return true
    }

    override fun showEvents(events: List<Event>) {

        eventAdapter.run {

            clearItem()
            events.forEach { addItem(it) }
            notifyDataSetChang()
        }

    }

    companion object {
        private val ARGUMENT_ID = "ID"

        fun newInstance(id:String?) = EventFragment().apply {
            arguments = Bundle().apply { putString(ARGUMENT_ID,id) }
        }
    }

}