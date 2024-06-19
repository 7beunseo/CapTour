package com.example.captour

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.res.ResourcesCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.captour.databinding.ItemMainBinding
import com.google.android.play.integrity.internal.al
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.File


class XmlViewHolder(val binding: ItemMainBinding): RecyclerView.ViewHolder(binding.root)
class XmlAdapter(val datas: MutableList<myXmlItem>, val address: String): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var sharedPreference: SharedPreferences

    override fun getItemCount(): Int {
        return datas?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return XmlViewHolder(ItemMainBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as XmlViewHolder).binding
        val model = datas!![position]

        val context = holder.itemView.context // context 얻기
        sharedPreference = PreferenceManager.getDefaultSharedPreferences(context)

        val myMemo = sharedPreference.getString("myMemeo", "서울역")

        // 위치와 정보 가져오기
        val retrofit = Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/distancematrix/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(NetworkService::class.java)

        val call =
            apiService.getDistanceDuration(
                origins = myMemo.toString(),
                destinations = model.galPhotographyLocation.toString()
            )

        call?.enqueue(object : Callback<LocationJsonResponse> {
            override fun onResponse(call: Call<LocationJsonResponse>, response: Response<LocationJsonResponse>) {
                response?.toString()?.let { Log.d("mobileapp", it) }
                val locationData = response.body()
                locationData?.rows?.forEach { row ->
                    row.elements.forEach { element ->
                        val distanceText = element.distance.text
                        val durationText = element.duration.text
                        Log.d("MainActivity", "거리: $distanceText, 시간: $durationText")
                        binding.galDistanceDuration.text = "${myMemo} 기준 : 거리: $distanceText, 시간: $durationText"
                    }
                }
            }

            override fun onFailure(call: Call<LocationJsonResponse>, t: Throwable) {
                Toast.makeText(context.applicationContext, "거리 데이터 불러오기 실패", Toast.LENGTH_LONG).show()
            }
        })

        binding.galTitle.text = model.galTitle
        binding.galCreatedtime.text = model.galCreatedtime
        binding.galPhotographyLocation.text = model.galPhotographyLocation + " (위치를 보려면 클릭)"
        binding.galPhotographer.text = model.galPhotographer
        binding.galSearchKeyword.text = model.galSearchKeyword

        Glide.with(binding.root)
            .load(model.galWebImageUrl)
            // .override(400, 300)
            .into(binding.image)

        binding.root.setOnClickListener{

            val context = holder.itemView.context
            val intent = Intent(context, YoutubeActivity::class.java)
            intent.putExtra("videoTitle", model.galTitle, )

            context.startActivity(intent)
            true

        }

        binding.galPhotographyLocation.setOnClickListener {
            // 지도 연결
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(
                "https://www.google.com/maps/search/?api=1&query=${model.galPhotographyLocation}"))
            context.startActivity(intent)
            true
        }

        // 폰트 사이즈 설정
        sharedPreference = PreferenceManager.getDefaultSharedPreferences(context)
        val fontSize = sharedPreference.getInt("font_size", 16)

        binding.galTitle.textSize = fontSize + 10f
        binding.galCreatedtime.textSize = fontSize + 1f
        binding.galPhotographer.textSize = fontSize + 1f
        binding.galPhotographyLocation.textSize = fontSize + 1f
        binding.searchYoutube.textSize = fontSize + 1f
        binding.galSearchKeyword.textSize = fontSize + 1f
        binding.galDistanceDuration.textSize = fontSize + 1f

        // 폰트 굵기 설정
        val fontStyle = sharedPreference.getString("font_style", "regular")

        var typeface: Typeface?
        if(fontStyle == "regular") {
            typeface = ResourcesCompat.getFont(context, R.font.nanum_regular)
        } else {
            typeface = ResourcesCompat.getFont(context, R.font.nanum_bold)
        }

        binding.galTitle.typeface = typeface
        binding.galPhotographyLocation.typeface = typeface
        binding.galPhotographer.typeface = typeface
        binding.searchYoutube.typeface = typeface
        binding.galSearchKeyword.typeface = typeface
        binding.galCreatedtime.typeface = typeface
        binding.galDistanceDuration.typeface = typeface

        // 색상 설정
        val color = sharedPreference.getString("color", "#363C90")
        val colorCode = Color.parseColor(color)
        val colorStateList = ColorStateList.valueOf(colorCode)
        binding.galTitle.setTextColor(colorCode)

    }
}