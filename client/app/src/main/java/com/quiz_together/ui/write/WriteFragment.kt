package com.quiz_together.ui.write

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.text.method.Touch
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.quiz_together.R
import com.quiz_together.util.setTouchable
import com.quiz_together.util.setVisibilityFromBoolean
import kotlinx.android.synthetic.main.frag_write.*

class WriteFragment : Fragment(), WriteContract.View {

    val TAG = "WriteFragment"

    override lateinit var presenter: WriteContract.Presenter

    override var isActive: Boolean = false
        get() = isAdded

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.frag_write, container, false)

        return root
    }

    override fun setLoadingIndicator(active: Boolean) {
        activity?.getWindow()?.setTouchable(active)
        pb.setVisibilityFromBoolean(active)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.findViewById<FloatingActionButton>(R.id.fab)?.setOnClickListener {
            Log.i(TAG,"findViewById<FloatingActionButton>")
        }
        llNumberLoc.setOnTouchListener(QuizNumberSwipeListener(onSwipeListener))
    }

    val onSwipeListener  = { isL2R:Boolean ->

        Log.i(TAG,isL2R.toString())

    }

    companion object {
        fun newInstance() = WriteFragment()
    }

}