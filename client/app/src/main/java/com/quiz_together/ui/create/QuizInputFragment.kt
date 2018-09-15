package com.quiz_together.ui.create

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.quiz_together.R
import com.quiz_together.data.model.CategoryType
import com.quiz_together.data.model.Question
import com.quiz_together.data.model.QuestionProp
import kotlinx.android.synthetic.main.frag_edit_quiz.*

class QuizInputFragment : Fragment(), View.OnClickListener {
    var mQuestion: Question? = null
    var mPosition: Int = -1

    companion object {

        @JvmStatic
        fun newInstance(position: Int): QuizInputFragment {
            val fragment = QuizInputFragment()
            fragment.mPosition = position
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_edit_quiz, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        cbAnswer1.setOnClickListener(this)
        cbAnswer2.setOnClickListener(this)
        cbAnswer3.setOnClickListener(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mQuestion?.let { setQuestion(it) }
    }

    private fun isEmpty(): Boolean {
        return cbAnswer1 == null || option1 == null || (getAnswerNo() < 0 && option1.text.isEmpty()
                && option2.text.isEmpty() && option3.text.isEmpty())
    }

    private fun getAnswerNo(): Int {
        return if (cbAnswer1 == null) -1 else when {
            cbAnswer1.isChecked -> 1
            cbAnswer2.isChecked -> 2
            cbAnswer3.isChecked -> 3
            else -> -1
        }
    }

    private fun setAnswerNo(answerNo: Int) {
        when (answerNo) {
            1 -> cbAnswer1.isChecked = true
            2 -> cbAnswer2.isChecked = true
            3 -> cbAnswer3.isChecked = true
            else -> return
        }
    }

    fun extractQuestion(): Question? {
        if (isEmpty()) {
            return null
        }

        val options = arrayListOf<String>()
        options.add(option1.text.toString())
        options.add(option2.text.toString())
        options.add(option3.text.toString())

        return Question(getAnswerNo(), null,
                QuestionProp(etQuizTitle.text.toString(), options), CategoryType.NORMAL)
    }

    private fun setQuestion(question: Question) {
        question.questionProp.let {
            etQuizTitle?.setText(it.title)
            option1?.setText(it.options[0])
            option2?.setText(it.options[1])
            option3?.setText(it.options[2])
        }

        setAnswerNo(question.answerNo)
    }

    override fun onClick(v: View?) {
        when (v) {
            cbAnswer1 -> {
                cbAnswer1.isChecked = true
                cbAnswer2.isChecked = false
                cbAnswer3.isChecked = false
            }
            cbAnswer2 -> {
                cbAnswer2.isChecked = true
                cbAnswer1.isChecked = false
                cbAnswer3.isChecked = false
            }
            cbAnswer3 -> {
                cbAnswer3.isChecked = true
                cbAnswer2.isChecked = false
                cbAnswer1.isChecked = false
            }
        }
    }
}