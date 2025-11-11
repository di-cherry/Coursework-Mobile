package com.example.wallcustomizer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.wallcustomizer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnWallpapers.setOnClickListener {
            startActivity(Intent(this, WallpaperActivity::class.java))
        }

        binding.btnWidgets.setOnClickListener {
            startActivity(Intent(this, WidgetActivity::class.java))
        }

        binding.btnPreview.setOnClickListener {
            startActivity(Intent(this, PreviewActivity::class.java))
        }
    }
}
