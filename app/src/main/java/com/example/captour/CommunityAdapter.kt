package com.example.captour

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.captour.databinding.CommunityItemBinding
import com.example.captour.databinding.ItemRecyclerviewBinding
import java.text.SimpleDateFormat


class CommunityViewHolder(val binding: CommunityItemBinding) : RecyclerView.ViewHolder(binding.root)

class CommunityAdapter (val context: Context, val itemList: MutableList<CommunityData>): RecyclerView.Adapter<CommunityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CommunityViewHolder(CommunityItemBinding.inflate(layoutInflater))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    override fun onBindViewHolder(holder: CommunityViewHolder, position: Int) {
        val data = itemList.get(position)

        val imageRef = MyApplication.storage.reference.child("images/${data.docId}.jpg")

        imageRef.downloadUrl.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                holder.binding.imageView.visibility = View.VISIBLE
                Glide.with(context)
                    .load(task.result)
                    .into(holder.binding.imageView)

                holder.binding.root.setOnClickListener {
                    Toast.makeText(context, task.result.toString(), Toast.LENGTH_LONG).show()
                    Intent(context, DetailCommunityActivity::class.java).apply {
                        putExtra("title", data.title)
                        putExtra("email", data.email)
                        putExtra("dateTime", data.date_time)
                        putExtra("content", data.content)
                        putExtra("stars", data.stars)
                        putExtra("image", task.result.toString()) // URL을 문자열로 전달
                        Log.d("mobileapp", task.result.toString())
                    }.run {
                        context.startActivity(this)
                    }
                }
            }
        }

        // 이미지 없는 경우
        holder.binding.root.setOnClickListener {
            Toast.makeText(context, "Root", Toast.LENGTH_LONG).show()
            Intent(context, DetailCommunityActivity::class.java).apply {
                putExtra("title", data.title)
                putExtra("email", data.email)
                putExtra("dateTime", data.date_time)
                putExtra("content", data.content)
                putExtra("stars", data.stars)
            }.run {
                context.startActivity(this)
            }
        }

        holder.binding.run {
            email.text = data.email
            title.text = data.title
            dateTime.text = data.date_time
            content.text = data.content
            ratingBar.rating = data.stars.toFloat()

        }


    }
}
