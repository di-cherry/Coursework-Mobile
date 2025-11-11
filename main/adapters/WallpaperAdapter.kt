package com.example.wallcustomizer.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.example.wallcustomizer.R

class WallpaperAdapter(private val context: Context, private val wallpapers: List<Int>) : BaseAdapter() {

    override fun getCount(): Int = wallpapers.size
    override fun getItem(position: Int): Any = wallpapers[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val imageView = ImageView(context)
        imageView.setImageResource(wallpapers[position])
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.layoutParams = ViewGroup.LayoutParams(500, 900)
        imageView.setPadding(8, 8, 8, 8)
        return imageView
    }
}
