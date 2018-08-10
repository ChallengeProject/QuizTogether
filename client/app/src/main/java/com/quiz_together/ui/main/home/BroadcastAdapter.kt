package com.quiz_together.ui.main.home

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.quiz_together.R
import com.quiz_together.data.model.Broadcast
import com.quiz_together.util.toStringTemplate
import kotlinx.android.synthetic.main.item_home_broadcast.view.*

class BroadcastAdapter(private val context: Context?, val cb: (str:String) -> Unit ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TAG = "BroadcastAdapter#$#"

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

    class ImageViewHolder(context: Context?, parent: ViewGroup?,val cbOnClickLl: (str: String) -> Unit)
        : RecyclerView.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_home_broadcast, parent, false)) {

        val TAG = "ImageViewHolder#$#"

        fun onBind(item: Broadcast) {
            itemView.onBind(item)
        }

        private fun View.onBind(item: Broadcast) {

            tvTitle.text = item.title
            tvDate.text = item.scheduledTime?.toStringTemplate()?.substring(2)
            tvName.text = item.userView?.name
            tvPrize.text = item.prize.toString()
            tvContent.text = item.description
            ivProfile.setImageResource(R.drawable.icc_profile)

            // TODO set type from date
            var roomOutputType = RoomOutputType.DEFAULT

            if(BroadcastAdapter.aa == 0)
            {
                roomOutputType = RoomOutputType.DEFAULT
                BroadcastAdapter.aa++
            } else             if(BroadcastAdapter.aa == 1)
            {
                roomOutputType = RoomOutputType.FOLLOW
                BroadcastAdapter.aa++
            } else             if(BroadcastAdapter.aa == 2)
            {
                roomOutputType = RoomOutputType.RESERVATION
                BroadcastAdapter.aa++
            }

            tvContent.text = "가나다라마바사\n아자차카타파하"

            if(roomOutputType == RoomOutputType.DEFAULT) {
                ivProfile.borderColor = getResources().getColor(R.color.white)
                llBg.setBackgroundResource(R.drawable.back_white_border_for_layout)
                tvShare.text = "팔로우"

                tvPrize.setTextColor(Color.parseColor("#000000"))
                tvName.setTextColor(Color.parseColor("#000000"))
                tvShare.setTextColor(Color.parseColor("#236ad1"))
                tvTitle.setTextColor(Color.parseColor("#0e8199"))
                tvContent.setTextColor(Color.parseColor("#6b6b6b"))
                tvDate.setTextColor(Color.parseColor("#a2a8b0"))

            }
            else if(roomOutputType == RoomOutputType.FOLLOW) {
                ivProfile.borderColor = getResources().getColor(R.color.deepBlue)
                llBg.setBackgroundResource(R.drawable.back_deepblue_border_for_layout)
                tvShare.text = "팔로잉"

                tvPrize.setTextColor(resources.getColor(R.color.white))
                tvName.setTextColor(resources.getColor(R.color.white))
                tvShare.setTextColor(resources.getColor(R.color.white))
                tvTitle.setTextColor(Color.parseColor("#fafd47"))
                tvContent.setTextColor(resources.getColor(R.color.white))
                tvDate.setTextColor(Color.parseColor("#fafd47"))

            } else if(roomOutputType == RoomOutputType.RESERVATION) {
                ivProfile.borderColor = getResources().getColor(R.color.shallowDark)
                llBg.setBackgroundResource(R.drawable.back_shallow_dark_border_for_layout)
                tvShare.text = ""

                tvPrize.setTextColor(resources.getColor(R.color.white))
                tvName.setTextColor(resources.getColor(R.color.white))
                tvShare.setTextColor(resources.getColor(R.color.white))
                tvTitle.setTextColor(Color.parseColor("#fafd47"))
                tvContent.setTextColor(resources.getColor(R.color.white))
                tvDate.setTextColor(Color.parseColor("#fafd47"))
            }




            rl.setOnClickListener({ _ ->
                cbOnClickLl.invoke(item.title + item.scheduledTime.toString() + item.userId + item.prize.toString())
            })


            tvShare.setOnClickListener({ _ ->
                cbOnClickLl.invoke(item.title + item.scheduledTime.toString() + item.userId + item.prize.toString())
            })


        }

    }

    enum class RoomOutputType (value:Int) {
        DEFAULT(100),
        FOLLOW(200),
        RESERVATION(300),
    }

    companion object {
        var aa = 0
    }

}