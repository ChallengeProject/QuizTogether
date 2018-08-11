package com.quiz_together.ui.main.home

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.quiz_together.App
import com.quiz_together.R
import com.quiz_together.data.Repository
import com.quiz_together.data.model.Broadcast
import com.quiz_together.data.model.ResGetPagingBroadcastList
import com.quiz_together.data.model.RoomOutputType
import com.quiz_together.ui.create.CreateActivity
import com.quiz_together.ui.quizing.QuizingActivity
import com.quiz_together.util.SC
import com.quiz_together.util.setTouchable
import kotlinx.android.synthetic.main.fragm_home.*

class HomeFragment : Fragment(), HomeContract.View {

    private val TAG = "HomeFragment#$#"
    private lateinit var presenter : HomePresenter

    override var isActive: Boolean = false
        get() = isAdded

    private val broadcastAdapter: BroadcastAdapter by lazy {
        BroadcastAdapter(activity?.applicationContext, {

            val intent = Intent(activity?.applicationContext , QuizingActivity::class.java)
            intent.putExtra(QuizingActivity.BROADCAST_ID,it.broadcastId)
            intent.putExtra(QuizingActivity.LAST_QUESTION_NUM, it.questionCount)
            intent.putExtra(QuizingActivity.IS_ADMIN, if(it.roomOutputType == RoomOutputType.RESERVATION) true else false)
            startActivity(intent)

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.frag_home_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menu_create -> {
                val intent = Intent(activity?.applicationContext,CreateActivity::class.java)

                startActivity(intent)
            }
        }
        return true
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater?.inflate(R.layout.fragm_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        initView()

        rvBroadcasts.run {
            adapter = broadcastAdapter
            layoutManager = LinearLayoutManager(activity?.applicationContext)
        }

        ssrl.run{

            scrollUpChild = rvBroadcasts
            setOnRefreshListener { presenter.loadBroadcasts() }

        }

    }

    override fun onResume() {
        super.onResume()

        setLoadingIndicator(true)

        presenter.start()
    }


    private fun initView() {
        presenter = HomePresenter(this@HomeFragment, pb,Repository)

    }


    override fun setLoadingIndicator(active: Boolean) {
        activity?.getWindow()?.setTouchable(active)
        ssrl.isRefreshing = active
    }

    override fun showBroadcasts(resGetPagingBroadcastList: ResGetPagingBroadcastList) {
        broadcastAdapter.run {

            clearItem()
            resGetPagingBroadcastList.myBroadcastList?.forEach { addItem(it, RoomOutputType.RESERVATION) }
            resGetPagingBroadcastList.currentBroadcastList?.forEach { addItem(it, RoomOutputType.DEFAULT) }
            notifyDataSetChang()
        }
    }



}