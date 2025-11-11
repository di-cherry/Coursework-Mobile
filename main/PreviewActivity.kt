package com.example.wallcustomizer

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import java.text.SimpleDateFormat
import java.util.*

class PreviewActivity : AppCompatActivity() {

    private lateinit var tvClock: TextView
    private lateinit var tvWeather: TextView
    private lateinit var etNote: EditText
    private lateinit var imgBackground: ImageView
    private lateinit var btnBack: Button
    private lateinit var root: RelativeLayout

    private val handler = Handler(Looper.getMainLooper())
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)

        root = findViewById(R.id.previewRoot)
        tvClock = findViewById(R.id.tvClock)
        tvWeather = findViewById(R.id.tvWeather)
        etNote = findViewById(R.id.etNote)
        imgBackground = findViewById(R.id.imgPreview)
        btnBack = findViewById(R.id.btnBack)

        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        val wallpaperRes = prefs.getInt("selectedWallpaper", R.drawable.sample_preview)
        val widgetColor = prefs.getString("widgetColor", "#FFFFFF")
        val textSize = prefs.getFloat("widgetTextSize", 48f)
        val noteText = prefs.getString("noteText", "")
        val fontName = prefs.getString("widgetFont", "Roboto")

        // Устанавливаем фон
        imgBackground.setImageResource(wallpaperRes)

        // Цвет, размер, шрифт
        val typeface = when (fontName) {
            "Playfair" -> ResourcesCompat.getFont(this, R.font.playfair_display)
            "Orbitron" -> ResourcesCompat.getFont(this, R.font.orbitron_regular)
            "Indie Flower" -> ResourcesCompat.getFont(this, R.font.indie_flower)
            else -> ResourcesCompat.getFont(this, R.font.roboto_regular)
        }

        listOf(tvClock, tvWeather, etNote).forEach {
            it.setTextColor(Color.parseColor(widgetColor))
            it.textSize = textSize
            it.typeface = typeface
        }

        etNote.setText(noteText)

        // Восстанавливаем позиции
        restorePosition(tvClock, "clock", prefs)
        restorePosition(tvWeather, "weather", prefs)
        restorePosition(etNote, "note", prefs)

        // Обработка перетаскивания
        enableDrag(tvClock, "clock", prefs)
        enableDrag(tvWeather, "weather", prefs)
        enableDrag(etNote, "note", prefs)

        // Сохраняем заметку при потере фокуса
        etNote.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                prefs.edit().putString("noteText", etNote.text.toString()).apply()
            }
        }

        startClock()

        btnBack.setOnClickListener {
            handler.removeCallbacksAndMessages(null)
            finish()
        }
    }

    /** Функция для включения перетаскивания элемента */
    private fun enableDrag(view: View, tag: String, prefs: android.content.SharedPreferences) {
        var dX = 0f
        var dY = 0f

        view.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    dX = v.x - event.rawX
                    dY = v.y - event.rawY
                }
                MotionEvent.ACTION_MOVE -> {
                    v.x = event.rawX + dX
                    v.y = event.rawY + dY
                }
                MotionEvent.ACTION_UP -> {
                    // Сохраняем координаты
                    prefs.edit()
                        .putFloat("${tag}_x", v.x)
                        .putFloat("${tag}_y", v.y)
                        .apply()
                }
            }
            true
        }
    }

    /** Восстанавливаем позицию элемента при загрузке */
    private fun restorePosition(view: View, tag: String, prefs: android.content.SharedPreferences) {
        val x = prefs.getFloat("${tag}_x", Float.NaN)
        val y = prefs.getFloat("${tag}_y", Float.NaN)
        if (!x.isNaN() && !y.isNaN()) {
            view.post {
                view.x = x
                view.y = y
            }
        }
    }

    private fun startClock() {
        handler.post(object : Runnable {
            override fun run() {
                tvClock.text = timeFormat.format(Date())
                handler.postDelayed(this, 60_000)
            }
        })
    }
}
