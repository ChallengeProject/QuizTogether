package com.quiz_together.ui.create.adapter

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.quiz_together.data.model.Question
import com.quiz_together.ui.create.fragment.QuizInputFragment
import java.util.*

class QuizPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    companion object {
        private const val NUMBER_OF_QUIZ = 12
    }

    private var questions = arrayListOf<Question>()

    init {
        for (i in 0 until NUMBER_OF_QUIZ) {
            questions.add(Question())
        }
    }

    override fun getItem(position: Int): QuizInputFragment {
        return QuizInputFragment.newInstance(questions[position]) { question -> questions[position] = question }
    }

    override fun getCount(): Int {
        return NUMBER_OF_QUIZ
    }

    fun getQuestions() = questions
    fun getQuestion(position: Int) = questions[position]
    fun setQuestions(questions: ArrayList<Question>) {
        this.questions = questions
        notifyDataSetChanged()
    }
}