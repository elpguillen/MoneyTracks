package com.chiu.moneytracks.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chiu.moneytracks.*
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

        val dividerItemDecoration = DividerItemDecoration(binding.resultsRecyclerView.context,
            DividerItemDecoration.VERTICAL)

        binding.resultsRecyclerView.addItemDecoration(dividerItemDecoration)

        // initialize percent label and income label to default values to show a temp value
        // in case not initialized later
        binding.percentSavedLabel.text = "Percent Saved: 0.0%"
        binding.netIncomeLabel.text = "0.0"

        viewModel.totalSum.observe(this.viewLifecycleOwner) {
            binding.netIncomeLabel.text = it.toString()
        }

        val sharedPref = activity?.getSharedPreferences(getString(R.string.shared_preference_file_key), Context.MODE_PRIVATE) ?: return
        val isDateSwitchChecked = sharedPref.getBoolean(getString(R.string.switch_state), false)

        if (isDateSwitchChecked) {
            val prefDate = sharedPref.getString(getString(R.string.chosen_date), "2000-01-01") ?: "2000-01-01"
            val prefFilterState = sharedPref.getInt(getString(R.string.date_filter_state_key), InvConstants.AT_DAY_CHOSEN)

            viewModel.applyDateFilter(prefDate, prefFilterState)

            // Todo: handle situation when the list is empty
            viewModel.allDateFilteredItems.observe(this.viewLifecycleOwner) {
                items -> items.let {
                    resultsAdapter.submitList(it)
                }
            }

        } else {

            viewModel.allItems.observe(this.viewLifecycleOwner) {
                    items -> items.let {
                        resultsAdapter.submitList(it)
                    }
            }
        }
    }

}