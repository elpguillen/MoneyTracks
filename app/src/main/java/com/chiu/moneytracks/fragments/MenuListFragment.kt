package com.chiu.moneytracks.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.chiu.moneytracks.*
import com.chiu.moneytracks.adapters.MenuListAdapter
import com.chiu.moneytracks.databinding.FragmentMenuGridListBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.time.LocalDate
import java.util.concurrent.ThreadLocalRandom

/**
 * A simple [Fragment] subclass.
 * Use the [MenuListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class MenuListFragment : Fragment() {

    //private var _binding: FragmentMenuListBinding? = null
    private var _binding: FragmentMenuGridListBinding? = null
    private val binding get() = _binding!!

    private var menuItems: MutableList<String> = mutableListOf()

    private val viewModel: IncomeViewModel by activityViewModels {
        IncomeViewModelFactory(
            (activity?.application as IncomeApplication).database.incomeDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // hide the action bar (but hides for all so try again)
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //_binding = FragmentMenuListBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        _binding = FragmentMenuGridListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //bind()

        val menuAdapter = MenuListAdapter({
            // based on string/button pressed choose correct path to go to next
            var action: NavDirections //= MenuListFragmentDirections.actionMenuListFragmentToResultsFragment()

            if (it == getString(R.string.delete_database)) {
                showConfirmationDialog()
            } else {
                action = getNavigationAction(it)
                view.findNavController().navigate(action)
            }
            //view.findNavController().navigate(action)
        })

        binding.menuGridRecyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)
        binding.menuGridRecyclerView.adapter = menuAdapter

        InvConstants.menu.let { (binding.menuGridRecyclerView.adapter as MenuListAdapter).submitList(it) }
    }

    /**
     * Displays an alert dialog to get the user's confirmation before deleting the item.
     */
    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.clearDatabase()
            }
            .show()
    }

    private fun bind() {
        binding.menuGridRecyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)
    }

    /*private fun bind() {
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

        binding.deleteButton.setOnClickListener {
            showConfirmationDialog()
        }

        binding.optionsButton.setOnClickListener {
            // move to OptionsFragment
            val action = MenuListFragmentDirections.actionMenuListFragmentToOptionsFragment()
            this.findNavController().navigate(action)
        }
    }*/

    private fun getNavigationAction(menuOption: String): NavDirections{
        return when(menuOption) {
            getString(R.string.earnings) -> MenuListFragmentDirections.actionMenuListFragmentToIncomeFragment()
            getString(R.string.expenses) -> MenuListFragmentDirections.actionMenuListFragmentToExpensesFragment()
            getString(R.string.results) -> MenuListFragmentDirections.actionMenuListFragmentToResultsFragment()
            "Options" -> MenuListFragmentDirections.actionMenuListFragmentToOptionsFragment()
            else -> MenuListFragmentDirections.actionMenuListFragmentToAddItemFragment()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun addRandomItems() {
        // date, description, amount
        var numItems: Int = 0

        viewModel.numItems.observe(this.viewLifecycleOwner) {
            numItems = it
        }

        for (i in numItems..30) {
            // get a random date
            val startDate = LocalDate.of(2000, 1, 1).toEpochDay()
            val endDate = LocalDate.of(2025, 1, 1).toEpochDay()

            val randomDateLong = ThreadLocalRandom.current().nextLong(startDate, endDate)
            val randomDate = LocalDate.ofEpochDay(randomDateLong)

            //all descriptions will be the same for simplicity
            val description = "Randomly generated item"

            // get random amount
            val minVal = -2000
            val maxVal = 10000

            val randomAmount = ThreadLocalRandom.current().nextInt(minVal, maxVal)

            viewModel.addNewItem(randomDate.toString(), description, randomAmount.toString())
        }
    }
}