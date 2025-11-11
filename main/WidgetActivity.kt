package com.example.wallcustomizer

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class WidgetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_widget)

        val cbClock = findViewById<CheckBox>(R.id.cbClock)
        val cbWeather = findViewById<CheckBox>(R.id.cbWeather)
        val cbNotes = findViewById<CheckBox>(R.id.cbNotes)
        val cbCalendar = findViewById<CheckBox>(R.id.cbCalendar)
        val btnSave = findViewById<Button>(R.id.btnSaveWidgets)

        val prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)

        // Подгружаем предыдущие сохранённые значения
        cbClock.isChecked = prefs.getBoolean("showClock", true)
        cbWeather.isChecked = prefs.getBoolean("showWeather", false)
        cbNotes.isChecked = prefs.getBoolean("showNotes", false)
        cbCalendar.isChecked = prefs.getBoolean("showCalendar", false)

        btnSave.setOnClickListener {
            prefs.edit()
                .putBoolean("showClock", cbClock.isChecked)
                .putBoolean("showWeather", cbWeather.isChecked)
                .putBoolean("showNotes", cbNotes.isChecked)
                .putBoolean("showCalendar", cbCalendar.isChecked)
                .apply()

            Toast.makeText(this, "Выбор сохранён!", Toast.LENGTH_SHORT).show()
            finish() // Закрываем экран
        }
    }
}
