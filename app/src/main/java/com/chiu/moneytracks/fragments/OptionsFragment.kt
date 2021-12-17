package com.chiu.moneytracks.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.chiu.moneytracks.*
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
            val dateChosen = LocalDate.of(
                year,
                monthOfYear + 1,
                dayOfMonth
            ).toString()

            val sharedPref = activity?.getSharedPreferences(getString(R.string.shared_preference_file_key), Context.MODE_PRIVATE) ?: return

            with(sharedPref.edit()) {
                putString(getString(R.string.chosen_date), dateChosen)
                apply()
            }
            binding.chooseDateButton.text = dateChosen
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
        val sharedPref = activity?.getSharedPreferences(getString(R.string.shared_preference_file_key), Context.MODE_PRIVATE) ?: return

        binding.apply {
            val isDateSwitchChecked = sharedPref.getBoolean(getString(R.string.switch_state), false)
            dateSwitchOption.isChecked = isDateSwitchChecked
            chooseDateButton.text = sharedPref.getString(getString(R.string.chosen_date), LocalDate.now().toString())
            setDateFilterState()

            // make sure switch is back to position last left off at
            applyDateSwitchState(isDateSwitchChecked)

            dateSwitchOption.setOnCheckedChangeListener { _, isChecked ->
                applyDateSwitchState(isChecked)
            }

            chooseDateButton.setOnClickListener { chooseDate() }

            dateRadioGroup.setOnCheckedChangeListener { _, checkedId ->
                changeDateFilterState(checkedId)
            }
        }
    }

    private fun chooseDate() {
        val today = LocalDate.now()

        // LocalDate provides monthValue from 1-12, while DatePickerDialog handles the
        // the monthValue from 0-11
        DatePickerDialog(requireContext(), dateSetListener,
            today.year, today.monthValue - 1, today.dayOfMonth)
            .show()
    }

    /* function that will take care of what to do on radio button active state */
    private fun changeDateFilterState(checkedId: Int) {
        val filterState = when (checkedId) {
            R.id.before_day_button -> InvConstants.BEFORE_DAY_CHOSEN
            R.id.after_day_button -> InvConstants.AFTER_DAY_CHOSEN
            else -> InvConstants.AT_DAY_CHOSEN
        }
        saveDateFilterToPref(filterState)
    }

    /* decides what to do based on what switch is currently at */
    private fun applyDateSwitchState(isChecked: Boolean) {

        val sharedPref = activity?.getSharedPreferences(getString(R.string.shared_preference_file_key), Context.MODE_PRIVATE) ?: return

        binding.apply {
            if (isChecked) {
                chooseDateButton.visibility = View.VISIBLE
                atDayButton.visibility = View.VISIBLE
                afterDayButton.visibility = View.VISIBLE
                beforeDayButton.visibility = View.VISIBLE
            } else {
                chooseDateButton.visibility = View.INVISIBLE
                atDayButton.visibility = View.INVISIBLE
                afterDayButton.visibility = View.INVISIBLE
                beforeDayButton.visibility = View.INVISIBLE
            }
        }

        with(sharedPref.edit()) {
            putBoolean(getString(R.string.switch_state), isChecked)
            apply()
        }
    }

    private fun saveDateFilterToPref(filterState: Int) {
        val sharedPref = activity?.getSharedPreferences(getString(R.string.shared_preference_file_key), Context.MODE_PRIVATE) ?: return

        with (sharedPref.edit()) {
            putInt(getString(R.string.date_filter_state_key), filterState)
            apply()
        }
    }

    private fun setDateFilterState() {
        val sharedPref = activity?.getSharedPreferences(getString(R.string.shared_preference_file_key), Context.MODE_PRIVATE) ?: return

        val dateFilterState = sharedPref.getInt(getString(R.string.date_filter_state_key), InvConstants.AT_DAY_CHOSEN)

        // based on dateFilterState set the appropriate radio button to active

        when (dateFilterState) {
            InvConstants.AT_DAY_CHOSEN -> binding.atDayButton.isChecked = true
            InvConstants.AFTER_DAY_CHOSEN -> binding.afterDayButton.isChecked = true
            else -> binding.beforeDayButton.isChecked = true
        }
    }
}