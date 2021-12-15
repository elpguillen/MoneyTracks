package com.chiu.moneytracks.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chiu.moneytracks.IncomeApplication
import com.chiu.moneytracks.IncomeViewModel
import com.chiu.moneytracks.IncomeViewModelFactory
import com.chiu.moneytracks.R
import com.chiu.moneytracks.adapters.ResultsListAdapter
import com.chiu.moneytracks.databinding.FragmentResultsBinding

/**
 * A simple [Fragment] subclass.
 * Use the [ResultsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResultsFragment : Fragment() {

    private val viewModel: IncomeViewModel by activityViewModels {
        IncomeViewModelFactory(
            (activity?.application as IncomeApplication).database.incomeDao()
        )
    }

    private var _binding: FragmentResultsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResultsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val resultsAdapter = ResultsListAdapter()

        binding.resultsRecyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.resultsRecyclerView.adapter = resultsAdapter

        // initialize percent label and income label to default values to show a temp value
        // in case not initialized later
        binding.percentSavedLabel.text = "0.0%"
        binding.netIncomeLabel.text = "0.0"

        viewModel.totalSum.observe(this.viewLifecycleOwner) {
            binding.netIncomeLabel.text = it.toString()
        }

        viewModel.allItems.observe(this.viewLifecycleOwner) {
            items -> items.let {
                resultsAdapter.submitList(it)
            }
        }
    }

}