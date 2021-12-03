package com.chiu.moneytracks.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chiu.moneytracks.data.NetIncome
import com.chiu.moneytracks.databinding.IncomeListItemBinding

class IncomeListAdapter : ListAdapter<NetIncome, IncomeListAdapter.IncomeViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IncomeViewHolder {
        return IncomeViewHolder(
            IncomeListItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: IncomeViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class IncomeViewHolder(private var binding: IncomeListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(incomeItem: NetIncome) {
            binding.listItemDate.text = incomeItem.date
            binding.listItemDescription.text = incomeItem.description
            binding.listItemAmount.text = incomeItem.amount.toString()
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
}