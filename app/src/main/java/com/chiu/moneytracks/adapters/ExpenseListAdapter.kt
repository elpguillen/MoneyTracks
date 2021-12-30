package com.chiu.moneytracks.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chiu.moneytracks.data.NetIncome
import com.chiu.moneytracks.databinding.ExpenseListItemBinding

class ExpenseListAdapter : ListAdapter<NetIncome, ExpenseListAdapter.ExpenseViewHolder>(DiffCallback) {

    class ExpenseViewHolder(private var binding: ExpenseListItemBinding) :
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        return ExpenseViewHolder(
            ExpenseListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}