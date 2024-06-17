package com.example.captour

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.example.captour.databinding.FollowerItemBinding

class MyFollowerViewHolder(val binding: FollowerItemBinding) : RecyclerView.ViewHolder(binding.root)

class FollowerAdapter(val datas: List<Follow>?): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    lateinit var sharedPreference: SharedPreferences

    override fun getItemCount(): Int {
        return datas?.size ?:0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MyFollowerViewHolder(FollowerItemBinding.inflate(LayoutInflater.from(parent.context), parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyFollowerViewHolder).binding
        val followdata = datas!![position]

        binding.follower.text = followdata.follower

        val context = holder.itemView.context

        binding.follower.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:${followdata.following}")
            }
            context.startActivity(intent)
        }

        sharedPreference = PreferenceManager.getDefaultSharedPreferences(context)
        val fontSize = sharedPreference.getInt("font_size", 16)

        binding.follower.textSize = fontSize + 1f
    }
}
