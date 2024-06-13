package com.example.captour

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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

        holder.binding.run {
            email.text = data.email
            title.text = data.title
            dateTime.text = data.date_time
            content.text = data.content
            ratingBar.rating = data.stars.toFloat()
        }

        /*
        val imageRef = MyApplication.storage.reference.child("images/${data.docId}.jpg")
        imageRef.downloadUrl.addOnCompleteListener{ task ->
            if(task.isSuccessful){
                holder.binding.itemImageView.visibility = View.VISIBLE
                Glide.with(context)
                    .load(task.result)
                    .into(holder.binding.itemImageView)
            }

        }

         */
    }
}
