package com.example.captour

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.captour.databinding.ActivityDetailCommunityBinding

class CommunityDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailCommunityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCommunityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar);
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val title = intent.getStringExtra("title")
        val email = intent.getStringExtra("email")
        val dateTime = intent.getStringExtra("dateTime")
        val content = intent.getStringExtra("content")
        val stars = intent.getFloatExtra("stars", 0.0f)
        val imageUrl = intent.getStringExtra("image")

        binding.title.text = title
        binding.email.text = email
        binding.dateTime.text = dateTime
        binding.content.text = content
        binding.ratingBar.rating = stars

        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(imageUrl)
                .into(binding.imageView)
            binding.imageView.visibility = View.VISIBLE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp()
    }
}