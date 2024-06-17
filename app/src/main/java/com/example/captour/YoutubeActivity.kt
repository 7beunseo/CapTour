package com.example.captour

import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.captour.databinding.ActivityYoutubeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class YoutubeActivity : AppCompatActivity() {
    lateinit var binding: ActivityYoutubeBinding
    lateinit var sharedPreference: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_youtube)

        binding = ActivityYoutubeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreference = PreferenceManager.getDefaultSharedPreferences(this)

        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar);


        val data = intent.getStringExtra("videoTitle")
        if (data != null) {
            Log.d("mobileapp", data)
        } else {
            Log.d("mobileapp", "data is null")
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/youtube/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(NetworkService::class.java)
        val call = data?.let {
            apiService.searchVideos(
                apiKey = "AIzaSyBmdUaOHy8OiRw3cNttYLul8Sa4m7NhVP4",
                query = it
            )
        }
        call?.enqueue(object : Callback<YoutubeJsonResponse> {
            override fun onResponse(call: Call<YoutubeJsonResponse>, response: Response<YoutubeJsonResponse>) {
                val videos = response.body()?.items

                /*
                videos?.forEach { video ->
                    Log.d("Video Title", video.snippet.title)
                    Log.d("Description", video.snippet.description)
                    Log.d("Thumbnail URL", video.snippet.thumbnails.medium.url)
                }
                 */

                val adapter = YoutubeAdapter(videos)
                binding.videosRecyclerview.adapter = adapter

                val layoutManager =  LinearLayoutManager(this@YoutubeActivity)
                binding.videosRecyclerview.layoutManager = layoutManager


                binding.videosRecyclerview.addItemDecoration(
                    DividerItemDecoration(this@YoutubeActivity, LinearLayoutManager.VERTICAL)
                )
            }

            override fun onFailure(call: Call<YoutubeJsonResponse>, t: Throwable) {
                Log.e("API Error", t.message ?: "Unknown error")
            }
        })

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun isLocalVoiceInteractionSupported(): Boolean {
        return super.isLocalVoiceInteractionSupported()
    }

    override fun onResume() {
        super.onResume()

        // 글자 크기 설정
        val fontSize = sharedPreference.getInt("font_size", 16)

        // 색상 설정
        val color = sharedPreference.getString("color", "#363C90")
        val colorCode = Color.parseColor(color)
        val colorStateList = ColorStateList.valueOf(colorCode)
        binding.toolbar.setBackgroundColor(colorCode)
    }
}