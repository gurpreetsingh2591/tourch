package com.tourch.ui.rider

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tourch.R
import com.tourch.databinding.ItemPromoCodeBinding
import com.tourch.ui.rider.model.AddPromoModel


class SelectPromoAdapter(var arrayList: ArrayList<AddPromoModel>, var listener: OnPromoClick?) : RecyclerView.Adapter<SelectPromoAdapter.ViewHolder>() {
    var mContext: Context? = null
    var pos = -1

    inner class ViewHolder(val binding: ItemPromoCodeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val binding = ItemPromoCodeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder) {
            with(arrayList[position]) {
                binding.tvPercentOff.text = arrayList[position].percent + "% Off"
                binding.tvPromoCode.text = "Code:" + arrayList[position].code + "  Exp." + arrayList[position].expDate

                if (pos == position) {
                    holder.itemView.background = ContextCompat.getDrawable(mContext!!, R.drawable.bg_promo_selected)
                    binding.rbSelection.background = ContextCompat.getDrawable(mContext!!, R.drawable.ic_radio_select_green)
                } else {
                    holder.itemView.background = ContextCompat.getDrawable(mContext!!, R.drawable.bg_edit_grey)
                    binding.rbSelection.background = ContextCompat.getDrawable(mContext!!, R.drawable.ic_radio_unselect_grey)
                }

                holder.itemView.setOnClickListener {
                    notifyDataSetChanged()
                    pos = position
                    listener!!.onClick(position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    fun updateList(list: ArrayList<AddPromoModel>) {
        arrayList.clear()
        arrayList.addAll(list)
        notifyDataSetChanged()
    }

    interface OnPromoClick {
        fun onClick(position: Int)
    }

}