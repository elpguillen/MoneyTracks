package com.chiu.moneytracks

import androidx.lifecycle.*
import com.chiu.moneytracks.data.IncomeDao
import com.chiu.moneytracks.data.NetIncome
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.coroutines.coroutineContext

class IncomeViewModel(private val dao: IncomeDao) : ViewModel() {

    val allItems: LiveData<List<NetIncome>> = dao.getAllItems().asLiveData()
    val incomeItems: LiveData<List<NetIncome>> = dao.getIncomeItems().asLiveData()
    val expenseItems: LiveData<List<NetIncome>> = dao.getExpenseItems().asLiveData()

    lateinit var allDateFilteredItems: LiveData<List<NetIncome>>

    val totalSum: LiveData<Double> = dao.getTotalSum().asLiveData()
    val numItems: LiveData<Int> = dao.getNumItems().asLiveData()

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

    // methods below will be responsible for filtering data
    fun getItemsByDateFilter(date: String = "2022-01-01", filterOption: Int = 4): LiveData<List<NetIncome>> {
            val items =  when (filterOption) {
                InvConstants.AFTER_DAY_CHOSEN ->  dao.getItemsAfterDate(date).asLiveData()
                InvConstants.BEFORE_DAY_CHOSEN -> dao.getItemsBeforeDate(date).asLiveData()
                else -> dao.getItemsAtDate(date).asLiveData()
            }
            return items
    }

    fun applyDateFilter(date: String, filterOption: Int) {
        viewModelScope.launch {
            allDateFilteredItems = getItemsByDateFilter(date, filterOption)
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