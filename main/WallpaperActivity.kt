package com.example.wallcustomizer

import android.app.WallpaperManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wallcustomizer.adapters.WallpaperAdapter

class WallpaperActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallpaper)

        val grid = findViewById<GridView>(R.id.wallpaperGrid)
        val images = listOf(
            R.drawable.wall1,
            R.drawable.wall2,
            R.drawable.wall3,
            R.drawable.wall4
        )

        val adapter = WallpaperAdapter(this, images)
        grid.adapter = adapter

        grid.setOnItemClickListener { _: AdapterView<*>, _, position: Int, _: Long ->
            val selectedImage = images[position]

            // Сохраняем выбранные обои
            val prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
            prefs.edit().putInt("selectedWallpaper", selectedImage).apply()

            // Устанавливаем как фон
            val wallpaperManager = WallpaperManager.getInstance(this)
            val bitmap = BitmapFactory.decodeResource(resources, selectedImage)
            wallpaperManager.setBitmap(bitmap)

            Toast.makeText(this, "Обои применены и сохранены!", Toast.LENGTH_SHORT).show()
        }
    }
}
