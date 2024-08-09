package com.tourch.ui.rider

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tourch.R
import com.tourch.databinding.ItemServicesBinding
import com.tourch.ui.rider.model.ServiceListApiResponse

class SelectServicesAdapter(
    var arrayList: ArrayList<ServiceListApiResponse.ServiceListApiResponseItem>, var listener: OnServicesClick?
) : RecyclerView.Adapter<SelectServicesAdapter.ViewHolder>() {
    var mContext: Context? = null
    var pos = 0
    inner class ViewHolder(val binding: ItemServicesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val binding = ItemServicesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(arrayList[position]) {
                binding.tvServices.text = arrayList[position].title

                if (DashboardFragment.selectedServiceId == arrayList[position].serviceId) {
                    binding.tvServices.setBackgroundResource(R.drawable.bg_ride_selected)
                } else {
                    binding.tvServices.setBackgroundResource(R.color.transparent)
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

    interface OnServicesClick {
        fun onClick(position: Int)
    }
}