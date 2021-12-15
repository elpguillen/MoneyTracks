package com.chiu.moneytracks

import androidx.lifecycle.*
import com.chiu.moneytracks.data.IncomeDao
import com.chiu.moneytracks.data.NetIncome
import kotlinx.coroutines.launch
import java.util.*

class IncomeViewModel(private val dao: IncomeDao) : ViewModel() {

    val allItems: LiveData<List<NetIncome>> = dao.getAllItems().asLiveData()
    val incomeItems: LiveData<List<NetIncome>> = dao.getIncomeItems().asLiveData()
    val expenseItems: LiveData<List<NetIncome>> = dao.getExpenseItems().asLiveData()

    val dateFilter: Boolean = false

    val totalSum: LiveData<Double> = dao.getTotalSum().asLiveData()

    /* Inserts a new item into the database */
    fun addNewItem(itemDate: String, itemDescription: String, itemAmount: String) {
        val newIncomeItem = getNewIncomeEntry(itemDate, itemDescription, itemAmount)
        insertItem(newIncomeItem)
    }

    /* Inserts a new item into the database */
    private fun insertItem(incomeItem: NetIncome) {
        viewModelScope.launch {
            dao.insert(incomeItem)
        }
    }

    /* Deletes item from the database */
    private fun deleteItem(incomeItem: NetIncome) {
        viewModelScope.launch {
            dao.delete(incomeItem)
        }
    }

    /* Deletes all items from the database*/
    private fun deleteAllItems() {
        viewModelScope.launch {
            dao.deleteAll()
        }
    }

    /* Updates item in the database */
    private fun updateItem(incomeItem: NetIncome) {
        viewModelScope.launch {
            dao.update(incomeItem)
        }
    }

    private fun getNewIncomeEntry(itemDate: String, itemDescription: String, itemAmount: String): NetIncome {
        return NetIncome(
            date = itemDate,
            description = itemDescription,
            amount = itemAmount.toDouble()
        )
    }

    fun isEntryValid(itemDate: String, itemDescription: String, itemAmount: String): Boolean {
        if (itemDate.isBlank() || itemDescription.isBlank() || itemAmount.isBlank()) {
            return false
        }

        return true
    }

    /* Clears all the items in the database*/
    fun clearDatabase() {
        deleteAllItems()
    }

}

class IncomeViewModelFactory(private val dao: IncomeDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IncomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return IncomeViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}