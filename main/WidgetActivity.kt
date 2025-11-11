package com.example.wallcustomizer

import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class WidgetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_widget)

        val spinnerColor = findViewById<Spinner>(R.id.spinnerColor)
        val spinnerFont = findViewById<Spinner>(R.id.spinnerFont)
        val seekSize = findViewById<SeekBar>(R.id.seekTextSize)
        val btnApply = findViewById<Button>(R.id.btnApplyWidget)

        val colors = mapOf(
            "Белый" to "#FFFFFF",
            "Чёрный" to "#000000",
            "Голубой" to "#00BCD4",
            "Розовый" to "#FF4081",
            "Золотой" to "#FFD700"
        )

        val fonts = listOf(
            "Roboto" to R.font.roboto_regular,
            "Playfair" to R.font.playfair_display,
            "Orbitron" to R.font.orbitron_regular,
            "Indie Flower" to R.font.indie_flower
        )

        // Настройка адаптеров
        spinnerColor.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            colors.keys.toList()
        )

        spinnerFont.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            fonts.map { it.first }
        )

        val prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)

        btnApply.setOnClickListener {
            val colorName = spinnerColor.selectedItem.toString()
            val colorHex = colors[colorName] ?: "#FFFFFF"
            val size = seekSize.progress.toFloat()
            val fontName = spinnerFont.selectedItem.toString()

            prefs.edit()
                .putString("widgetColor", colorHex)
                .putFloat("widgetTextSize", size)
                .putString("widgetFont", fontName)
                .apply()

            Toast.makeText(this, "Настройки сохранены", Toast.LENGTH_SHORT).show()
        }
    }
}
