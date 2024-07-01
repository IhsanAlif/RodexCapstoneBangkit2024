package com.alice.rodexapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alice.rodexapp.R
import com.alice.rodexapp.activity.ReportDetailActivity
import com.alice.rodexapp.databinding.ListItemBinding
import com.alice.rodexapp.model.UserModel
import com.alice.rodexapp.response.ListStoryItem
import com.alice.rodexapp.utils.UserDiffCallBack
import com.bumptech.glide.Glide

class ReportAdapter: PagingDataAdapter<ListStoryItem, ReportAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private var storyList = ArrayList<UserModel>()
    private var onUserClickListener: OnUserClickListener? = null

    fun setOnUserClickListener(listener: OnUserClickListener) {
        this.onUserClickListener = listener
    }

    fun setUser(newList: List<UserModel>) {
        val diffCallback = UserDiffCallBack(storyList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        storyList.clear()
        storyList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val layoutParams = holder.itemView.layoutParams as RecyclerView.LayoutParams
        val marginInPixels = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.spacing)
        if (position != 0) {
            layoutParams.topMargin = marginInPixels
        } else {
            layoutParams.topMargin = 0
        }
        holder.itemView.layoutParams = layoutParams

        val stories = getItem(position)
        if (stories != null) {
            holder.bind(stories)
        }
    }

    class MyViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(stories: ListStoryItem) {
            binding.tvRoadType.text = stories.name
            binding.tvDescription.text = stories.description
            Glide.with(itemView.context)
                .load(stories.photoUrl)
                .into(binding.ivRoad)
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, ReportDetailActivity::class.java)
                intent.putExtra(ReportDetailActivity.USER_ID, stories.id)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.ivRoad, "profile"),
                        Pair(binding.tvRoadType, "name"),
                        Pair(binding.tvDescription, "description")
                    )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    fun removeDivider(recyclerView: RecyclerView) {
        if (recyclerView.itemDecorationCount > 0) {
            recyclerView.removeItemDecorationAt(0)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface OnUserClickListener {
        fun onUserClick(user: UserModel)
    }
}
