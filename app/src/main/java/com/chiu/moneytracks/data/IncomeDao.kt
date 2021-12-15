package com.chiu.moneytracks.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.sql.Date
import java.util.*

@Dao
interface IncomeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(incomeItem: NetIncome)

    @Update
    suspend fun update(incomeItem: NetIncome)

    @Delete
    suspend fun delete(incomeItem: NetIncome)

    @Query("DELETE FROM net_income")
    suspend fun deleteAll()

    @Query("SELECT * FROM net_income ORDER BY date")
    fun getAllItems(): Flow<List<NetIncome>>

    @Query("SELECT * FROM net_income WHERE amount >= 0")
    fun getIncomeItems(): Flow<List<NetIncome>>

    @Query("SELECT * FROM net_income WHERE amount < 0")
    fun getExpenseItems(): Flow<List<NetIncome>>

    @Query("SELECT * FROM net_income WHERE date < :date")
    fun getItemsBeforeDate(date: String): Flow<List<NetIncome>>

    @Query("SELECT * FROM net_income WHERE date > :date")
    fun getItemsAfterDate(date: String): Flow<List<NetIncome>>

    @Query("SELECT * FROM net_income WHERE date = :date")
    fun getItemsAtDate(date: String): Flow<List<NetIncome>>

    @Query("SELECT SUM(amount) FROM net_income")
    fun getTotalSum(): Flow<Double>

    @Query("SELECT SUM(amount) FROM net_income WHERE amount > 0")
    fun getIncomeSum(): Flow<Double>

    @Query("SELECT SUM(amount) FROM net_income WHERE amount < 0")
    fun getExpenseSum(): Flow<Double>

    @Query("SELECT * FROM net_income WHERE description LIKE :phrase")
    fun findByDescription(phrase: String): Flow<List<NetIncome>>

}