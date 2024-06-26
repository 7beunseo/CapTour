package com.example.captour

import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.captour.databinding.ActivityFollowingListBinding
import com.example.captour.databinding.ItemRecyclerviewBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FollowingListActivity : AppCompatActivity() {
    lateinit var binding: ActivityFollowingListBinding
    lateinit var sharedPreference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFollowingListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreference = PreferenceManager.getDefaultSharedPreferences(this)

        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar);
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 팔로우 조회
        val retrofit = Retrofit.Builder()
            .baseUrl("http://13.125.163.176/captour/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(NetworkService::class.java)
        val call =
            apiService.readFollowing(
                follower = MyApplication.email.toString()
            )

        call?.enqueue(object : Callback<FollowJsonResponse> {
            override fun onResponse(call: Call<FollowJsonResponse>, response: Response<FollowJsonResponse>) {
                Log.d("mobileapp", response.body()?.data.toString())
                // Toast.makeText(this@FollowingListActivity, "팔로잉 조회 완료", Toast.LENGTH_LONG).show()
                binding.currentUser.text = "팔로잉 " + response.body()?.data?.count() ?: ""+0 + "명"

                val adapter = FollowAdapter(response.body()?.data, this@FollowingListActivity)
                binding.followRecyclerView.adapter = adapter

                val layoutManager =  LinearLayoutManager(this@FollowingListActivity)
                binding.followRecyclerView.layoutManager = layoutManager
            }

            override fun onFailure(call: Call<FollowJsonResponse>, t: Throwable) {
                Log.d("mobileapp", t.toString())
                Toast.makeText(this@FollowingListActivity, "팔로잉 조회 실패", Toast.LENGTH_LONG).show()

            }
        })

    }

    // Adapter에서 다시 그리라고 호출
    fun loadFollowList() {
        // 팔로우 조회
        val retrofit = Retrofit.Builder()
            .baseUrl("http://13.125.163.176/captour/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(NetworkService::class.java)
        val call = apiService.readFollowing(MyApplication.email.toString())

        call?.enqueue(object : Callback<FollowJsonResponse> {
            override fun onResponse(call: Call<FollowJsonResponse>, response: Response<FollowJsonResponse>) {
                Log.d("mobileapp", response.body()?.data.toString())
                binding.currentUser.text = "팔로잉 " + (response.body()?.data?.count() ?: 0) + "명"

                val adapter = FollowAdapter(response.body()?.data, this@FollowingListActivity)
                binding.followRecyclerView.adapter = adapter

                val layoutManager = LinearLayoutManager(this@FollowingListActivity)
                binding.followRecyclerView.layoutManager = layoutManager
            }

            override fun onFailure(call: Call<FollowJsonResponse>, t: Throwable) {
                Log.d("mobileapp", t.toString())
                Toast.makeText(this@FollowingListActivity, "팔로잉 조회 실패", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun isLocalVoiceInteractionSupported(): Boolean {
        return super.isLocalVoiceInteractionSupported()
    }

    override fun onResume() {
        super.onResume()

        // 글자 크기 설정
        val fontSize = sharedPreference.getInt("font_size", 16)
        binding.currentUser.textSize = fontSize + 10f

        // 색상 설정
        val color = sharedPreference.getString("color", "#363C90")
        val colorCode = Color.parseColor(color)
        val colorStateList = ColorStateList.valueOf(colorCode)
        binding.toolbar.setBackgroundColor(colorCode)

    }
}