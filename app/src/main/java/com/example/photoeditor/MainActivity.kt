package com.example.photoeditor

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.photoeditor.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> loadImage(result.data?.dataString)
                else -> Toast.makeText(this, R.string.image_load_error, Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            btnLoad.setOnClickListener { openAndroidGallery() }
            btnCrop.setOnClickListener { initializeFeatureActivity(binding.btnCrop.text.toString()) }
            btnLight.setOnClickListener { initializeFeatureActivity(binding.btnLight.text.toString()) }
            btnColor.setOnClickListener { initializeFeatureActivity(binding.btnColor.text.toString()) }
            btnFilters.setOnClickListener { initializeFeatureActivity(binding.btnFilters.text.toString()) }
        }
    }

    private fun openAndroidGallery() {
        val intent = Intent(
            Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        resultLauncher.launch(intent)
    }

    private fun loadImage(image: String?) {
        Glide.with(this)
            .load(image)
            .into(binding.ivGalleryPhoto)
    }

    private fun initializeFeatureActivity(btnText: String) {
        Intent(this@MainActivity, FeatureActivity::class.java).also {
            it.putExtra(BTN_TEXT, btnText)
            startActivity(it)
        }
    }

    companion object {
        const val BTN_TEXT = "BTN_TEXT"
    }
}