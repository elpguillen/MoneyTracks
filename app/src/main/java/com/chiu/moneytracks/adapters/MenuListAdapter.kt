package com.chiu.moneytracks.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.chiu.moneytracks.R
import com.chiu.moneytracks.databinding.MenuGridListItemBinding


class MenuListAdapter(private val onItemClicked: (menuOption: String) -> Unit) :
    androidx.recyclerview.widget.ListAdapter<String, MenuListAdapter.MenuListViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MenuListAdapter.MenuListViewHolder {
        val viewHolder = MenuListViewHolder(
            MenuGridListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            onItemClicked(getItem(position))
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: MenuListAdapter.MenuListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MenuListViewHolder(private var binding: MenuGridListItemBinding
        ): RecyclerView.ViewHolder(binding.root) {
            fun bind(optionLabel: String) {
                binding.menuItem.text = optionLabel
            }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }

}