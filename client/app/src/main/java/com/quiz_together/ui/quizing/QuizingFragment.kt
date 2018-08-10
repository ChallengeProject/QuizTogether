package com.quiz_together.ui.quizing

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import com.quiz_together.R
import com.quiz_together.data.model.AdminMsg
import com.quiz_together.data.model.AnswerMsg
import com.quiz_together.data.model.BroadcastStatus
import com.quiz_together.data.model.ChatMsg
import com.quiz_together.data.model.EndMsg
import com.quiz_together.data.model.GiftType
import com.quiz_together.data.model.QuestionMsg
import com.quiz_together.data.model.WinnersMsg
import com.quiz_together.util.SC
import com.quiz_together.util.setTouchable
import com.quiz_together.util.toast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.frag_quizing.*
import java.util.concurrent.TimeUnit


class QuizingFragment : Fragment(), QuizingContract.View {

    val TAG = "QuizingFragment#$#"
    val CAN_PICK = -1
    val ICON_IS_IMG_SATUS = -1

    val MSG_LINE_CNT_WHEN_REDUCE_WINDOW = 5

    override lateinit var presenter: QuizingContract.Presenter

    lateinit var gvAdapter : QuizingAdapter
    lateinit var rcpbController: SelectorController

    var curQuizStep = 0
    var isAlive :Boolean= false
    lateinit var quizStatus :QuizStatus
    var quizBefStatus = QuizStatus.ANSWERING
    lateinit var userMsgs : Array<String>
    var pickNum:Int = CAN_PICK // -1 is can select, but cant touch when value is more than 0

    var isExpandChatWindow = true

    var finalMsg = arrayListOf<String>()

    var disposer1:Disposable? = null
    var disposer2:Disposable? = null

    var isAdmin = false
    var lastQuestionNum = -1

    override var isActive: Boolean = false
        get() = isAdded

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val root = inflater.inflate(R.layout.frag_quizing, container, false)

        return root
    }

    override fun setLoadingIndicator(active: Boolean) {
        activity?.getWindow()?.setTouchable(active)

    }


    fun pickAnswer(num:Int) {

        if(isAdmin) return

        Log.i(TAG,"pickAnswer >> $num")
        pickNum = num
        setAnswerClickable(false)
        presenter.sendAnswer(curQuizStep,pickNum)

        if(pickNum > 0)
            rcpbController.setRCPB(pickNum, SelectorController.SelectorColor.SELECT, 100)
    }

    fun initListeners() {

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
            presenter.sendMsg(etMsg.text.toString())
        }

        rlNextStep.setOnClickListener { v ->


            if(!isAdmin) return@setOnClickListener

            rlNextStep.isClickable = false

            if(quizBefStatus == QuizStatus.ANSWERING ) {

                if(lastQuestionNum == curQuizStep)
                    presenter.openWinners()
                else
                    presenter.openQuestion(curQuizStep+1)

                quizBefStatus = QuizStatus.QUIZING
            } else if (quizBefStatus == QuizStatus.QUIZING) {
                presenter.openAnswer(curQuizStep)
                quizBefStatus = QuizStatus.ANSWERING
            } else if (quizBefStatus == QuizStatus.ENDING) {
                presenter.endBroadcast()
                "종료".toast()
            }

        }

//        etMsg.setOnFocusChangeListener { v, hasFocus ->
//            Log.i(TAG,"setOnFocusChangeListener")
//            isExpandChatWindow = !hasFocus
//            updateExpandChatWindow()
//        }

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

        initListeners()
        initQuizCalledByOncreate()

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
//        gvAdapter = QuizingAdapter(this.context!!)
//        gvAdapter.users = users
//        gvResult.adapter = gvAdapter
//        setDynamicWidth(gvResult)
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
        llNotice.visibility = View.INVISIBLE
        llQuestion.visibility = View.INVISIBLE
        llResult.visibility = View.INVISIBLE

    }

    override fun initQuizCalledByPresenter() {
        fortest()
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

        updateExpandChatWindow()
    }

    // firebase
    override fun showAdminMsg(adminMsg: AdminMsg) {
        Log.i(TAG,"showAdminMsg : ${adminMsg.toString()}")
        updateAdminMsg(adminMsg.message)
    }

    override fun showChatMsg(chatMsg: ChatMsg) {
        Log.i(TAG,"showChatMsg : ${chatMsg.toString()}")
        updateUserMsg("${chatMsg.userName} : ${chatMsg.message}")
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

        if(!isAdmin)
            setAnswerClickable(true)

        rcpbController.apply {

            setRCPB(1,SelectorController.SelectorColor.DEFAULT,0)
            setRCPB(2,SelectorController.SelectorColor.DEFAULT,0)
            setRCPB(3,SelectorController.SelectorColor.DEFAULT,0)

            tvQuestion.text = questionMsg.questionProp.title
            cleanPickNumbers()

            questionMsg.questionProp.options.let {
                setQuestions(it.get(0),it.get(1),it.get(2))
            }
        }



        startTimer(null,pickEnd,turnRestView)

    }

    override fun showAnswerView(answerMsg: AnswerMsg) {
        Log.i(TAG,"showAnswerView : ${answerMsg.toString()}")

        viewUpdate(quizStatus_ = QuizStatus.ANSWERING,
                questionNum = answerMsg.step,
                cvToolbarShow = View.GONE,
                llNoticeShow = View.INVISIBLE,
                llQuestionShow = View.VISIBLE,
                llResultShow = View.INVISIBLE,
                isExpandChatWindow_ = false)

        val pick1Cnt = answerMsg.questionStatistics.get("1") ?: 0
        val pick2Cnt = answerMsg.questionStatistics.get("2") ?: 0
        val pick3Cnt = answerMsg.questionStatistics.get("3") ?: 0

        Log.i(TAG,"showAnswerView : ${pick1Cnt} ${pick2Cnt} ${pick3Cnt}")

        val sumPick = (pick1Cnt + pick2Cnt + pick3Cnt).toDouble()

        rcpbController.apply {

            setRCPB(1,SelectorController.SelectorColor.DEFAULT,(20 + pick1Cnt / sumPick * 60).toInt())
            setRCPB(2,SelectorController.SelectorColor.DEFAULT,(20 + pick2Cnt / sumPick * 60).toInt())
            setRCPB(3,SelectorController.SelectorColor.DEFAULT,(20 + pick3Cnt / sumPick * 60).toInt())

            setRCPBOnlyColor(answerMsg.answerNo,SelectorController.SelectorColor.O)
            if(!isAdmin && answerMsg.answerNo != pickNum && pickNum != CAN_PICK) {
                setRCPBOnlyColor(pickNum,SelectorController.SelectorColor.X)
                isAlive = false
            }

            tvQuestion.text = answerMsg.questionProp.title

            answerMsg.questionProp.options.let {
                setQuestions(it.get(0),it.get(1),it.get(2))
                setNumbers(pick1Cnt.toString(),pick2Cnt.toString(),pick3Cnt.toString())
            }
        }

        startTimer(null,turnRestView,null)
    }

    override fun showWinnerView(winnersMsg: WinnersMsg) {
        Log.i(TAG,"showWinnerView : ${winnersMsg.toString()}")

        var msgTurn = 0

        finalMsg.add("") // admin msg is finalMsg[0]
        finalMsg.add("${if(winnersMsg.giftType==GiftType.PRIZE) winnersMsg.prize else winnersMsg.giftDescription}" )

        if(isAlive) {
            finalMsg.add("${winnersMsg.winnerMessage}")
        } else {
            finalMsg.add("수고하셨습니다. 담에도 파이팅!")
        }

        llNotice.visibility = View.VISIBLE
        tvAdminMsg.text = winnersMsg.winnerMessage

        disposer2 = Observable.interval(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    updateAdminMsg( finalMsg.get(msgTurn++))
                    if(msgTurn==3) msgTurn = 0
                }

        viewUpdate(quizStatus_ = QuizStatus.ENDING,
                questionNum = ICON_IS_IMG_SATUS,
                cvToolbarShow = View.GONE,
                llNoticeShow = View.VISIBLE,
                llQuestionShow = View.INVISIBLE,
                llResultShow = View.VISIBLE,
                isExpandChatWindow_ = true)

        val users = mutableListOf<Pair<String,String>>()

        var tmpStr :String? = null

        winnersMsg.userName.forEach{

            tmpStr?.run{
                users.add(Pair(it,tmpStr!!))
                tmpStr = null
            }?: run{
                tmpStr = it
            }

        }


        Log.i(TAG,"tmpStr ${tmpStr}")

        if(winnersMsg.userName.size % 2 != 0){
            Log.i(TAG,"users.size")
            users.add(Pair(tmpStr!!,""))
        }

        gvResult.numColumns = users.size

        gvAdapter = QuizingAdapter(this.context!!)
        gvAdapter.users = users
        gvResult.adapter = gvAdapter
        setDynamicWidth(gvResult)
    }

    override fun endQuiz(endMsg: EndMsg) {
        Log.i(TAG,"endQuiz : ${endMsg.toString()}")
        presenter.unsubscribeFirebase(false)
        activity?.finish()
    }

    // for showQuestionView
    val pickEnd : () -> Any = {

        Log.i(TAG,"pickEnd")

        if(!isAdmin && pickNum == CAN_PICK)
            pickAnswer(0)

    }

    val turnRestView : () -> Any = {
        Log.i(TAG,"turnRestView")

        if(isAdmin) {
            presenter.updateBroadcastStatus(BroadcastStatus.WATING)
            rlNextStep.isClickable = true
        }

        viewUpdate(quizStatus_ = QuizStatus.RESTING,
                questionNum = ICON_IS_IMG_SATUS,
                cvToolbarShow = View.VISIBLE,
                llNoticeShow = View.VISIBLE,
                llQuestionShow = View.INVISIBLE,
                llResultShow = View.INVISIBLE,
                isExpandChatWindow_ = true)
    }

    fun startTimer(doWhen5Sec:(() -> Any)? , doWhen10Sec:(()->Any)?, doWhen15Sec:(()->Any)?){

        Log.i(TAG,"startTimer start")

        disposer1 = Observable.interval(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
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
//        private val IS_ADMIN = "IS_ADMIN"
        fun newInstance(isAdmin_:Boolean, lastQuestionNum_:Int) = QuizingFragment().apply {
            isAdmin = isAdmin_
            lastQuestionNum = lastQuestionNum_
//            arguments = Bundle().apply { putBoolean(IS_ADMIN,false) }
        }
    }

    fun updateExpandChatWindow(){
        var msgRst = ""
        userMsgs
                .forEachIndexed { index, _ ->
                    if(isExpandChatWindow)
                        msgRst += userMsgs.get(index) + "\n"
                    if(!isExpandChatWindow && index >= MSG_LINE_CNT_WHEN_REDUCE_WINDOW)
                        msgRst += userMsgs.get(index) + "\n"
                }

        tvUserMsg.text = msgRst
    }

    fun updateAdminMsg(msg:String){

        tvAdminMsg.text = msg

        Observable.just(msg)
                .observeOn(AndroidSchedulers.mainThread())
                .filter{ quizStatus == QuizStatus.RESTING ||
                        quizStatus == QuizStatus.BEFORE_START||
                        quizStatus == QuizStatus.ENDING}
                .subscribe{

                    Log.i(TAG,"showAdminMsg - ${msg}")

                    llNotice.visibility = View.VISIBLE
                }
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
        ENDING(500),
    }

    fun fortest(){

        for(i in 1..10) {
            updateUserMsg("$i$i$i$i$i$i$i")
        }
    }

    override fun onPause() {
        super.onPause()

        disposer1?.dispose()
        disposer2?.dispose()

    }

}