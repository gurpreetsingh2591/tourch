package com.tourch.ui.rider

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tourch.databinding.ItemSelectedFiltersBinding

class SelectFiltersAdapter (
    var arrayList: ArrayList<String>, var listener: OnFilterClick?
) : RecyclerView.Adapter<SelectFiltersAdapter.ViewHolder>(){
    var mContext: Context? = null

    fun setFilterClick(listener: OnFilterClick) {
        this.listener = listener
    }

    inner class ViewHolder(val binding: ItemSelectedFiltersBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSelectedFiltersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder) {
            with(arrayList[position]) {
                binding.tvFilter.text =arrayList[position]
                binding.ivDelete.setOnClickListener {
                    listener!!.onDelete(position)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    fun updateList(list: ArrayList<String>) {
        val set: Set<String> = HashSet(list)
        arrayList.clear()
        arrayList.addAll(set)
        notifyDataSetChanged()
    }

    interface OnFilterClick {
        fun onDelete(position: Int)
    }

}