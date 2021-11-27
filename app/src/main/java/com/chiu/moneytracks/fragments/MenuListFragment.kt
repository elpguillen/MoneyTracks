package com.chiu.moneytracks.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.chiu.moneytracks.databinding.FragmentMenuListBinding

/**
 * A simple [Fragment] subclass.
 * Use the [MenuListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class MenuListFragment : Fragment() {

    private var _binding: FragmentMenuListBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMenuListBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.incomeButton.setOnClickListener {
            val action = MenuListFragmentDirections.actionMenuListFragmentToIncomeFragment()
            this.findNavController().navigate(action)
        }

        binding.expensesButton.setOnClickListener {
            val action = MenuListFragmentDirections.actionMenuListFragmentToExpensesFragment()
            this.findNavController().navigate(action)
        }

        binding.resultsButton.setOnClickListener {
            val action = MenuListFragmentDirections.actionMenuListFragmentToResultsFragment()
            this.findNavController().navigate(action)
        }

        binding.addIncomeItemButton.setOnClickListener {
            val action = MenuListFragmentDirections.actionMenuListFragmentToAddItemFragment()
            this.findNavController().navigate(action)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}