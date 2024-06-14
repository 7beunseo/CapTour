package com.example.captour

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.captour.databinding.ActivityFollowListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FollowListActivity : AppCompatActivity() {
    lateinit var binding: ActivityFollowListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFollowListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 팔로우 조회
        val retrofit = Retrofit.Builder()
            .baseUrl("http://172.30.1.4:8080/captour/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(NetworkService::class.java)
        val call =
            apiService.readFollow(
                follower = MyApplication.email.toString()
            )

        call?.enqueue(object : Callback<FollowJsonResponse> {
            override fun onResponse(call: Call<FollowJsonResponse>, response: Response<FollowJsonResponse>) {
                Log.d("mobileapp", response.body()?.data.toString())
                Toast.makeText(this@FollowListActivity, "팔로우 조회 완료", Toast.LENGTH_LONG).show()

                val adapter = FollowAdapter(response.body()?.data)
                binding.followRecyclerView.adapter = adapter

                val layoutManager =  LinearLayoutManager(this@FollowListActivity)
                binding.followRecyclerView.layoutManager = layoutManager
            }

            override fun onFailure(call: Call<FollowJsonResponse>, t: Throwable) {
                Log.d("mobileapp", t.toString())
                Toast.makeText(this@FollowListActivity, "팔로우 조회 실패", Toast.LENGTH_LONG).show()

            }
        })

    }
}