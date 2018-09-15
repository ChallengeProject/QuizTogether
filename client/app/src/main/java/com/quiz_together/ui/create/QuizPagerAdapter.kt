package com.quiz_together.ui.create

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.Log
import com.quiz_together.data.model.Question

class QuizPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val NUMBER_OF_QUIZ = 12

    val mQuizFragList = ArrayList<QuizInputFragment>()
    var mSavedQuestions: List<Question>? = null

    init {
        while (mQuizFragList.size < NUMBER_OF_QUIZ) {
            val fragment = QuizInputFragment.newInstance(mQuizFragList.size)
            mQuizFragList.add(fragment)
        }
    }

    override fun getItem(position: Int): QuizInputFragment {
        mSavedQuestions?.let {
            if (it.size > position) {
                mQuizFragList[position].mQuestion = it[position]
            }
        }

        Log.e("TEST", "position {$position}")
        return mQuizFragList[position]
    }

    override fun getCount(): Int {
        return NUMBER_OF_QUIZ
    }
}