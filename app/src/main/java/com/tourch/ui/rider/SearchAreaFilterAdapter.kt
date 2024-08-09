package com.tourch.ui.rider

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tourch.R


class SearchAreaFilterAdapter(
    var activity: Context,
    var arrayList: ArrayList<String>,
    var listener: OnSearchAreaClick?
) : BaseAdapter() {

   // var mContext: Context? = null
    var pos: Int? = null
    private var layoutInflater: LayoutInflater? = null
    private lateinit var tvMiles: TextView
    private lateinit var llMiles: LinearLayout



    fun updateList(list: ArrayList<String>) {
        arrayList.clear()
        arrayList.addAll(list)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    interface OnSearchAreaClick {
        fun onClick(position: Int, area: String)

    }

    override fun getCount(): Int {
        return arrayList.size
    }

    override fun getItem(p0: Int): Any? {
        return null
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, p2: ViewGroup?): View {
        var convertView = convertView
        // on blow line we are checking if layout inflater
        // is null, if it is null we are initializing it.
        if (layoutInflater == null) {
            layoutInflater =
                activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        // on the below line we are checking if convert view is null.
        // If it is null we are initializing it.
        if (convertView == null) {
            // on below line we are passing the layout file
            // which we have to inflate for each item of grid view.
            convertView = layoutInflater!!.inflate(R.layout.item_search_area, null)
        }
        // on below line we are initializing our course image view
        // and course text view with their ids.
        tvMiles = convertView!!.findViewById(R.id.tvMiles)
        llMiles = convertView.findViewById(R.id.llMiles)
        tvMiles.text = arrayList[position]
        llMiles.setOnClickListener {
            listener!!.onClick(position, arrayList[position])
            notifyDataSetChanged()
            pos=position
        }


        if (pos==position){
            llMiles.background = ContextCompat.getDrawable(activity, R.drawable.bg_selected_filter)
            tvMiles.setTextColor(activity.getColor(R.color.white))
        }else{
            llMiles.background = ContextCompat.getDrawable(activity, R.drawable.bg_unselected_filter)
            tvMiles.setTextColor(activity.getColor(R.color.black))
        }

        // at last we are returning our convert view.
        return convertView
    }

}