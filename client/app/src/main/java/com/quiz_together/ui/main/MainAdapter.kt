package com.quiz_together.ui.main

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.quiz_together.R
import com.quiz_together.data.model.Broadcast
import com.quiz_together.util.getDateTime
import kotlinx.android.synthetic.main.item_main_broadcast.view.*

class MainAdapter(private val context: Context?, val cb: (str:String) -> Unit ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val list = mutableListOf<Broadcast>()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? ImageViewHolder)?.onBind(list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = ImageViewHolder(context, parent,cb)

    override fun getItemCount() = list.size

    fun addItem(data: Broadcast) = list.add(data)

    fun clearItem() = list.clear()

    fun notifyDataSetChang() = notifyDataSetChanged()

    class ImageViewHolder(context: Context?, parent: ViewGroup?, val cbOnClickLl: (str: String) -> Unit)
        : RecyclerView.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_main_broadcast, parent, false)) {

        fun onBind(item: Broadcast) {
            itemView.onBind(item)
        }

        private fun View.onBind(item: Broadcast) {
            tvTitle.text = item.title
            tvDate.text = item.scheduledTime.getDateTime()
            tvName.text = item.userRes.name
            tvPrize.text = if(item.giftType==0) item.prize.toString() else item.giftDescription

            ll.setOnClickListener({ _ ->
                cbOnClickLl.invoke(item.broadcastId)
            })

        }

    }



}