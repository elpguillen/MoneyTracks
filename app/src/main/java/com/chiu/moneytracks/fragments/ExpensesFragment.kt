package com.chiu.moneytracks.fragments

import android.os.Bundle
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
import com.chiu.moneytracks.adapters.ExpenseListAdapter
import com.chiu.moneytracks.databinding.FragmentExpensesBinding

/**
 * A simple [Fragment] subclass.
 * Use the [ExpensesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ExpensesFragment : Fragment() {

    private val viewModel: IncomeViewModel by activityViewModels {
        IncomeViewModelFactory(
            (activity?.application as IncomeApplication).database.incomeDao()
        )
    }

    private var _binding: FragmentExpensesBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExpensesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val expenseAdapter = ExpenseListAdapter()

        binding.expenseRecyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.expenseRecyclerView.adapter = expenseAdapter

        val dividerItemDecoration = DividerItemDecoration(binding.expenseRecyclerView.context,
            DividerItemDecoration.VERTICAL)

        binding.expenseRecyclerView.addItemDecoration(dividerItemDecoration)

        viewModel.expenseItems.observe(this.viewLifecycleOwner) {
            items -> items.let {
                expenseAdapter.submitList(it)
            }
        }
    }
}