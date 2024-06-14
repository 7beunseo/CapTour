package com.example.captour

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.captour.databinding.ActivityDetailCommunityBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

        if(MyApplication.email.toString() != binding.email.text.toString()) {
            // Log.d("mobileapp", MyApplication.email.toString())
            // Log.d("mobileapp", binding.email.toString())

            // 팔로우 상태 확인
            val retrofit = Retrofit.Builder()
                .baseUrl("http://172.30.1.4:8080/captour/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(NetworkService::class.java)
            val call =
                apiService.getFollowStatus(
                    follower = MyApplication.email.toString(),
                    following = binding.email.text.toString()
                )

            call?.enqueue(object : Callback<FollowJsonResponse> {
                override fun onResponse(call: Call<FollowJsonResponse>, response: Response<FollowJsonResponse>) {
                    response.body()?.toString()?.let { Log.d("mobileapp", it) }
                    Toast.makeText(this@CommunityDetailActivity, "팔로우 상태 조회 완료", Toast.LENGTH_LONG).show()
                    if(response.body()?.message == "true") {
                        binding.followBtn.visibility = View.GONE
                        binding.followCancleBtn.visibility = View.VISIBLE
                    } else {
                        binding.followBtn.visibility = View.VISIBLE
                        binding.followCancleBtn.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<FollowJsonResponse>, t: Throwable) {
                    // Log.d("mobileapp", t.toString())
                    Toast.makeText(this@CommunityDetailActivity, "팔로우 상태 조회 실패", Toast.LENGTH_LONG).show()

                }
            })
        }

        binding.followCancleBtn.setOnClickListener {
            // 팔로우 취소
            val retrofit = Retrofit.Builder()
                .baseUrl("http://172.30.1.4:8080/captour/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(NetworkService::class.java)
            val call =
                apiService.deleteFollow(
                    follower = MyApplication.email.toString(),
                    following = binding.email.text.toString()
                )

            call?.enqueue(object : Callback<FollowJsonResponse> {
                override fun onResponse(call: Call<FollowJsonResponse>, response: Response<FollowJsonResponse>) {
                    response.body()?.toString()?.let { Log.d("mobileapp", it) }
                    Toast.makeText(this@CommunityDetailActivity, "팔로우 삭제 완료", Toast.LENGTH_LONG).show()

                    // 화면 다시 조회
                    val intent = intent
                    finish()
                    startActivity(intent)
                }

                override fun onFailure(call: Call<FollowJsonResponse>, t: Throwable) {
                    // Log.d("mobileapp", t.toString())
                    Toast.makeText(this@CommunityDetailActivity, "팔로우 삭제 실패", Toast.LENGTH_LONG).show()

                }
            })
        }


        binding.followBtn.setOnClickListener {
            // 팔로우 생성
            val retrofit = Retrofit.Builder()
                .baseUrl("http://172.30.1.4:8080/captour/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(NetworkService::class.java)
            val call =
                apiService.createFollow(
                    follower = MyApplication.email.toString(),
                    following = binding.email.text.toString()
                )

            call?.enqueue(object : Callback<FollowJsonResponse> {
                override fun onResponse(call: Call<FollowJsonResponse>, response: Response<FollowJsonResponse>) {
                    // Log.d("mobileapp", response.message())
                    Toast.makeText(this@CommunityDetailActivity, "팔로우 완료", Toast.LENGTH_LONG).show()

                    // 화면 다시 조회
                    val intent = intent
                    finish()
                    startActivity(intent)
                }

                override fun onFailure(call: Call<FollowJsonResponse>, t: Throwable) {
                    // Log.d("mobileapp", t.toString())
                    Toast.makeText(this@CommunityDetailActivity, "팔로우 실패", Toast.LENGTH_LONG).show()

                }
            })
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp()
    }
}