package com.chiu.moneytracks

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve NavController from the NavHostFragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Set up the action bar for use with the NavController
        setupActionBarWithNavController(this, navController)
    }

    /**
     * Handle navigation when the user chooses 'Up' from the action bar
     */
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    fun onSubmitIncomeClicked(view: View) {
        val incomeApplication = (application as IncomeApplication)

        if (view is RadioButton) {
            var isViewChecked = view.isChecked

            when (view.id) {
                R.id.income_radio_button -> if (isViewChecked) incomeApplication.typeSubmission = InvConstants.INCOME_TYPE
                else -> incomeApplication.typeSubmission = InvConstants.EXPENSE_TYPE
            }
        }
    }

    /* fun onDateFilterClick(view: View) {
        val sharedPref = this?.getSharedPreferences(getString(R.string.shared_preference_file_key), Context.MODE_PRIVATE)

        if (view is RadioButton) {
            var isViewChecked = view.isChecked

            when (view.id) {
                R.id.at_day_button -> with (sharedPref.edit()) {
                    putInt(getString(R.string.date_filter_at_state), )
                }
            }
        }
    } */

}