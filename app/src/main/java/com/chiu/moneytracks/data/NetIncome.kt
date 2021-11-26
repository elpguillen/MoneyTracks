package com.chiu.moneytracks.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "net_income")
data class NetIncome (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String,
    val description: String,
    val amount: Double
)

