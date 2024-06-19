package com.example.captour

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.captour.databinding.CommunityItemBinding


class CommunityViewHolder(val binding: CommunityItemBinding) : RecyclerView.ViewHolder(binding.root)

class CommunityAdapter (val context: Context, val itemList: MutableList<CommunityData>): RecyclerView.Adapter<CommunityViewHolder>() {
    lateinit var sharedPreference: SharedPreferences

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CommunityViewHolder(CommunityItemBinding.inflate(layoutInflater))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    override fun onBindViewHolder(holder: CommunityViewHolder, position: Int) {
        val data = itemList.get(position)

        val context = holder.itemView.context
        sharedPreference = PreferenceManager.getDefaultSharedPreferences(context)
        val fontSize = sharedPreference.getInt("font_size", 16)

        holder.binding.title.textSize = fontSize + 1f
        // holder.binding.title.textSize = fontSize + 10f

        val imageRef = MyApplication.storage.reference.child("images/${data.docId}.jpg")

        imageRef.downloadUrl.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                holder.binding.imageView.visibility = View.VISIBLE
                Glide.with(context)
                    .load(task.result)
                    .into(holder.binding.imageView)

                // 이미지가 있는 경우
                holder.binding.root.setOnClickListener {
                    Intent(context, CommunityDetailActivity::class.java).apply {
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
            else {
                // 이미지 없는 경우
                holder.binding.root.setOnClickListener {
                    Intent(context, CommunityDetailActivity::class.java).apply {
                        putExtra("title", data.title)
                        putExtra("email", data.email)
                        putExtra("dateTime", data.date_time)
                        putExtra("content", data.content)
                        putExtra("stars", data.stars)
                    }.run {
                        context.startActivity(this)
                    }
                }
            }
        }.addOnFailureListener {task ->

        }

        holder.binding.run {
            email.text = data.email
            title.text = data.title
            dateTime.text = data.date_time
            content.text = data.content
            ratingBar.rating = data.stars.toFloat()

        }

        // 폰트 굵기 설정
        val fontStyle = sharedPreference.getString("font_style", "regular")

        var typeface: Typeface?
        if(fontStyle == "regular") {
            typeface = ResourcesCompat.getFont(context, R.font.nanum_regular)
        } else {
            typeface = ResourcesCompat.getFont(context, R.font.nanum_bold)
        }

        holder.binding.title.typeface = typeface
    }
}
