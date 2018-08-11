package com.quiz_together.ui.create

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.quiz_together.data.model.Question

class QuizPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    val fragmentList = ArrayList<QuizInputFragment>()
    private val FRAG_NO = 12

    init {
        while (fragmentList.size < FRAG_NO) {
            val fragment = QuizInputFragment.newInstance()
            fragmentList.add(fragment)
        }
    }

    override fun getItem(position: Int): QuizInputFragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return FRAG_NO
    }

    fun extractQuestions(): ArrayList<Question>? {
        val questionList = ArrayList<Question>()

        for (i in 1..FRAG_NO) {
            if (fragmentList[i].isCompleted()) {
                questionList.add(fragmentList[i].extractQuestion())
            } else return null
        }

        return questionList
    }
}