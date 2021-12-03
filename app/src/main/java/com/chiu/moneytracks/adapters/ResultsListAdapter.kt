package com.chiu.moneytracks.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chiu.moneytracks.data.NetIncome
import com.chiu.moneytracks.databinding.ResultsListItemBinding

class ResultsListAdapter : ListAdapter<NetIncome, ResultsListAdapter.ResultsViewHolder>(DiffCallback) {

    class ResultsViewHolder(private var binding: ResultsListItemBinding):
        RecyclerView.ViewHolder(binding.root) {

            fun bind(netIncome: NetIncome) {
                binding.listItemDate.text = netIncome.date
                binding.listItemDescription.text = netIncome.description
                binding.listItemAmount.text = netIncome.amount.toString()
            }
        }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<NetIncome>() {
            override fun areItemsTheSame(oldItem: NetIncome, newItem: NetIncome): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: NetIncome, newItem: NetIncome): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultsViewHolder {
        return ResultsViewHolder(
            ResultsListItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: ResultsViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}