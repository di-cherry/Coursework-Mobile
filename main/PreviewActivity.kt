package com.example.wallcustomizer

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import yuku.ambilwarna.AmbilWarnaDialog
import java.text.SimpleDateFormat
import java.util.*

class PreviewActivity : AppCompatActivity() {

    private lateinit var root: RelativeLayout
    private lateinit var imgBackground: ImageView
    private lateinit var editPanel: LinearLayout
    private lateinit var btnAdd: Button
    private lateinit var btnDelete: Button
    private lateinit var btnColor: Button
    private lateinit var btnFont: Button
    private lateinit var btnSize: Button

    private var selectedView: View? = null
    private val handler = Handler(Looper.getMainLooper())
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    private val prefs by lazy { getSharedPreferences("settings", MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)

        root = findViewById(R.id.previewRoot)
        imgBackground = findViewById(R.id.imgPreview)
        editPanel = findViewById(R.id.editPanel)
        btnAdd = findViewById(R.id.btnAdd)
        btnDelete = findViewById(R.id.btnDelete)
        btnColor = findViewById(R.id.btnColor)
        btnFont = findViewById(R.id.btnFont)
        btnSize = findViewById(R.id.btnSize)

        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        val wallpaperRes = prefs.getInt("selectedWallpaper", R.drawable.sample_preview)
        imgBackground.setImageResource(wallpaperRes)

        val showClock = prefs.getBoolean("showClock", true)
        val showWeather = prefs.getBoolean("showWeather", false)
        val showNotes = prefs.getBoolean("showNotes", false)
        val showCalendar = prefs.getBoolean("showCalendar", false)

        if (showClock) addClockWidget()
        if (showWeather) addWeatherWidget()
        if (showNotes) addNotesWidget()
        if (showCalendar) addCalendarWidget()

        btnAdd.setOnClickListener { showAddDialog() }
        btnDelete.setOnClickListener { removeSelectedView() }
        btnColor.setOnClickListener { changeColor() }
        btnFont.setOnClickListener { changeFont() }
        btnSize.setOnClickListener { changeSize() }
    }

    /** Диалог добавления нового элемента **/
    private fun showAddDialog() {
        val items = arrayOf("🕒 Часы", "🌤 Погода", "✍️ Заметка", "📅 Календарь")
        AlertDialog.Builder(this)
            .setTitle("Добавить элемент")
            .setItems(items) { _, which ->
                when (which) {
                    0 -> addClock()
                    1 -> addWeather()
                    2 -> addNote()
                    3 -> addCalendar()
                }
            }.show()
    }

    /** Создание виджета часов **/
    private fun addClock() {
        val tv = TextView(this)
        tv.text = timeFormat.format(Date())
        tv.textSize = 48f
        tv.setTextColor(Color.WHITE)
        tv.setPadding(8, 8, 8, 8)
        tv.typeface = ResourcesCompat.getFont(this, R.font.roboto_regular)
        tv.setOnClickListener { selectView(tv) }

        enableDrag(tv)
        root.addView(tv)
        startClock(tv)
    }

    /** Погода (фиктивная) **/
    private fun addWeather() {
        val tv = TextView(this)
        tv.text = "☀ 22°C, ясно"
        tv.textSize = 20f
        tv.setTextColor(Color.WHITE)
        tv.setPadding(8, 8, 8, 8)
        tv.setOnClickListener { selectView(tv) }

        enableDrag(tv)
        root.addView(tv)
    }

    /** Заметка **/
    private fun addNote() {
        val et = EditText(this)
        et.hint = "Моя заметка..."
        et.setTextColor(Color.WHITE)
        et.setBackgroundColor(Color.parseColor("#40000000"))
        et.setPadding(12, 8, 12, 8)
        et.setOnClickListener { selectView(et) }

        enableDrag(et)
        root.addView(et)
    }

    /** Календарь (текущая дата) **/
    private fun addCalendar() {
        val tv = TextView(this)
        val date = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date())
        tv.text = "📅 $date"
        tv.textSize = 18f
        tv.setTextColor(Color.WHITE)
        tv.setOnClickListener { selectView(tv) }

        enableDrag(tv)
        root.addView(tv)
    }

    /** Обновление времени для часов **/
    private fun startClock(tv: TextView) {
        handler.post(object : Runnable {
            override fun run() {
                tv.text = timeFormat.format(Date())
                handler.postDelayed(this, 60_000)
            }
        })
    }

    /** Перетаскивание **/
    private fun enableDrag(view: View) {
        var dX = 0f
        var dY = 0f
        view.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    dX = v.x - event.rawX
                    dY = v.y - event.rawY
                    selectView(v)
                }
                MotionEvent.ACTION_MOVE -> {
                    v.x = event.rawX + dX
                    v.y = event.rawY + dY
                }
            }
            true
        }
    }

    /** Выделение элемента (рамка вокруг) **/
    private fun selectView(v: View) {
        selectedView?.background = null
        selectedView = v
        v.setBackgroundColor(Color.parseColor("#33FFFFFF"))
    }

    /** Удаление выделенного элемента **/
    private fun removeSelectedView() {
        selectedView?.let {
            root.removeView(it)
            selectedView = null
        } ?: Toast.makeText(this, "Выберите элемент для удаления", Toast.LENGTH_SHORT).show()
    }

    /** Изменение цвета текста **/
    private fun changeColor() {
        val view = selectedView ?: return Toast.makeText(this, "Сначала выберите элемент", Toast.LENGTH_SHORT).show()
        val colorDialog = AmbilWarnaDialog(this, Color.WHITE, object : AmbilWarnaDialog.OnAmbilWarnaListener {
            override fun onCancel(dialog: AmbilWarnaDialog?) {}
            override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                if (view is TextView) view.setTextColor(color)
                if (view is EditText) view.setTextColor(color)
            }
        })
        colorDialog.show()
    }

    /** Смена шрифта **/
    private fun changeFont() {
        val view = selectedView ?: return Toast.makeText(this, "Выберите элемент", Toast.LENGTH_SHORT).show()
        val fonts = listOf(
            "Roboto" to R.font.roboto_regular,
            "Playfair" to R.font.playfair_display,
            "Orbitron" to R.font.orbitron_regular,
            "Indie Flower" to R.font.indie_flower
        )

        val fontNames = fonts.map { it.first }.toTypedArray()
        AlertDialog.Builder(this)
            .setTitle("Выберите шрифт")
            .setItems(fontNames) { _, which ->
                val tf = ResourcesCompat.getFont(this, fonts[which].second)
                if (view is TextView) view.typeface = tf
                if (view is EditText) view.typeface = tf
            }.show()
    }

    /** Изменение размера текста **/
    private fun changeSize() {
        val view = selectedView ?: return Toast.makeText(this, "Выберите элемент", Toast.LENGTH_SHORT).show()
        val seekBar = SeekBar(this)
        seekBar.max = 80
        seekBar.progress = if (view is TextView) view.textSize.toInt() else 16

        AlertDialog.Builder(this)
            .setTitle("Размер текста")
            .setView(seekBar)
            .setPositiveButton("OK") { _, _ ->
                val size = seekBar.progress.toFloat()
                if (view is TextView) view.textSize = size
                if (view is EditText) view.textSize = size
            }.show()
    }
}
