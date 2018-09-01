package com.quiz_together.ui.create

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.quiz_together.R
import com.quiz_together.data.model.CategoryType
import com.quiz_together.data.model.Question
import com.quiz_together.data.model.QuestionProp
import kotlinx.android.synthetic.main.frag_create.*
import kotlinx.android.synthetic.main.frag_edit_quiz.*

class QuizInputFragment : Fragment(), View.OnClickListener {
    companion object {

        @JvmStatic
        fun newInstance(): QuizInputFragment {
            return QuizInputFragment()
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

    fun isValidatedQuiz(): Boolean {
        if (isEmpty()) {
            return false
        }

        val toastMsg = when {
            option1.text.isEmpty() -> "보기1을 입력하지 않았습니다"
            option2.text.isEmpty() -> "보기2을 입력하지 않았습니다"
            option3.text.isEmpty() -> "보기3을 입력하지 않았습니다"
            getAnswerNo() < 0 -> "정답을 선택하지 않았습니다"
            else -> null
        }

        return if (toastMsg == null) true else {
            Toast.makeText(context, toastMsg, Toast.LENGTH_LONG).show()
            false
        }
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
        }
    }

    fun extractQuestion(): Question {
        val options = arrayListOf<String>()
        options.add(option1.text.toString())
        options.add(option2.text.toString())
        options.add(option3.text.toString())

        return Question(getAnswerNo(), null,
                QuestionProp(etQuizTitle.text.toString(), options), CategoryType.NORMAL)
    }

    fun setQuestion(question: Question) {
        etTtile.setText(question.questionProp.title)
        option1.setText(question.questionProp.options[0])
        option2.setText(question.questionProp.options[1])
        option3.setText(question.questionProp.options[2])

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