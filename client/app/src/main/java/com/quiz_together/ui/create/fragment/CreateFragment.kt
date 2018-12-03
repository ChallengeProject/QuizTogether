package com.quiz_together.ui.create.fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import android.widget.Toast
import com.quiz_together.R
import com.quiz_together.data.Repository
import com.quiz_together.data.model.Broadcast
import com.quiz_together.data.model.GiftType
import com.quiz_together.ui.create.CreateContract
import com.quiz_together.ui.create.adapter.NumberRecyclerViewAdapter
import com.quiz_together.ui.create.adapter.QuizPagerAdapter
import com.quiz_together.ui.quizing.QuizingActivity
import com.quiz_together.util.setTouchable
import com.quiz_together.util.setVisibilityFromBoolean
import kotlinx.android.synthetic.main.frag_create.*
import kotlinx.android.synthetic.main.layout_horizontal_three_buttons.*
import kotlinx.android.synthetic.main.layout_input_room_info.*


class CreateFragment : Fragment(), CreateContract.View {

    companion object {
        const val TAG = "CreateFragment"
        const val QUESTION_NUMBER_VIEW_COLUMNS = 6
        const val RESERVED_BROADCAST = "RESERVED_BROADCAST"

        fun newInstance() = CreateFragment()
    }

    override lateinit var presenter: CreateContract.Presenter

    override var isActive: Boolean = false
        get() = isAdded

    private var openingDate: String? = null
    private var isClickedOpen = false
    private var isReservedBroadcast = false
    private val quizPagerAdapter: QuizPagerAdapter by lazy { QuizPagerAdapter(childFragmentManager) }
    private val numberRecyclerViewAdapter by lazy { NumberRecyclerViewAdapter(activity?.applicationContext!!) }

    private val onDateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                val formattedYear = year % 100
                broadcastTime.text = "$formattedYear. $month. $dayOfMonth"
                openingDate = year.toString() + month.toString() + dayOfMonth.toString()
                showTimerPicker()
            }

    private val onTimeSetListener =
            TimePickerDialog.OnTimeSetListener { _: TimePicker?, hour: Int, minute: Int ->
                broadcastTime.text = getVisualizeTimeString(hour, minute)
                openingDate += hour.toString() + minute.toString()

                if (presenter.createBroadcast(extractBroadcast())) {
                    activity?.finish()
                }
            }

    private fun showTimerPicker() {
        activity?.fragmentManager?.let { TimePickerDialogFragment.show(onTimeSetListener, it) }
    }

    private fun getVisualizeTimeString(hour: Int, minute: Int): String {
        val meridiem = if (hour < 11) "오전" else "오후"
        val formattedHour = if (hour < 11) hour else hour - 11
        return "${broadcastTime.text} $meridiem $formattedHour 시 $minute 분 시작"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_create, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initQuizInfoArea()
        initViewsSetOnClickListener()
    }

    private fun initQuizInfoArea() {
        quizNoRecyclerView.layoutManager = GridLayoutManager(activity, QUESTION_NUMBER_VIEW_COLUMNS)
        quizNoRecyclerView.adapter = numberRecyclerViewAdapter
        numberRecyclerViewAdapter.setItemClickListener { prevPosition, currentPosition ->
            val prevItemValidation = quizPagerAdapter.getQuestionValidation(prevPosition)
            numberRecyclerViewAdapter.refreshViewState(prevItemValidation, currentPosition)
            quizViewPager.currentItem = currentPosition
        }

        quizViewPager.adapter = quizPagerAdapter
        quizViewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val prevPosition = numberRecyclerViewAdapter.getCurrentIndex()
                val prevItemValidation = quizPagerAdapter.getQuestionValidation(prevPosition)
                numberRecyclerViewAdapter.refreshViewState(prevItemValidation, position)
            }
        })
        indicator.setViewPager(quizViewPager)
    }

    private fun initViewsSetOnClickListener() {
        initCreateBroadcastButtons()
        initRewardButtons()
        broadcastTime.setOnClickListener { showDatePicker() }
        roomInfoTitle.setOnLongClickListener { autoFillForTest() }
    }

    private fun initCreateBroadcastButtons() {
        val createBroadcastButtonsListener = View.OnClickListener { view ->
            when (view.id) {
                R.id.reservation -> reserveBroadcast()
                R.id.cancel -> activity?.finish()
                R.id.open -> openBroadcast()
                R.id.save -> saveBroadcast()
            }
        }

        reservation.setOnClickListener(createBroadcastButtonsListener)
        cancel.setOnClickListener(createBroadcastButtonsListener)
        open.setOnClickListener(createBroadcastButtonsListener)
        save.setOnClickListener(createBroadcastButtonsListener)
    }

    private fun initRewardButtons() {
        val rewardButtonsListener = View.OnClickListener { view ->
            when (view.id) {
                R.id.goodsButton, R.id.prizeButton -> {
                    setGiftType(if (view.id == R.id.prizeButton) GiftType.PRIZE else GiftType.GOODS)
                }
                R.id.detailPrize, R.id.detailGoods -> setGiftTypeVisibility(View.VISIBLE)
            }
        }

        prizeButton.setOnClickListener(rewardButtonsListener)
        goodsButton.setOnClickListener(rewardButtonsListener)
        detailGoods.setOnClickListener(rewardButtonsListener)
        detailPrize.setOnClickListener(rewardButtonsListener)
    }

    private fun showDatePicker() {
        DatePickerDialogFragment.newInstance(onDateSetListener)
                .show(activity?.fragmentManager, "예약 일자")
    }

    private fun reserveBroadcast() {
        val validationStr = presenter.checkValidationForBroadcast(extractBroadcast())
        if (validationStr.isNotBlank()) {
            showToast(validationStr)
            return
        }

        if (openingDate == null) {
            showDatePicker()
        } else {
            if (presenter.createBroadcast(extractBroadcast())) {
                activity?.finish()
            }
        }
    }

    private fun openBroadcast() {
        // TODO DIALOG
        isClickedOpen = true
        presenter.createBroadcast(extractBroadcast())
        // TODO DIALOG
    }

    private fun saveBroadcast() {
        if (isReservedBroadcast) {
            presenter.updateBroadcast(extractBroadcast())
        } else {
            presenter.saveQuiz(extractBroadcast())
            activity?.finish()
            showToast("작성중인 정보를 저장했습니다.")
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
        isReservedBroadcast = arguments?.getBoolean(RESERVED_BROADCAST) ?: false
        if (isReservedBroadcast) {
            broadcastTime.visibility = View.VISIBLE
            cancel.visibility = View.VISIBLE
            reservation.visibility = View.GONE
            open.setBackgroundResource(R.drawable.open_reserved_quiz)

            // load broadcast from intent
        } else {
            presenter.loadBroadcastIfHasSavedBroadcast()
        }
    }

    override fun setLoadingIndicator(active: Boolean) {
        activity?.window?.setTouchable(active)
        pb.setVisibilityFromBoolean(active)
    }

    override fun onSuccessCreatedBroadcast(broadcastId: String) {

        if (isClickedOpen) { // 방 개설 시
            showToast("방 개설")
            val intent = Intent(context, QuizingActivity::class.java)
                    .apply {
                        putExtra(QuizingActivity.BROADCAST_ID, broadcastId)
                        putExtra(QuizingActivity.IS_ADMIN, true)
                    }
            startActivity(intent)
        } else {

            showToast("방 예약 완료")
        }
        activity?.finish()
    }

    override fun loadSavedBroadcast(savedBroadcast: Broadcast?) {
        savedBroadcast?.let { it ->
            broadcastTitle.setText(it.title)
            broadcastDesc.setText(it.description)
            winnerMsg.setText(savedBroadcast.winnerMessage)
            setGiftType(savedBroadcast.giftType)
            savedBroadcast.scheduledTime?.let { openingDate = it.toString() }
            savedBroadcast.prize?.let { prize -> detailPrize.setText(prize.toString()) }
            savedBroadcast.goodsDescription?.let { desc -> detailGoods.setText(desc) }
            savedBroadcast.questionList.forEachIndexed { position, question ->
                if (question.isValidate()) {
                    numberRecyclerViewAdapter.updateState(position, isValidate = true)
                }
            }
            quizPagerAdapter.setQuestions(savedBroadcast.questionList)

            showToast("작성 중인 정보를 불러왔습니다.")
        }
    }

    override fun extractBroadcast(): Broadcast {
        val title = broadcastTitle.text.toString()
        val description = broadcastDesc.text.toString()
        val scheduledTime = openingDate?.toLong()
        val winnersMsg = winnerMsg.text.toString()
        val goodsType = getGiftType()
        val prize = if (detailPrize.text.isEmpty()) null else detailPrize.text.toString().toLong()
        val goodsDescription = detailGoods.text.toString()

        return Broadcast(null, title, description, scheduledTime, null, goodsType,
                prize, goodsDescription, Repository.getUserId(), null, winnersMsg, null,
                quizPagerAdapter.getQuestions(), 0, null)
    }

    private fun setGiftTypeVisibility(visibility: Int) {
        goodsButton.visibility = visibility
        prizeButton.visibility = visibility

        val detailViewVisibility = if (visibility == View.VISIBLE) View.GONE else View.VISIBLE
        detailGoods.visibility = detailViewVisibility
        detailPrize.visibility = detailViewVisibility
        detailGoods.setText("")
        detailPrize.setText("")

        if (visibility == View.VISIBLE) {
            goodsButton.isSelected = false
            prizeButton.isSelected = false
        }
    }

    private fun getGiftType(): GiftType {
        return when {
            goodsButton.isSelected -> GiftType.GOODS
            prizeButton.isSelected -> GiftType.PRIZE
            else -> GiftType.NONE
        }
    }

    private fun setGiftType(type: GiftType) {
        if (type == GiftType.NONE) {
            return
        }

        setGiftTypeVisibility(View.GONE)
        if (type == GiftType.GOODS) {
            detailGoods.visibility = View.VISIBLE
            goodsButton.isSelected = true
        } else {
            detailPrize.visibility = View.VISIBLE
            prizeButton.isSelected = true
        }
    }

    private fun autoFillForTest(): Boolean {
        // 방 제목
        broadcastTitle.setText("방 제목")

        // 방 설명
        broadcastDesc.setText("방 설명")

        // 승자 메세지
        winnerMsg.setText("승자 메세지")

        // 상금(상품) 설정
        setGiftType(GiftType.PRIZE)
        detailPrize.setText("10000")

        return true
    }

    fun testCreateBroadcast() {

    }

    fun testReserveBroadcast() {

    }

    fun testSaveBroadcast() {

    }

    override fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }
}
