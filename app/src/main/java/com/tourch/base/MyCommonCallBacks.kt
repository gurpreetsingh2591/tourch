package com.tourch.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

interface MyCommonOnViewClick {
    fun onViewClick(view: View)
}

interface MyOnItemClickListener {

    fun onItemClicked(requestCode: Int)
    fun onActivityResult(imgPath : String)
}
interface MyOnItemClickListener2 {

    fun onItemClicked(position: Int, view: RecyclerView)
}
interface MyOnItemClickListener3 {

    fun onItemClicked(position: Int,view: RecyclerView,view2:View)
}

