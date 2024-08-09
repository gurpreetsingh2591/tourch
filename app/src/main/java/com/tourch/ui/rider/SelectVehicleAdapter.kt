package com.tourch.ui.rider

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tourch.R
import com.tourch.databinding.ItemSelectCarBinding
import com.tourch.ui.rider.model.EstimateFareApiResponse

class SelectVehicleAdapter(
    var arrayList: ArrayList<EstimateFareApiResponse.Ride>, var listener: OnVehicleClick?
) : RecyclerView.Adapter<SelectVehicleAdapter.ViewHolder>() {
    var mContext: Context? = null
    var pos = -1
    inner class ViewHolder(val binding: ItemSelectCarBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val binding = ItemSelectCarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder) {
            with(arrayList[position]) {
                binding.tvCarName.text = arrayList[position].title
                binding.tvCarSeats.text = arrayList[position].maxSeats.toString() + " Seats"
                binding.tvCarTime.text = arrayList[position].vehicleDuration?.durationTime + " away"
                binding.tvEstimate.text = "$" + arrayList[position].fareInfo?.total.toString()

                if (pos == position) {
                    binding.mainLay.setBackgroundResource(R.drawable.bg_edit_text)
                } else {
                    binding.mainLay.setBackgroundResource(R.drawable.bg_edit_grey)
                }

                itemView.setOnClickListener {
                    notifyDataSetChanged()
                    pos = position
                    listener!!.onClick(arrayList[position])
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }


    interface OnVehicleClick {
        fun onClick(position: EstimateFareApiResponse.Ride)
    }
}