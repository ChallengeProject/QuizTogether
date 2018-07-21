package com.quiz_together.ui.main.search

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.quiz_together.R
import kotlinx.android.synthetic.main.fragm_search.*

class SearchFragment : Fragment(), SearchContract.View {

    private val TAG = "SearchFragment#$#"
    private lateinit var searchPresenter : SearchPresenter





    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater?.inflate(R.layout.fragm_search, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        initView()

    }



    private fun initView() {
        searchPresenter = SearchPresenter(this@SearchFragment, pb)

    }



}
