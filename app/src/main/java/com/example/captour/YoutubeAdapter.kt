package com.example.captour

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.captour.databinding.YoutubeItemBinding
import java.text.SimpleDateFormat

class MyViewHolder(val binding: YoutubeItemBinding) : RecyclerView.ViewHolder(binding.root)

class YoutubeAdapter(val datas: List<VideoItem>?): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun getItemCount(): Int {
        return datas?.size ?:0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MyViewHolder(YoutubeItemBinding.inflate(LayoutInflater.from(parent.context), parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyViewHolder).binding
        val youtubeData = datas!![position]

        Glide.with(binding.root)
            .load(youtubeData.snippet.thumbnails.default.url)
            // .override(400, 300)
            .into(binding.videoThumbnail)

        binding.videoDescription.text = youtubeData.snippet.description
        binding.videoTitle.text = youtubeData.snippet.title
    }

}
