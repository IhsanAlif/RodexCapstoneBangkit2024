package com.alice.rodexapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alice.rodexapp.activity.DetailActivity
import com.alice.rodexapp.databinding.ListItemBinding
import com.alice.rodexapp.viewmodel.AboutRoad

class AboutRoadAdapter(private val listAboutRoad: ArrayList<AboutRoad>) : RecyclerView.Adapter<AboutRoadAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(var binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listAboutRoad.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (name, description, photo) = listAboutRoad[position]
        holder.binding.ivRoad.setImageResource(photo)
        holder.binding.tvRoadType.text = name
        holder.binding.tvDescription.text = description

        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            intentDetail.putExtra("key_aboutRoad", listAboutRoad[holder.absoluteAdapterPosition])
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: AboutRoad)
    }
}