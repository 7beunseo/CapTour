package com.example.captour

import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.captour.databinding.ActivityFollwerListBinding
import com.example.captour.databinding.ItemRecyclerviewBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FollowerListActivity : AppCompatActivity() {
    lateinit var binding: ActivityFollwerListBinding
    lateinit var sharedPreference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFollwerListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreference = PreferenceManager.getDefaultSharedPreferences(this)

        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar);
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://13.125.163.176/captour/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(NetworkService::class.java)

        // 팔로우 조회
        val call =
            apiService.readFollower(
                following = MyApplication.email.toString()
            )

        call?.enqueue(object : Callback<FollowJsonResponse> {
            override fun onResponse(call: Call<FollowJsonResponse>, response: Response<FollowJsonResponse>) {
                Log.d("mobileapp", response.body()?.data.toString())
                Toast.makeText(this@FollowerListActivity, "팔로워 조회 완료", Toast.LENGTH_LONG).show()
                binding.currentUser.text = "팔로워 " + response.body()?.data?.count() ?: ""+0 + "명"

                val adapter = FollowerAdapter(response.body()?.data)
                binding.followRecyclerView.adapter = adapter

                val layoutManager =  LinearLayoutManager(this@FollowerListActivity)
                binding.followRecyclerView.layoutManager = layoutManager
            }

            override fun onFailure(call: Call<FollowJsonResponse>, t: Throwable) {
                Log.d("mobileapp", t.toString())
                Toast.makeText(this@FollowerListActivity, "팔로워 조회 실패", Toast.LENGTH_LONG).show()

            }
        })

        // 팔로워 통계 조회
        val weekStatisticsCall =
            apiService.weekStatistics(
                following = MyApplication.email.toString()
            )

        weekStatisticsCall?.enqueue(object : Callback<FollowerStatisticsJsonResponse> {
            override fun onResponse(call: Call<FollowerStatisticsJsonResponse>, response: Response<FollowerStatisticsJsonResponse>) {
                Toast.makeText(this@FollowerListActivity, "팔로워 통계 조회 완료", Toast.LENGTH_LONG).show()

                val follower_values = ArrayList<Entry>(7)

                val datas = response.body()?.data
                datas?.let {
                    for(data in it) {
                        val entry = Entry(data.day.toFloat(), data.followerNum.toFloat())
                        follower_values.add(entry)
                    }
                }

                val linedataset = LineDataSet(follower_values.reversed(), "LineDataSet") // reverse해주어야 출력됨!

                val color = sharedPreference.getString("color", "#363C90")
                val colorCode = Color.parseColor(color)
                linedataset.color = colorCode

                linedataset.lineWidth = 5f
                linedataset.setCircleColor(Color.MAGENTA)
                val linedata = LineData(linedataset)
                binding.weekLineChart.data = linedata

            }

            override fun onFailure(call: Call<FollowerStatisticsJsonResponse>, t: Throwable) {
                Log.d("mobileapp", t.toString())
                Toast.makeText(this@FollowerListActivity, "팔로워 조회 실패", Toast.LENGTH_LONG).show()

            }
        })

        val monthStatisticsCall =
            apiService.monthStatistics(
                following = MyApplication.email.toString()
            )

        monthStatisticsCall?.enqueue(object : Callback<FollowerStatisticsJsonResponse> {
            override fun onResponse(call: Call<FollowerStatisticsJsonResponse>, response: Response<FollowerStatisticsJsonResponse>) {
                Toast.makeText(this@FollowerListActivity, "팔로워 통계 조회 완료", Toast.LENGTH_LONG).show()

                val follower_values = ArrayList<Entry>(7)

                val datas = response.body()?.data
                datas?.let {
                    for(data in it) {
                        val entry = Entry(data.day.toFloat(), data.followerNum.toFloat())
                        follower_values.add(entry)
                    }
                }

                // Log.d("mobileapp",follower_values.reversed().toString() )

                val linedataset = LineDataSet(follower_values, "LineDataSet") // reverse해주어야 출력됨!

                val color = sharedPreference.getString("color", "#363C90")
                val colorCode = Color.parseColor(color)
                linedataset.color = colorCode

                linedataset.lineWidth = 5f
                linedataset.setCircleColor(Color.MAGENTA)
                val linedata = LineData(linedataset)
                binding.monthLineChart.data = linedata

            }

            override fun onFailure(call: Call<FollowerStatisticsJsonResponse>, t: Throwable) {
                Log.d("mobileapp", t.toString())
                Toast.makeText(this@FollowerListActivity, "팔로워 조회 실패", Toast.LENGTH_LONG).show()

            }
        })

        binding.week.setTextColor(Color.MAGENTA)

        binding.month.setOnClickListener {
            binding.weekLineChart.visibility = View.GONE
            binding.monthLineChart.visibility = View.VISIBLE
            binding.month.setTextColor(Color.MAGENTA)
            binding.week.setTextColor(Color.WHITE)
        }

        binding.week.setOnClickListener {
            binding.weekLineChart.visibility = View.VISIBLE
            binding.monthLineChart.visibility = View.GONE
            binding.month.setTextColor(Color.WHITE)
            binding.week.setTextColor(Color.MAGENTA)
        }


        // 그래프
/*
        var line_values: ArrayList<Entry> = ArrayList()
        // line_values.add(Entry(1, 2))
        for(i in 0 until 10) {
            var v = Math.random() * 10
            line_values.add(Entry(i.toFloat(), v.toFloat()))
        }
        // Log.d("mobileapp", follower_values.toString())
        val linedataset = LineDataSet(line_values, "LineDataSet")
        linedataset.color = Color.GREEN
        linedataset.lineWidth = 3f
        linedataset.setCircleColor(Color.MAGENTA)
        val linedata = LineData(linedataset)
        binding.lineChart.data = linedata
        */



        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
        binding.week.setBackgroundColor(colorCode)
        binding.month.setBackgroundColor(colorCode)

        // 폰트 굵기 설정
        val fontStyle = sharedPreference.getString("font_style", "regular")

        var typeface: Typeface?
        if(fontStyle == "regular") {
            typeface = ResourcesCompat.getFont(this, R.font.nanum_regular)
        } else {
            typeface = ResourcesCompat.getFont(this, R.font.nanum_bold)
        }
        binding.currentUser.typeface = typeface
        binding.month.typeface = typeface
        binding.week.typeface = typeface
    }
}