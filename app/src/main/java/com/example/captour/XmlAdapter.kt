package com.example.captour

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.res.ResourcesCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.captour.databinding.ItemMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class XmlViewHolder(val binding: ItemMainBinding): RecyclerView.ViewHolder(binding.root)
class XmlAdapter(val datas: MutableList<myXmlItem>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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
            val context = holder.itemView.context // Context 얻기
            // 지도 연결
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=${model.galPhotographyLocation}"))
            context.startActivity(intent)
            true
        }

        val context = holder.itemView.context

        // 폰트 사이즈 설정
        sharedPreference = PreferenceManager.getDefaultSharedPreferences(context)
        val fontSize = sharedPreference.getInt("font_size", 16)

        binding.galTitle.textSize = fontSize + 10f
        binding.galCreatedtime.textSize = fontSize + 1f
        binding.galPhotographer.textSize = fontSize + 1f
        binding.galPhotographyLocation.textSize = fontSize + 1f
        binding.searchYoutube.textSize = fontSize + 1f
        binding.galSearchKeyword.textSize = fontSize + 1f

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


    }
}