package com.quiz_together.ui.subscribe

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar
import com.quiz_together.R
import com.quiz_together.data.model.User
import com.quiz_together.data.model.UserRes
import com.quiz_together.util.setTouchable
import com.quiz_together.util.setVisibilityFromBoolean
import kotlinx.android.synthetic.main.frag_subscribe.*
import android.widget.GridView
import android.text.Html
import android.R.attr.name





class SubscribeFragment : Fragment(), SubscribeContract.View {

    val TAG = "SubscribeFragment"

    override lateinit var presenter: SubscribeContract.Presenter

    lateinit var gvAdapter : SubscribeAdapter
    lateinit var rcpbController: SelectorController


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
        setHasOptionsMenu(true)
        val root = inflater.inflate(R.layout.frag_subscribe, container, false)

        return root
    }

    override fun setLoadingIndicator(active: Boolean) {
        activity?.getWindow()?.setTouchable(active)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        cvToolbar.visibility = View.VISIBLE
        llNotice.visibility = View.INVISIBLE
        llQuestion.visibility = View.VISIBLE
        llResult.visibility = View.INVISIBLE



        rcpbController = SelectorController(
                arrayOf(rcpbQ1,rcpbQ2,rcpbQ3),
                arrayOf(tvQuestion1,tvQuestion2,tvQuestion3,tvCnt1,tvCnt2,tvCnt3))


        //TODO this is dummy data for check ui
        rcpbController.setRCPB(1,SelectorController.SelectorColor.O,80)
        rcpbController.setRCPB(2,SelectorController.SelectorColor.X,50)
        rcpbController.setRCPB(3,SelectorController.SelectorColor.SELECT,20)

        rcpbController.tvQustions[1].text = "ASD"
        rcpbController.cleanNumbers()
        rcpbController.setQuestions("aa","bb","cc")
        rcpbController.setNumbers("111","222","333")

        val sourceString = "<b>aaaa</b><br>" +
                "bbbbb<br>" +
                "ccccc<br>" +
                "ddddd"
        tvUserMsg.setText(Html.fromHtml(sourceString))

        //TODO this is dummy data for check ui
        val users = mutableListOf<Pair<String,String>>()
        users.add(Pair("aaaaa","bbbb"))
        users.add(Pair("cccc","ddddd"))
        users.add(Pair("eeeee","ffffffff"))
        users.add(Pair("gggggggg","hhhhhh"))
        users.add(Pair("iiiiii","jjjjjj"))
        users.add(Pair("kkkkkkk","lllllll"))
        users.add(Pair("mmmmmmmm","nnnnnnnnn"))
        users.add(Pair("ooooooooo","ppppppppp"))
        users.add(Pair("qqqq","rrrrrr"))
        users.add(Pair("ssssss","tttttttt"))
        users.add(Pair("uuuuuu","vvvvvvv"))
        users.add(Pair("wwwwwww","xxxxxxx"))
        users.add(Pair("yyyyyyyy","zzzzzz"))
        users.add(Pair("11111111","22222222"))
        users.add(Pair("3333333","444444"))

        gvResult.numColumns = users.size

        gvAdapter = SubscribeAdapter(this.context!!)
        gvAdapter.users = users
        gvResult.adapter = gvAdapter
        setDynamicWidth(gvResult)

    }

    private fun setDynamicWidth(gridView: GridView) {
        val gridViewAdapter = gridView.adapter ?: return
        var totalWidth: Int
        val items = gridViewAdapter.count
        val listItem = gridViewAdapter.getView(0, null, gridView)
        listItem.measure(0, 0)
        totalWidth = listItem.measuredWidth
        totalWidth = totalWidth * items
        val params = gridView.layoutParams
        params.width = totalWidth
        gridView.layoutParams = params
    }

    companion object {
        fun newInstance() = SubscribeFragment()
    }

}