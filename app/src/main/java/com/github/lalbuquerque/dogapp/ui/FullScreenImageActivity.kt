package com.github.lalbuquerque.dogapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.github.lalbuquerque.dogapp.R
import kotlinx.android.synthetic.main.activity_full_screen_image.*

class FullScreenImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_image)

        val imageUrl = intent.getStringExtra(EXTRA_IMAGE_RESOURCE)

        Glide.with(this)
            .load(imageUrl)
            .into(imgFullScreen)

        container.setBackgroundColor(ContextCompat.getColor(this, android.R.color.black))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    companion object { const val EXTRA_IMAGE_RESOURCE = "image_resource" }
}
