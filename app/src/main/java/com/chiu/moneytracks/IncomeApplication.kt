package com.chiu.moneytracks

import android.app.Application
import androidx.room.Room
import com.chiu.moneytracks.data.NetIncomeRoomDatabase

class IncomeApplication : Application() {

    val database: NetIncomeRoomDatabase by lazy { NetIncomeRoomDatabase.getDatabase(this) }

    // used to determine what radio button is selected at time of submission
    var typeSubmission: Int = InvConstants.INCOME_TYPE

    /*var database: NetIncomeRoomDatabase? = null

    fun getDatabase(): NetIncomeRoomDatabase? {
        return database
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(applicationContext,
            NetIncomeRoomDatabase::class.java, "database").allowMainThreadQueries().build()
    }*/
}