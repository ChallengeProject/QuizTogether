package com.quiz_together.ui.create

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.quiz_together.R
import com.quiz_together.util.setTouchable
import com.quiz_together.util.setVisibilityFromBoolean
import kotlinx.android.synthetic.main.frag_create.*
import kotlinx.android.synthetic.main.frag_edit_quiz.*

class CreateFragment : Fragment(), CreateContract.View {

    val TAG = "CreateFragment"

    companion object {
        fun newInstance() = CreateFragment()
    }

    private val COLUMN_NO = 6

    override lateinit var presenter: CreateContract.Presenter

    override var isActive: Boolean = false
        get() = isAdded

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_create, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initQuizInfoArea()
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun setLoadingIndicator(active: Boolean) {
        activity?.getWindow()?.setTouchable(active)
        pb.setVisibilityFromBoolean(active)
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

        numberRecyclerViewAdapter.mFragmentList = (quizViewPager.adapter as QuizPagerAdapter).fragmentList
    }

    inner class QuizPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        val fragmentList = ArrayList<InputQuizFragment>()
        private val FRAG_NO = 12

        init {
            while (fragmentList.size < FRAG_NO) {
                val fragment = InputQuizFragment.newInstance()
                fragmentList.add(fragment)
            }
        }

        override fun getItem(position: Int): InputQuizFragment {
            return fragmentList[position]
        }

        override fun getCount(): Int {
            return FRAG_NO
        }

        fun extractQuestions() {

        }
    }

    class InputQuizFragment : Fragment(), View.OnClickListener {
        companion object {

            @JvmStatic
            fun newInstance(): InputQuizFragment {
                return InputQuizFragment()
            }
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.frag_edit_quiz, container, false)
        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)
            cb1.setOnClickListener(this)
            cb2.setOnClickListener(this)
            cb3.setOnClickListener(this)
        }

        fun isCompleted(): Boolean {
            return (cb1.isChecked || cb2.isChecked || cb3.isChecked) && option1.text.isNotEmpty() &&
                    option2.text.isNotEmpty() && option3.text.isNotEmpty() && etQuizTitle.text.isNotEmpty()
        }

        fun extractQuestion() {

        }

        override fun onClick(v: View?) {
            when (v) {
                cb1 -> {
                    cb1.isChecked = true
                    cb2.isChecked = false
                    cb3.isChecked = false
                }
                cb2 -> {
                    cb2.isChecked = true
                    cb1.isChecked = false
                    cb3.isChecked = false
                }
                cb3 -> {
                    cb3.isChecked = true
                    cb2.isChecked = false
                    cb1.isChecked = false
                }
            }
        }
    }
}
