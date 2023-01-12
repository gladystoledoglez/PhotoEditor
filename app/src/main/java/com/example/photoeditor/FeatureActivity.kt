package com.example.photoeditor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.photoeditor.MainActivity.Companion.BTN_TEXT
import com.example.photoeditor.databinding.ActivityFeatureBinding

class FeatureActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeatureBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFeatureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val text = intent.getStringExtra(BTN_TEXT)
        binding.tvCrop.text = text
    }
}