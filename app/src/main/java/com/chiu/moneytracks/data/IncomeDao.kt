package com.chiu.moneytracks.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface IncomeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(incomeItem: NetIncome)

    @Update
    suspend fun update(incomeItem: NetIncome)

    @Delete
    suspend fun delete(incomeItem: NetIncome)

    @Query("SELECT * FROM net_income ORDER BY date")
    fun getIncomeItems(): Flow<List<NetIncome>>
}