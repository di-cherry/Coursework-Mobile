package com.example.wallcustomizer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.example.wallcustomizer.R

class WallpaperAdapter(
    private val context: Context,
    private val wallpapers: List<Int>
) : BaseAdapter() {

    override fun getCount(): Int = wallpapers.size

    override fun getItem(position: Int): Any = wallpapers[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_wallpaper, parent, false)
        val imageView = view.findViewById<ImageView>(R.id.imgWallpaper)
        imageView.setImageResource(wallpapers[position])
        return view
    }
}
