package com.quiz_together.ui.create

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.quiz_together.data.model.Question

class QuizPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    val quizFragmentList = ArrayList<QuizInputFragment>()
    private val FRAG_NO = 12

    init {
        while (quizFragmentList.size < FRAG_NO) {
            val fragment = QuizInputFragment.newInstance()
            quizFragmentList.add(fragment)
        }
    }

    override fun getItem(position: Int): QuizInputFragment {
        return quizFragmentList[position]
    }

    override fun getCount(): Int {
        return FRAG_NO
    }

    fun extractQuestions(): ArrayList<Question>? {
        val quizList = ArrayList<Question>()

        for (quizInputFragment in quizFragmentList) {
            if (quizInputFragment.isValidatedQuiz()) {
                quizList.add(quizInputFragment.extractQuestion())
            }
        }

        return if (quizList.size == 0) null else quizList
    }
}