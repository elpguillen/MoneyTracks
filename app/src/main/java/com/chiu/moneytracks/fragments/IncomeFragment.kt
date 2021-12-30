package com.chiu.moneytracks.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.chiu.moneytracks.IncomeApplication
import com.chiu.moneytracks.IncomeViewModel
import com.chiu.moneytracks.IncomeViewModelFactory
import com.chiu.moneytracks.adapters.IncomeListAdapter
import com.chiu.moneytracks.databinding.FragmentIncomeBinding

/**
 * A simple [Fragment] subclass.
 * Use the [IncomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class IncomeFragment : Fragment() {

    private val viewModel: IncomeViewModel by activityViewModels {
        IncomeViewModelFactory(
            (activity?.application as IncomeApplication).database.incomeDao()
        )
    }

    private var _binding: FragmentIncomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIncomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val incomeAdapter = IncomeListAdapter()

        binding.incomeRecyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.incomeRecyclerView.adapter = incomeAdapter

        val dividerItemDecoration = DividerItemDecoration(binding.incomeRecyclerView.context,
                                                            DividerItemDecoration.VERTICAL)

        binding.incomeRecyclerView.addItemDecoration(dividerItemDecoration)

        viewModel.incomeItems.observe(this.viewLifecycleOwner) {
            items -> items.let {
                incomeAdapter.submitList(it)
            }
        }
    }

}