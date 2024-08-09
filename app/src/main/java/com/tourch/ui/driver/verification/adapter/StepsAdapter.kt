package com.tourch.ui.driver.verification.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.Resource
import com.tourch.R
import com.tourch.databinding.ItemHomeVerificationBinding
import com.tourch.databinding.LayoutVerificationStepesBinding
import com.tourch.ui.driver.verification.model.HomeVerificationModel


@Suppress("DEPRECATION")
class StepsAdapter(
    var arrayList: ArrayList<HomeVerificationModel>,  var type:String,var listener: OnVehicleClick?
) : RecyclerView.Adapter<StepsAdapter.ViewHolder>() {
    var mContext: Context? = null


    inner class ViewHolder(val binding: LayoutVerificationStepesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val binding =
            LayoutVerificationStepesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder) {
            with(arrayList[holder.adapterPosition]) {
                val dataItem=arrayList[holder.adapterPosition]


                if ((arrayList.size-1)==holder.adapterPosition){
                    binding.viewProgress.visibility=View.GONE
                }

                if (dataItem.completePercent!=0) {
                    binding.ivProgress.setImageResource(R.drawable.ic_circle_check)
                    binding.viewProgress.setBackgroundColor(mContext!!.resources.getColor(R.color.color32B67A))

                }else{
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