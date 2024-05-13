package com.example.ch17_storage2

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.captour.databinding.ItemRecyclerviewBinding
import java.text.SimpleDateFormat


class MyViewHolder(val binding: ItemRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root)

class MyAdapter(val datas: MutableList<String>?): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun getItemCount(): Int {
        return datas?.size ?:0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MyViewHolder(ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyViewHolder).binding
        val data = datas!![position]
        binding.title.text = data.split("\n")[0]
        binding.content.text = data.split("\n")[0]
        val dataFormat = SimpleDateFormat("yyyy-MM-dd")
        binding.date.text = dataFormat.format(System.currentTimeMillis())
    }

}
