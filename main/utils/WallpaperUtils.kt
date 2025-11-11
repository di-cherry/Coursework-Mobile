package com.example.wallcustomizer.utils

import android.app.WallpaperManager
import android.content.Context
import android.graphics.BitmapFactory
import android.widget.Toast

object WallpaperUtils {

    fun setWallpaper(context: Context, imageRes: Int) {
        try {
            val bitmap = BitmapFactory.decodeResource(context.resources, imageRes)
            val wallpaperManager = WallpaperManager.getInstance(context)
            wallpaperManager.setBitmap(bitmap)
            Toast.makeText(context, "Обои установлены!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Ошибка при установке обоев", Toast.LENGTH_SHORT).show()
        }
    }
}
