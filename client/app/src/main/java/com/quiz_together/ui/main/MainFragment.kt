package com.quiz_together.ui.main

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.quiz_together.App
import com.quiz_together.R
import com.quiz_together.data.model.Broadcast
import com.quiz_together.util.setTouchable
import com.quiz_together.util.toast
import kotlinx.android.synthetic.main.frag_main.*

class MainFragment : Fragment(), MainContract.View {


    val TAG = "MainFragment"

    override lateinit var presenter: MainContract.Presenter

    override var isActive: Boolean = false
        get() = isAdded

    private val mainAdapter: MainAdapter by lazy {
        MainAdapter(activity?.applicationContext, {
            Toast.makeText(App.instance, "get recycler view data -> ${it}" , Toast.LENGTH_LONG).show()
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
        val root = inflater.inflate(R.layout.frag_main, container, false)

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
            //            presenter.editTask()
        }


        rvBroadcasts.run {
            adapter = mainAdapter
            layoutManager = LinearLayoutManager(activity?.applicationContext)
        }

        ssrl.run{

            scrollUpChild = rvBroadcasts
            setOnRefreshListener { presenter.loadBroadcastTask() }

        }
    }

    override fun showBroadcasts(broadcasts: List<Broadcast>) {

        mainAdapter.run {

            clearItem()
            broadcasts.forEach { addItem(it) }
            notifyDataSetChang()
        }

    }

    override fun showErrorMsg() {
        "can't get broadcasts list".toast()
    }

    companion object {
        fun newInstance() = MainFragment()
    }

}