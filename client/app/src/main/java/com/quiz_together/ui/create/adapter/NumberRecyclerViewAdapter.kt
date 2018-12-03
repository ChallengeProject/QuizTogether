package com.quiz_together.ui.create.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.quiz_together.R
import kotlinx.android.synthetic.main.item_number.view.*

class NumberRecyclerViewAdapter(context: Context) : RecyclerView.Adapter<NumberRecyclerViewAdapter.ViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val states = ArrayList<NumberHolderState>()
    private val numbers = (1..12).toList()
    private var currentIdx = 0

    lateinit var mItemClickListener: ItemClickListener

    enum class NumberHolderState { DEFAULT, COMPLETED, FOCUSING;

        companion object {
            @JvmStatic
            fun getBackgroundResource(state: NumberHolderState): Int {
                return when (state) {
                    DEFAULT -> R.drawable.quiz_number_view_state_default
                    COMPLETED -> R.drawable.quiz_number_view_state_completed
                    FOCUSING -> R.drawable.quiz_number_view_state_focusing
                }
            }

            fun getTextColor(state: NumberHolderState): Int {
                return when (state) {
                    DEFAULT -> R.color.light_gray
                    COMPLETED -> R.color.cyan
                    FOCUSING -> R.color.yellow
                }
            }
        }
    }

    interface ItemClickListener {
        fun onItemClicked(idx: Int)
    }

    init {
        states.add(NumberHolderState.FOCUSING)
        for (i in 1..11) {
            states.add(NumberHolderState.DEFAULT)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.item_number, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentState = states[position]

        val backgroundRes = NumberHolderState.getBackgroundResource(currentState)
        holder.tvNumber.setBackgroundResource(backgroundRes)

        val textColor = NumberHolderState.getTextColor(currentState)
        holder.tvNumber.setTextColor(ContextCompat.getColor(holder.tvNumber.context, textColor))
        holder.tvNumber.text = numbers[position].toString()
    }

    override fun getItemCount(): Int {
        return numbers.size
    }

    fun refreshViewState(prevItemValidation: Boolean, focusedPosition: Int) {
        setPrevItemState(prevItemValidation)
        setCurrentItemState(focusedPosition)
    }

    private fun setPrevItemState(isValidate: Boolean) {
        val previousIdx = currentIdx
        updateState(previousIdx, isValidate)
    }

    private fun setCurrentItemState(currentIdx: Int) {
        this.currentIdx = currentIdx
        states[this.currentIdx] = NumberHolderState.FOCUSING
        notifyItemChanged(this.currentIdx)
    }

    fun updateState(position: Int, isValidate: Boolean) {
        if (isValidate) {
            states[position] = NumberHolderState.COMPLETED
        } else {
            states[position] = NumberHolderState.DEFAULT
        }
        notifyItemChanged(position)
    }

    fun setItemClickListener(listener: (prevPosition: Int, currentPosition: Int) -> Unit) {
        mItemClickListener = object : ItemClickListener {
            override fun onItemClicked(idx: Int) {
                listener(currentIdx, idx)
            }
        }
    }

    fun getCurrentIndex() = currentIdx

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val tvNumber: TextView = itemView.tvNumber
        private var currentState = NumberHolderState.DEFAULT

        init {
            tvNumber.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            currentState = NumberHolderState.FOCUSING
            mItemClickListener.onItemClicked(adapterPosition)
        }
    }
}