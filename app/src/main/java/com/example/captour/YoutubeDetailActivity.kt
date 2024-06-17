package com.example.captour

import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.preference.PreferenceManager
import com.example.captour.databinding.ActivityYoutubeDetailBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener

class YoutubeDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityYoutubeDetailBinding
    lateinit var sharedPreference: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityYoutubeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar);

        sharedPreference = PreferenceManager.getDefaultSharedPreferences(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val videoId = intent.getStringExtra("videoId")


        binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                if (videoId != null) {
                    youTubePlayer.loadVideo(videoId, 0f)
                }
            }
        })

        binding.videoTitle.text = title
        binding.videoDescription.text = description

    }

    override fun onSupportNavigateUp(): Boolean {
    return super.onSupportNavigateUp()
    }

    override fun onResume() {
        super.onResume()

        // 글자 크기 설정
        val fontSize = sharedPreference.getInt("font_size", 16)
        binding.videoTitle.textSize = fontSize + 5f
        binding.videoDescription.textSize = fontSize + 1f

        // 색상 설정
        val color = sharedPreference.getString("color", "#363C90")
        val colorCode = Color.parseColor(color)
        val colorStateList = ColorStateList.valueOf(colorCode)
        binding.toolbar.setBackgroundColor(colorCode)

        // 폰트 굵기 설정
        val fontStyle = sharedPreference.getString("font_style", "regular")

        var typeface: Typeface?
        if(fontStyle == "regular") {
            typeface = ResourcesCompat.getFont(this, R.font.nanum_regular)
        } else {
            typeface = ResourcesCompat.getFont(this, R.font.nanum_bold)
        }
        binding.videoDescription.typeface = typeface
        binding.videoTitle.typeface = typeface
    }
}