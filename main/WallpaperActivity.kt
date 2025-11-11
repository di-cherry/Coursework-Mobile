package com.example.wallcustomizer

import android.content.Context
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

        val gridView = findViewById<GridView>(R.id.wallpaperGrid)

        // Список доступных обоев
        val wallpapers = listOf(
            R.drawable.wall1,
            R.drawable.wall2,
            R.drawable.wall3,
            R.drawable.wall4
        )

        // Настраиваем адаптер
        val adapter = WallpaperAdapter(this, wallpapers)
        gridView.adapter = adapter

        // Обработка выбора
        gridView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
                prefs.edit().putInt("selectedWallpaper", wallpapers[position]).apply()

                Toast.makeText(this, "Обои выбраны!", Toast.LENGTH_SHORT).show()
                finish() // Возвращаемся назад (например, на главный экран)
            }
    }
}
