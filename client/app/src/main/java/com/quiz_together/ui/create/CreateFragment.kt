package com.quiz_together.ui.create

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
import com.quiz_together.ui.quizing.QuizingActivity
import com.quiz_together.util.setTouchable
import com.quiz_together.util.setVisibilityFromBoolean
import kotlinx.android.synthetic.main.frag_create.*


class CreateFragment : Fragment(), CreateContract.View, View.OnClickListener {

    val TAG = "CreateFragment"
    override lateinit var presenter: CreateContract.Presenter
    override var isActive: Boolean = false
        get() = isAdded

    private val COLUMN_NO = 6
    private var mOpeningDate: String? = null
    private var mIsReservedBroadcast = false
    private var mIsClickedReservation = false

    companion object {
        @JvmStatic
        val RESERVED_BROADCAST = "RESERVED_BROADCAST"

        fun newInstance() = CreateFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_create, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initQuizInfoArea()
        initViewsSetOnClickListener()

        mIsReservedBroadcast = arguments?.getBoolean(RESERVED_BROADCAST) ?: false
        if (mIsReservedBroadcast) {
            tvTimeSetting.visibility = View.VISIBLE
            cancel.visibility = View.VISIBLE
            reservation.visibility = View.GONE
            open.setBackgroundResource(R.drawable.open_reserved_quiz)

            // load broadcast from intent
        } else {
            presenter.loadQuizIfHasSavedQuiz()
        }
    }

    private fun initQuizInfoArea() {
        val numberRecyclerViewAdapter = NumberRecyclerViewAdapter(activity?.applicationContext!!)
        numberRecyclerViewAdapter.setItemClickListener {
            quizViewPager.currentItem = it
            numberRecyclerViewAdapter.setCurrentItem(it)
        }

        rvQuizNumbers.layoutManager = GridLayoutManager(activity, COLUMN_NO)
        rvQuizNumbers.adapter = numberRecyclerViewAdapter

        quizViewPager.adapter = QuizPagerAdapter(childFragmentManager)
        indicator.setViewPager(quizViewPager)
        quizViewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                numberRecyclerViewAdapter.setCurrentItem(position)
            }
        })

        numberRecyclerViewAdapter.mFragmentList = (quizViewPager.adapter as QuizPagerAdapter).quizFragmentList
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun setLoadingIndicator(active: Boolean) {
        activity?.window?.setTouchable(active)
        pb.setVisibilityFromBoolean(active)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btGoods, R.id.btPrize -> {
                setGiftType(if (v.id == R.id.btGoods) GiftType.GIFT else GiftType.PRIZE)
            }

            R.id.etPrize, R.id.etGoodsDescription -> {
                etGoodsDescription.visibility = View.GONE
                etPrize.visibility = View.GONE
                setGiftTypeVisibility(View.VISIBLE)
            }

            R.id.tvTimeSetting -> showDatePicker()

            R.id.reservation -> {
                if (isValidatedBroadcast()) {
                    showDatePicker()
                    mIsClickedReservation = true
                    Toast.makeText(context, "방 예약 완료", Toast.LENGTH_LONG).show()
                    activity?.finish()
                } else {
                    // Toast
                }
            }

            R.id.cancel -> {
                activity?.finish()
            }

            R.id.open -> {
                // dialog
                presenter.requestCreateBroadcast()
                // d
            }

            R.id.save -> {
                if (mIsReservedBroadcast) {
                    // update Broadcast api
                } else {
                    presenter.saveQuiz()
                }
                activity?.finish()
            }

        }
    }

    override fun onSuccessCreatedBroadcast(broadcastId: String) {
        Toast.makeText(context, "방 개설", Toast.LENGTH_LONG).show()

        if (!mIsClickedReservation) { // 방 개설 시
            val intent = Intent(context, QuizingActivity::class.java)
            intent.putExtra(QuizingActivity.BROADCAST_ID, broadcastId)
            intent.putExtra(QuizingActivity.IS_ADMIN, true)

            startActivity(intent)
        }
        activity?.finish()
    }

    override fun onErrorCreatedBroadcast() {
        Toast.makeText(context, "방 개설 실패", Toast.LENGTH_LONG).show()
    }

    override fun isValidatedBroadcast(): Boolean {
        if (etTtile.text.isEmpty()) {
            Toast.makeText(context, "방 제목이 입력되지 않았습니다.", Toast.LENGTH_LONG).show()
            return false
        }

        val giftType = getGiftType()
        if (giftType == null) {
            Toast.makeText(context, "Gift Type 을 설정해 주세요.", Toast.LENGTH_LONG).show()
            return false
        } else if (giftType == GiftType.GIFT && etGoodsDescription.text.toString().isEmpty()) {
            Toast.makeText(context, "상품을 입력해 주세요", Toast.LENGTH_LONG).show()
            return false
        } else if (giftType == GiftType.PRIZE && etPrize.text.toString() == "") {
            Toast.makeText(context, "상금을 입력해 주세요.", Toast.LENGTH_LONG).show()
            return false
        } else if (etWinnerMessage.text.toString().isEmpty()) {
            Toast.makeText(context, "우승자 메시지를 입력해 주세요.", Toast.LENGTH_LONG).show()
            return false
        }

        val quizList = (quizViewPager.adapter as QuizPagerAdapter).extractQuestions()
        if (quizList == null) {
            Toast.makeText(context, "입력된 퀴즈가 없습니다.", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    override fun extractBroadcast(): Broadcast {
        val title = etTtile.text.toString()
        val description = etDescription.text.toString()
        val scheduledTime = mOpeningDate?.toLong()
        val giftType = getGiftType()
        val prize = if (etPrize.text.toString() == "") null else etPrize.text.toString().toLong()
        val giftDescription = etGoodsDescription.text.toString()
        val winnersMsg = etWinnerMessage.text.toString()

        val questionList = (quizViewPager.adapter as QuizPagerAdapter).extractQuestions()!!
        val questionCount = questionList.count()

        return Broadcast(null, title, description, scheduledTime, null, giftType!!,
                prize, giftDescription, Repository.getUserId(), null, winnersMsg, null,
                questionList, questionCount, null)
    }

    private fun setGiftTypeVisibility(visibility: Int) {
        btGoods.visibility = visibility
        btPrize.visibility = visibility
        etGoodsDescription.setText("")
        etPrize.setText("")

        if (visibility == View.VISIBLE) {
            btGoods.isSelected = false
            btPrize.isSelected = false
        }
    }

    private fun setGiftType(type: GiftType) {
        if (type == GiftType.GIFT) {
            etGoodsDescription.visibility = View.VISIBLE
            btGoods.isSelected = true
        } else {
            etPrize.visibility = View.VISIBLE
            btPrize.isSelected = true
        }
        setGiftTypeVisibility(View.GONE)
    }

    private fun getGiftType(): GiftType? {
        return when {
            btGoods.isSelected -> GiftType.GIFT
            btPrize.isSelected -> GiftType.PRIZE
            else -> null
        }
    }

    override fun loadQuiz(savedBroadcast: Broadcast?) {
        savedBroadcast?.let {
            savedBroadcast.title.let { etTtile.setText(it) }
            savedBroadcast.description.let { etDescription.setText(it) }
            mOpeningDate = savedBroadcast.scheduledTime?.toString()
            setGiftType(savedBroadcast.giftType)
            etPrize.setText(savedBroadcast.prize.toString())
            etGoodsDescription.setText(savedBroadcast.giftDescription)
            etWinnerMessage.setText(savedBroadcast.winnerMessage)

            val questionList = savedBroadcast.questionList
            (quizViewPager.adapter as QuizPagerAdapter).quizFragmentList.forEachIndexed { index, inputQuizFragment ->
                inputQuizFragment.setQuestion(questionList[index])
            }
        }
    }

    private fun showDatePicker() {
        val datePicker = DatePickerDialogFragment()
        datePicker.setOnTimeSetListener(mOnDateSetListener)
        datePicker.show(activity?.fragmentManager, "datePicker")
    }

    private fun showTimerPicker() {
        val timePicker = TimePickerDialogFragment()
        timePicker.setOnTimeSetListener(mOnTimeSetListener)
        timePicker.show(activity?.fragmentManager, "timePicker")
    }

    private val mOnDateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                val formattedYear = year % 100
                tvTimeSetting.text = "$formattedYear. $month. $dayOfMonth"
                mOpeningDate = year.toString() + month.toString() + dayOfMonth.toString()

                showTimerPicker()
            }

    private val mOnTimeSetListener =
            TimePickerDialog.OnTimeSetListener { view: TimePicker?, hourOfDay: Int, minute: Int ->
                val meridiem = if (hourOfDay < 11) "오전" else "오후"
                val formattedHour = if (hourOfDay < 11) hourOfDay else hourOfDay - 11

                tvTimeSetting.text = "${tvTimeSetting.text} $meridiem $formattedHour 시 $minute 분 시작"
                mOpeningDate += hourOfDay.toString() + minute.toString()

                if (!this.mIsReservedBroadcast) {
                    presenter.requestCreateBroadcast()
                }
            }

    private fun initViewsSetOnClickListener() {
        tvTimeSetting.setOnClickListener(this)
        reservation.setOnClickListener(this)
        cancel.setOnClickListener(this)
        open.setOnClickListener(this)
        save.setOnClickListener(this)
        btPrize.setOnClickListener(this)
        btGoods.setOnClickListener(this)
        etGoodsDescription.setOnClickListener(this)
        etPrize.setOnClickListener(this)
    }
}
