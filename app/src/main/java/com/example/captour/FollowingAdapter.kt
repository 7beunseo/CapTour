package com.example.captour

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.captour.databinding.FollowingItemBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyFollowViewHolder(val binding: FollowingItemBinding) : RecyclerView.ViewHolder(binding.root)

class FollowAdapter(val datas: List<Follow>?): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun getItemCount(): Int {
        return datas?.size ?:0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MyFollowViewHolder(FollowingItemBinding.inflate(LayoutInflater.from(parent.context), parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyFollowViewHolder).binding
        val followdata = datas!![position]

        binding.following.text = followdata.following

        val context = holder.itemView.context

        binding.following.setOnClickListener {
            Log.d("mobileapp", "in-adapter")
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:${followdata.following}")
            }
            context.startActivity(intent)
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
                    following = binding.following.text.toString()
                )

            val context = holder.itemView.context

            call?.enqueue(object : Callback<FollowJsonResponse> {
                override fun onResponse(call: Call<FollowJsonResponse>, response: Response<FollowJsonResponse>) {
                    response.body()?.toString()?.let { Log.d("mobileapp", it) }
                    Toast.makeText(context.applicationContext, "팔로우 삭제 완료", Toast.LENGTH_LONG).show()
                }

                override fun onFailure(call: Call<FollowJsonResponse>, t: Throwable) {
                    Toast.makeText(context.applicationContext, "팔로우 삭제 실패", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}
