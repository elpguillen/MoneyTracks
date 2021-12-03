package com.chiu.moneytracks.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.chiu.moneytracks.IncomeApplication
import com.chiu.moneytracks.IncomeViewModel
import com.chiu.moneytracks.IncomeViewModelFactory
import com.chiu.moneytracks.R
import com.chiu.moneytracks.data.NetIncome
import com.chiu.moneytracks.databinding.FragmentAddItemBinding

/**
 * A simple [Fragment] subclass.
 * Use the [AddItemFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddItemFragment : Fragment() {

    private val viewModel: IncomeViewModel by activityViewModels {
        IncomeViewModelFactory(
            (activity?.application as IncomeApplication).database
                .incomeDao()
        )
    }

    lateinit var incomeItem: NetIncome

    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addItemSubmitButton.setOnClickListener {
            addNewItem()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }


    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
                binding.itemDateText.text.toString(),
                binding.itemDescriptionText.text.toString(),
                binding.itemAmountText.text.toString()
        )
    }

    private fun addNewItem() {
        if (isEntryValid()) {
            viewModel.addNewItem(
                binding.itemDateText.text.toString(),
                binding.itemDescriptionText.text.toString(),
                binding.itemAmountText.text.toString()
            )

            val action = AddItemFragmentDirections.actionAddItemFragmentToMenuListFragment()
            findNavController().navigate(action)
        }
    }

}