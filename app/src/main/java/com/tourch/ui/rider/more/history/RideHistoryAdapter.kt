package com.tourch.ui.rider.more.history

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.tourch.databinding.ItemRideHistoryBinding
import java.util.*

class RideHistoryAdapter(
    var tripArrayList: ArrayList<TripHistoryModel>, var listener: OnClickRide?
) : RecyclerView.Adapter<RideHistoryAdapter.ViewHolder>(),Filterable {
    var mContext: Context? = null

    private var arrayList = ArrayList<TripHistoryModel>()

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                var resultList = ArrayList<TripHistoryModel>()
                if (charSearch.isEmpty()) {
                    resultList = tripArrayList
                } else {
                    for (row in tripArrayList.indices) {
                        if (tripArrayList[row].status!!.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT))) {
                            resultList.add(tripArrayList[row])
                        }
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = resultList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                arrayList = results?.values as ArrayList<TripHistoryModel>
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(val binding: ItemRideHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val binding = ItemRideHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder) {
            with(arrayList[position]) {
                binding.cost.text = "$"+arrayList[position].fareInfo?.total
                binding.date.text = arrayList[position].bookingDate
                binding.rideStatus.text = arrayList[position].status
                binding.tvPickUpAddress.text = arrayList[position].pickUp?.location
                binding.tvDropOffAddress.text = "$" + arrayList[position].dropOff[0].location



                itemView.setOnClickListener {
                    notifyDataSetChanged()
                    listener!!.onClick(arrayList[position])
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }


    interface OnClickRide {
        fun onClick(position: TripHistoryModel)
    }
}