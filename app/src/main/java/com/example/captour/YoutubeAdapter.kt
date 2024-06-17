package com.example.captour

import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.captour.databinding.YoutubeItemBinding


class MyViewHolder(val binding: YoutubeItemBinding) : RecyclerView.ViewHolder(binding.root)

class YoutubeAdapter(val datas: List<VideoItem>?): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    lateinit var sharedPreference: SharedPreferences

    override fun getItemCount(): Int {
        return datas?.size ?:0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MyViewHolder(YoutubeItemBinding.inflate(LayoutInflater.from(parent.context), parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyViewHolder).binding
        val youtubeData = datas!![position]

        /*
        val context = holder.itemView.context
        val player = ExoPlayer.Builder(context).build()
        binding.playerView.player = player

        val mediaItem: MediaItem = MediaItem.fromUri("https://www.youtube.com/embed/" + youtubeData.id)
        player.setMediaItem(mediaItem)

         */
        // getLifecycle().addObserver(binding.playerView)

        /*
        Log.d("mobileapp", youtubeData.id.toString())
        binding.playerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val videoId = youtubeData.id.toString() // 유튜브 재생 안됨 -> 섬네일로 바꿈
                youTubePlayer.loadVideo(videoId, 0f)
            }
        })

         */

        Log.d("mobileapp", youtubeData.snippet.thumbnails.toString())
        Glide.with(binding.root)
            .load(youtubeData.snippet.thumbnails.default.url)
            .into(binding.youtubeThumbnail)

        val context = holder.itemView.context

        sharedPreference = PreferenceManager.getDefaultSharedPreferences(context)
        val fontSize = sharedPreference.getInt("font_size", 16)

        holder.binding.videoTitle.textSize = fontSize + 4f
        holder.binding.videoDescription.textSize = fontSize + 1f

        binding.root.setOnClickListener {
            Intent(context, YoutubeDetailActivity::class.java).apply {
                putExtra("title", youtubeData.snippet.title)
                putExtra("description", youtubeData.snippet.description)
                putExtra("videoId", youtubeData.id.videoId.toString())
            }.run {
                context.startActivity(this)
            }
        }

        binding.videoDescription.text = youtubeData.snippet.description
        binding.videoTitle.text = youtubeData.snippet.title
    }

}
