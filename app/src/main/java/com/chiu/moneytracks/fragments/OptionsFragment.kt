package com.chiu.moneytracks.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.activityViewModels
import com.chiu.moneytracks.IncomeApplication
import com.chiu.moneytracks.IncomeViewModel
import com.chiu.moneytracks.IncomeViewModelFactory
import com.chiu.moneytracks.R
import com.chiu.moneytracks.databinding.FragmentOptionsBinding
import java.sql.Date
import java.time.LocalDate
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [OptionsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OptionsFragment : Fragment() {

    private val viewModel: IncomeViewModel by activityViewModels {
        IncomeViewModelFactory(
            (activity?.application as IncomeApplication).database.incomeDao()
        )
    }

    private var _binding: FragmentOptionsBinding? = null
    private val binding get() = _binding!!


    // monthOfYear has values from 0-11 for compatibility with Calendar#MONTH
    val dateSetListener = object : DatePickerDialog.OnDateSetListener {
        override fun onDateSet(
            view: DatePicker?,
            year: Int,
            monthOfYear: Int,
            dayOfMonth: Int
        ) {
            var dateChosen = LocalDate.of(
                year,
                monthOfYear + 1,
                dayOfMonth
            )
            binding.chooseDateButton.text = dateChosen.toString()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOptionsBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bind()
    }

    private fun bind() {
        binding.apply {
            dateSwitchOption.setOnClickListener {  }
            chooseDateButton.setOnClickListener { chooseDate() }
            atDayButton.setOnClickListener {  }
            afterDayButton.setOnClickListener {  }
            beforeDayButton.setOnClickListener {  }
        }
    }

    private fun chooseDate() {
        val today = LocalDate.now()

        DatePickerDialog(requireContext(), dateSetListener,
            today.year, today.monthValue, today.dayOfMonth)
            .show()
    }

    /* decides what to do based on what switch is currently at */
    private fun pressDateFilter(view: View) {
        // find which state the switch is in

        /* if the switch is active then apply the date filtering,
            else make sure that the filter is not active
         */
    }

    /* function that will take care of what to do on radio button active state */
    private fun chooseDateRange() {

    }
}