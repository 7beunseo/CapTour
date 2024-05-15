package com.example.captour

import android.R
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.captour.databinding.YoutubeItemBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


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

        /*
        Glide.with(binding.root)
            .load(youtubeData.snippet.thumbnails.default.url)
            // .override(400, 300)
            .into(binding.videoThumbnail)

         */
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
                val videoId = youtubeData.id.toString() //재생을 원하는 YouTube 비디오의 videoID
                youTubePlayer.loadVideo(videoId, 0f)
            }
        })

         */


        binding.videoDescription.text = youtubeData.snippet.description
        binding.videoTitle.text = youtubeData.snippet.title
    }

}
