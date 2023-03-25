package com.novandi.githubuser.ui.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.novandi.githubuser.api.UserItems
import com.novandi.githubuser.databinding.ItemRowFollowBinding

class ListFollowAdapter(private val listFollow: List<UserItems>) : RecyclerView.Adapter<ListFollowAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    class ListViewHolder(var binding: ItemRowFollowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val binding = ItemRowFollowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: UserItems)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (usernameItem, avatarUrlItem) = listFollow[position]
        Glide.with(holder.itemView.context).load(avatarUrlItem).into(holder.binding.photoFollow)
        holder.binding.usernameFollow.text = usernameItem
        holder.binding.cardFollow.setOnClickListener {
            onItemClickCallback.onItemClicked(listFollow[holder.adapterPosition])
        }
    }

    override fun getItemCount() = listFollow.size
}