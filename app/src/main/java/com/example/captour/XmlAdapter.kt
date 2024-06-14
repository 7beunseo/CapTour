package com.example.captour

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
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


    }
}