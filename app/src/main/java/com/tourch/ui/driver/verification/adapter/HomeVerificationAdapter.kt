package com.tourch.ui.driver.verification.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tourch.R
import com.tourch.databinding.ItemHomeVerificationBinding
import com.tourch.ui.driver.verification.model.HomeVerificationModel


@Suppress("DEPRECATION")
class HomeVerificationAdapter(
    var arrayList: ArrayList<HomeVerificationModel>,  var type:String,var listener: OnVehicleClick?
) : RecyclerView.Adapter<HomeVerificationAdapter.ViewHolder>() {
    var mContext: Context? = null
    var pos = -1

    inner class ViewHolder(val binding: ItemHomeVerificationBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val binding =
            ItemHomeVerificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder) {
            with(arrayList[holder.adapterPosition]) {
                val dataItem=arrayList[holder.adapterPosition]

                if (type=="info"){
                    binding.rlDriverInfo.visibility=View.GONE
                }else{
                    binding.rlDriverInfo.visibility=View.VISIBLE
                }

                binding.txtTitle.text=dataItem.name
                binding.txtDescription.text=dataItem.description
                if (dataItem.completePercent!=0) {
                    binding.txtCompletion.visibility=View.VISIBLE
                    binding.txtCompletion.text = "Only " + dataItem.completePercent + "% Completed"
                    binding.ivProgress.setImageResource(R.drawable.ic_circle_check)
                    binding.viewProgress.setBackgroundColor(mContext!!.resources.getColor(R.color.color32B67A))

                }else{
                    binding.txtCompletion.visibility=View.GONE

                    binding.ivProgress.setImageResource(R.drawable.ic_circle_uncheck)
                    binding.viewProgress.setBackgroundColor(mContext!!.resources.getColor(R.color.color_grey))
                }


            }
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }


    interface OnVehicleClick {
        fun onClick(position: Int)
    }
}