package com.example.captour

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.captour.databinding.FollowItemBinding

class MyFollowViewHolder(val binding: FollowItemBinding) : RecyclerView.ViewHolder(binding.root)

class FollowAdapter(val datas: List<Follow>?): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun getItemCount(): Int {
        return datas?.size ?:0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MyFollowViewHolder(FollowItemBinding.inflate(LayoutInflater.from(parent.context), parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyFollowViewHolder).binding
        val followdata = datas!![position]

        binding.following.text = followdata.following

    }

}
