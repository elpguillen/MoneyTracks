package com.chiu.moneytracks.fragments

import android.content.Context
import android.icu.util.Calendar
import android.icu.util.LocaleData
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.chiu.moneytracks.*
import com.chiu.moneytracks.data.NetIncome
import com.chiu.moneytracks.databinding.FragmentAddItemBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.time.LocalDate
import java.util.*
import kotlin.math.absoluteValue

/**
 * A simple [Fragment] subclass.
 * Use the [AddItemFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddItemFragment : Fragment() {

    //private val incomeApplication = activity?.application as IncomeApplication

    private val viewModel: IncomeViewModel by activityViewModels {
        IncomeViewModelFactory(
            (activity?.application as IncomeApplication).database.incomeDao()
        )
    }

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

        val sharedPref = activity?.getSharedPreferences(getString(R.string.shared_preference_file_key), Context.MODE_PRIVATE) ?: return

        binding.itemDateLabel.setOnClickListener {
            displayDatePicker()
        }

        binding.addItemSubmitButton.setOnClickListener {
            addNewItem()
        }

        // make sure correct previous button state is active
        val prevIncomeState = sharedPref.getInt(getString(R.string.income_type_key), InvConstants.INCOME_TYPE)

        if (prevIncomeState == InvConstants.EXPENSE_TYPE) {
            binding.expenseRadioButton.isChecked = true
        } else {
            binding.incomeRadioButton.isChecked = true
        }

        val dateInUtcMilli = sharedPref.getLong(getString(R.string.date_selected_to_add_key), MaterialDatePicker.todayInUtcMilliseconds())
        val date = convertUtcMilliTimeToDate(dateInUtcMilli)
        binding.itemDateText.text = date
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }


    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
                binding.itemDateLabel.text.toString(),
                binding.itemDescriptionText.text.toString(),
                binding.itemAmountText.text.toString()
        )
    }

    private fun addNewItem() {

        val incomeApplication = (activity?.application) as IncomeApplication

        if (isEntryValid()) {

            val sharedPref = activity?.getSharedPreferences(getString(R.string.shared_preference_file_key), Context.MODE_PRIVATE) ?: return

            val dateInPref = sharedPref.getLong(getString(R.string.date_selected_to_add_key), MaterialDatePicker.todayInUtcMilliseconds())


            val date = convertUtcMilliTimeToDate(dateInPref)
            //val calendar = java.util.Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            //calendar.time = Date(dateInPref)



            //val date = "${calendar.get(java.util.Calendar.DAY_OF_MONTH)}-" +
            //                "${calendar.get(java.util.Calendar.MONTH) + 1}- ${calendar.get(java.util.Calendar.YEAR)}"
            // date should refer to the day selected from the date picker
            //var date = binding.itemDateLabel.text.toString()
            var description = binding.itemDescriptionText.text.toString()
            var amount = binding.itemAmountText.text.toString()

            var amountNum = amount.toDouble().absoluteValue

            if (sharedPref.getInt(getString(R.string.income_type_key), InvConstants.INCOME_TYPE) == InvConstants.EXPENSE_TYPE) {
                amountNum *= (-1.0)
            }

            /*if (incomeApplication.typeSubmission == InvConstants.EXPENSE_TYPE) {
                amountNum *= (-1.0)
            }*/

            amount = amountNum.toString()
            viewModel.addNewItem(date, description, amount)

            val action = AddItemFragmentDirections.actionAddItemFragmentToMenuListFragment()
            findNavController().navigate(action)
        }
    }

    /* Displays and manages click events for the date picker */
    private fun displayDatePicker() {

        // Get shared preferences to save the date that is chosen
        val sharedPref = activity?.getSharedPreferences(getString(R.string.shared_preference_file_key), Context.MODE_PRIVATE) ?: return
        val dateSelection = sharedPref.getLong(getString(R.string.date_selected_to_add_key), MaterialDatePicker.todayInUtcMilliseconds())

        // build and display the datePicker with the day previously selected (if none selected before, the default will be today)
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setSelection(dateSelection)
            .build()

        datePicker.show(parentFragmentManager, datePicker.toString())

        // when the user clicks on "OK", save the date chosen in shared preferences
        datePicker.addOnPositiveButtonClickListener {
            with(sharedPref.edit()) {
                binding.itemDateText.text = convertUtcMilliTimeToDate(it)
                putLong(getString(R.string.date_selected_to_add_key), it)
                apply()
            }
        }
    }

    private fun convertUtcMilliTimeToDate(time: Long): String {
        val calendar = java.util.Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.time = Date(time)

        val date = "${calendar.get(java.util.Calendar.MONTH) + 1}-" +
                "${calendar.get(java.util.Calendar.DAY_OF_MONTH)}-${calendar.get(java.util.Calendar.YEAR)}"

        return date
    }
}