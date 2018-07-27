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
import android.os.AsyncTask
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
    val CAN_PICK = -1
    val ICON_IS_IMG_SATUS = -1

    val MSG_LINE_CNT_WHEN_REDUCE_WINDOW = 5

    override lateinit var presenter: SubscribeContract.Presenter

    lateinit var gvAdapter : SubscribeAdapter
    lateinit var rcpbController: SelectorController

    var curQuizStep = 0
    var isAlive :Boolean= false;
    lateinit var quizStatus :QuizStatus//= QuizStatus.BEFORE_START
    lateinit var userMsgs : Array<String>//("","","","","","","","","")
    var pickNum:Int = CAN_PICK // -1 is can select, but cant touch when value is more than 0

    //    var userMsgs = arrayOf("","","","","","","","","")
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


    fun pickAnswer(num:Int) {
        pickNum = num
        setAnswerClickable(false)
        presenter.sendAnswer(curQuizStep,pickNum)

        if(pickNum > 0)
            rcpbController.setRCPB(pickNum, SelectorController.SelectorColor.SELECT, 0)
    }

    fun initOnclickListeners() {

        rcpbQ1.setOnClickListener { _ ->
            if (pickNum != CAN_PICK)
                return@setOnClickListener
            pickAnswer(1)
        }
        rcpbQ2.setOnClickListener { _ ->
            if (pickNum != CAN_PICK)
                return@setOnClickListener
            pickAnswer(2)
        }
        rcpbQ3.setOnClickListener { _ ->
            if (pickNum != CAN_PICK)
                return@setOnClickListener
            pickAnswer(3)
        }

        // send msg button
        btSendMsg.setOnClickListener{



        }
    }

    fun setAnswerClickable(isClickable : Boolean) {
        rcpbQ1.isClickable = isClickable
        rcpbQ2.isClickable = isClickable
        rcpbQ3.isClickable = isClickable
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        presenter.start()

        rcpbController = SelectorController(
                arrayOf(rcpbQ1,rcpbQ2,rcpbQ3),
                arrayOf(tvQuestion1,tvQuestion2,tvQuestion3,tvCnt1,tvCnt2,tvCnt3))

        initOnclickListeners()
        initQuizCalledByOncreate()




//        //TODO this is dummy data for check ui
//        rcpbController.setRCPB(1,SelectorController.SelectorColor.O,80)
//        rcpbController.setRCPB(2,SelectorController.SelectorColor.X,50)
//        rcpbController.setRCPB(3,SelectorController.SelectorColor.SELECT,20)
//
//        rcpbController.tvQustions[1].text = "ASD"
//        rcpbController.cleanNumbers()
//        rcpbController.setQuestions("aa","bb","cc")
//        rcpbController.setNumbers("111","222","333")


//        //TODO this is dummy data for check ui
//        val users = mutableListOf<Pair<String,String>>()
//        users.add(Pair("aaaaa","bbbb"))
//        users.add(Pair("cccc","ddddd"))
//        users.add(Pair("eeeee","ffffffff"))
//        users.add(Pair("gggggggg","hhhhhh"))
//        users.add(Pair("iiiiii","jjjjjj"))
//        users.add(Pair("kkkkkkk","lllllll"))
//        users.add(Pair("mmmmmmmm","nnnnnnnnn"))
//        users.add(Pair("ooooooooo","ppppppppp"))
//        users.add(Pair("qqqq","rrrrrr"))
//        users.add(Pair("ssssss","tttttttt"))
//        users.add(Pair("uuuuuu","vvvvvvv"))
//        users.add(Pair("wwwwwww","xxxxxxx"))
//        users.add(Pair("yyyyyyyy","zzzzzz"))
//        users.add(Pair("11111111","22222222"))
//        users.add(Pair("3333333","444444"))
//
//        gvResult.numColumns = users.size
//
//        gvAdapter = SubscribeAdapter(this.context!!)
//        gvAdapter.users = users
//        gvResult.adapter = gvAdapter
//        setDynamicWidth(gvResult)
//
//        setAdminMsg("에베벱베벱ㅂ")
//        setQuizNum(4)
//        setIcon()
    }

    fun setQuizNum(num :Int) {

        curQuizStep = num

        tvQuizNum.text = "Q$num"
        tvQuizNum.visibility = View.VISIBLE
        ivIcon.visibility = View.INVISIBLE
    }

//    fun setUsersMsg(msg:String) {
//        tvUserMsg.text = msg
//    }

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


    fun initQuizCalledByOncreate() {
        isAlive = true
        quizStatus = QuizStatus.BEFORE_START
        userMsgs = arrayOf("","","","","","","","","") // "" count is nine !

        userMsgs.forEach {  updateUserMsg(it) }

        setAnswerClickable(false)

        cvToolbar.visibility = View.VISIBLE
        llNotice.visibility = View.VISIBLE
        llQuestion.visibility = View.INVISIBLE
        llResult.visibility = View.INVISIBLE

    }

    override fun initQuizCalledByPresenter() {





    }

    fun viewUpdate(quizStatus_: QuizStatus, questionNum:Int,cvToolbarShow:Int,
                   llNoticeShow:Int,llQuestionShow:Int,llResultShow:Int,isExpandChatWindow_:Boolean) {
        quizStatus = quizStatus_
        isExpandChatWindow = isExpandChatWindow_
        if(questionNum == ICON_IS_IMG_SATUS) setIcon()
        else setQuizNum(questionNum)
        cvToolbar.visibility = cvToolbarShow
        llNotice.visibility = llNoticeShow
        llQuestion.visibility = llQuestionShow
        llResult.visibility = llResultShow
    }

    // firebase
    override fun showAdminMsg(adminMsg: AdminMsg) {
        Log.i(TAG,"showAdminMsg : ${adminMsg.toString()}")

        Observable.just(adminMsg.message)
                .filter{ quizStatus != QuizStatus.RESTING &&
                        quizStatus != QuizStatus.BEFORE_START }
                .subscribe{
                    llNotice.visibility = View.VISIBLE
                    tvAdminMsg.text = adminMsg.message
                }

    }

    override fun showChatMsg(chatMsg: ChatMsg) {
        Log.i(TAG,"showChatMsg : ${chatMsg.toString()}")


    }

    override fun showQuestionView(questionMsg: QuestionMsg) {
        Log.i(TAG,"showQuestionView : ${questionMsg.toString()}")

        viewUpdate(quizStatus_ = QuizStatus.QUIZING,
                questionNum = questionMsg.step,
                cvToolbarShow = View.GONE,
                llNoticeShow = View.INVISIBLE,
                llQuestionShow = View.VISIBLE,
                llResultShow = View.INVISIBLE,
                isExpandChatWindow_ = false)

        pickNum = CAN_PICK
        setAnswerClickable(true)

        rcpbController.setRCPB(1,SelectorController.SelectorColor.DEFAULT,0)
        rcpbController.setRCPB(2,SelectorController.SelectorColor.DEFAULT,0)
        rcpbController.setRCPB(3,SelectorController.SelectorColor.DEFAULT,0)

        rcpbController.tvQustions[1].text = questionMsg.questionProp.title
        rcpbController.cleanPickNumbers()
        questionMsg.questionProp.options.let {
            rcpbController.setQuestions(it.get(0),it.get(1),it.get(2))
        }

        startTimer(null,pickEnd,turnRestView)

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


    // for showQuestionView
    val pickEnd : () -> Any = {

        Log.i(TAG,"pickEnd")
        if(pickNum == CAN_PICK)
            pickAnswer(0)

    }

    val turnRestView : () -> Any = {
        Log.i(TAG,"turnRestView")
        setIcon()
        quizStatus = QuizStatus.QUIZING
        viewUpdate(quizStatus_ = QuizStatus.QUIZING,
                questionNum = ICON_IS_IMG_SATUS,
                cvToolbarShow = View.VISIBLE,
                llNoticeShow = View.INVISIBLE,
                llQuestionShow = View.INVISIBLE,
                llResultShow = View.INVISIBLE,
                isExpandChatWindow_ = true)


    }

    fun startTimer(doWhen5Sec:(() -> Any)? , doWhen10Sec:(()->Any)?, doWhen15Sec:(()->Any)?){

        Log.i(TAG,"startTimer start")

        Observable.interval(5, TimeUnit.SECONDS)
                .take(3)
                .map { ((it+1)*5).toInt() }
                .subscribe {
                    if(it == 5) {
                        doWhen5Sec?.run { doWhen5Sec() }
                    } else if(it == 10) {
                        doWhen10Sec?.run { doWhen10Sec() }
                    } else if(it == 15) {
                        doWhen15Sec?.run { doWhen15Sec() }
                    }
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