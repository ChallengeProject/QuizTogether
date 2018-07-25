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
import com.quiz_together.data.model.AdminMsg
import com.quiz_together.data.model.AnswerMsg
import com.quiz_together.data.model.ChatMsg
import com.quiz_together.data.model.EndMsg
import com.quiz_together.data.model.QuestionMsg
import com.quiz_together.data.model.WinnersMsg
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit


class SubscribeFragment : Fragment(), SubscribeContract.View {

    val TAG = "SubscribeFragment#$#"

    val MSG_LINE_CNT_WHEN_REDUCE_WINDOW = 5

    override lateinit var presenter: SubscribeContract.Presenter

    lateinit var gvAdapter : SubscribeAdapter
    lateinit var rcpbController: SelectorController

    var isAlive = false;
    var quizStatus = QuizStatus.BEFORE_START
    var userMsgs = arrayOf("","","","","","","","","")
    var isExpandChatWindow = true

    override var isActive: Boolean = false
        get() = isAdded

    override fun onResume() {
        super.onResume()
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

        presenter.start()


//        cvToolbar.visibility = View.VISIBLE
        llNotice.visibility = View.VISIBLE
        llQuestion.visibility = View.INVISIBLE
        llResult.visibility = View.INVISIBLE


        for ( i in 0 .. 10) {
            updateUserMsg("$i$i$i$i$i")
        }

        btSendMsg.setOnClickListener{



        }

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

        setAdminMsg("에베벱베벱ㅂ")
//        setQuizNum(4)
//        setIcon()
    }

    fun setAdminMsg(msg:String) {
        tvAdminMsg.text = msg
    }

    fun setQuizNum(num :Int) {
        tvQuizNum.text = "Q$num"
        tvQuizNum.visibility = View.VISIBLE
        ivIcon.visibility = View.INVISIBLE
    }

    fun setUsersMsg(msg:String) {
        tvUserMsg.text = msg
    }

    // TODO first, use dummy data
    fun setIcon() {
        ivIcon.setImageDrawable(context!!.getDrawable(R.drawable.ic_dummy))
        ivIcon.visibility = View.VISIBLE
        tvQuizNum.visibility = View.INVISIBLE
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

    /**
     * start when success to subscribe firebase topic
     */
    override fun initQuiz() {
        isAlive = true

        startTimer()
    }

    // firebase
    override fun showAdminMsg(adminMsg: AdminMsg) {
        Log.i(TAG,"showAdminMsg : ${adminMsg.toString()}")


    }

    override fun showChatMsg(chatMsg: ChatMsg) {
        Log.i(TAG,"showChatMsg : ${chatMsg.toString()}")


    }

    override fun showQuestionView(questionMsg: QuestionMsg) {
        Log.i(TAG,"showQuestionView : ${questionMsg.toString()}")


    }

    override fun showAnswerView(answerMsg: AnswerMsg) {
        Log.i(TAG,"showAnswerView : ${answerMsg.toString()}")


    }

    override fun showWinnerView(winnersMsg: WinnersMsg) {
        Log.i(TAG,"showWinnerView : ${winnersMsg.toString()}")


    }

    override fun endQuiz(endMsg: EndMsg) {
        Log.i(TAG,"endQuiz : ${endMsg.toString()}")


    }

    fun startTimer() {

        Observable.interval(5, TimeUnit.SECONDS)
                .take(3)
                .map { (it+1)*5 }
                .subscribe {

                }

    }

    companion object {
        fun newInstance() = SubscribeFragment()
    }

    fun updateUserMsg(msg:String) {
        var msgRst = ""
        userMsgs
                .filterIndexed { index, _ ->index != userMsgs.size - 1 }
                .forEachIndexed { index, _ ->
                    userMsgs.set(index,userMsgs.get(index+1))

                    if(isExpandChatWindow)
                        msgRst += userMsgs.get(index) + "\n"
                    if(!isExpandChatWindow && index >= MSG_LINE_CNT_WHEN_REDUCE_WINDOW)
                        msgRst += userMsgs.get(index) + "\n"
                }

        userMsgs.set(userMsgs.size - 1 , msg)
        msgRst += userMsgs.last()

        tvUserMsg.text = msgRst
    }

    enum class QuizStatus(val value:Int) {
        BEFORE_START(100),
        RESTING(200),
        QUIZING(300),
        ANSWERING(400),
    }

}