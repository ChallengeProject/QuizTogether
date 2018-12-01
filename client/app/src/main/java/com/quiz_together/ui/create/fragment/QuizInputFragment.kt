package com.quiz_together.ui.create.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.quiz_together.R
import com.quiz_together.data.model.Question
import kotlinx.android.synthetic.main.frag_input_question.*
import kotlinx.android.synthetic.main.layout_question_option.view.*

class QuizInputFragment : Fragment() {
    private var question: Question? = null
    private var updateQuestions: ((Question) -> Unit)? = null

    companion object {

        @JvmStatic
        fun newInstance(question: Question, updateQuestions: (Question) -> Unit): QuizInputFragment {
            val fragment = QuizInputFragment()
            fragment.question = question
            fragment.updateQuestions = updateQuestions
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_input_question, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setAnswerCheckBoxClickListener()
        setEditTextChangedListener(quizTitle) { s -> question?.questionProp?.title = s }
        for (i in 0..2) {
            setEditTextChangedListener(optionView1.option) { s ->
                question?.questionProp?.options?.let {
                    val option = it.getOrNull(i)
                    if (option == null) {
                        it.add(s)
                    } else {
                        it[i] = s
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        question?.let { setQuestion(it) }
    }

    fun getQuestion(): Question? {
        return question
    }

    private fun setQuestion(question: Question) {
        with(question.questionProp) {
            setTextViewText(quizTitle, title)
            for (i in 0..2) {
                options.getOrNull(i)?.let {
                    when (i) {
                        0 -> setTextViewText(optionView1.option, it)
                        1 -> setTextViewText(optionView2.option, options[1])
                        2 -> setTextViewText(optionView3.option, options[2])
                    }
                }
            }
        }
        setAnswerView(question.answerNo)
    }

    private fun setTextViewText(tv: TextView?, str: String) {
        if (!str.isEmpty()) {
            tv?.text = str
        }
    }

    private fun updateUpToDateQuestion() {
        question?.let { updateQuestions?.invoke(it) }
    }

    private fun setEditTextChangedListener(editText: EditText, action: (String) -> Unit) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                action.invoke(s.toString())
                updateUpToDateQuestion()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setAnswerCheckBoxClickListener() {
        val answerSelectedListener = View.OnClickListener { view ->
            question?.let {
                when (view) {
                    optionView1.answer -> it.answerNo = 1
                    optionView2.answer -> it.answerNo = 2
                    optionView3.answer -> it.answerNo = 3
                }

                setAnswerView(it.answerNo)
                updateUpToDateQuestion()
            }
        }

        optionView1.answer.setOnClickListener(answerSelectedListener)
        optionView2.answer.setOnClickListener(answerSelectedListener)
        optionView3.answer.setOnClickListener(answerSelectedListener)
    }

    private fun setAnswerView(answerNo: Int) {
        when (answerNo) {
            1 -> {
                optionView1.answer.isChecked = true
                optionView2.answer.isChecked = false
                optionView3.answer.isChecked = false
            }
            2 -> {
                optionView1.answer.isChecked = false
                optionView2.answer.isChecked = true
                optionView3.answer.isChecked = false
            }
            3 -> {
                optionView1.answer.isChecked = false
                optionView2.answer.isChecked = false
                optionView3.answer.isChecked = true
            }
            else -> return
        }
    }
}