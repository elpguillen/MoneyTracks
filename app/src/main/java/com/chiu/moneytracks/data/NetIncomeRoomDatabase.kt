package com.chiu.moneytracks.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [NetIncome::class], version = 1, exportSchema = false)
abstract class NetIncomeRoomDatabase : RoomDatabase() {

    abstract fun incomeDao(): IncomeDao

    companion object {

        /**
         * INSTANCE will keep reference to the database whenever it is created.
         * This will help maintain a single instance of the database open.
         * Value of a volatile variable will never be cached. All writes and reads
         * will be done to and from main memory. This helps ensure the value of
         * INSTANCE is always up-to-date and the same for all execution threads.
         * Changes by one thread to INSTANCE are visible to all other threads
         * immediately.
         */
        @Volatile
        private var INSTANCE: NetIncomeRoomDatabase? = null

        /**
         * Gets the instance for the database if created or creates the database.
         * To ensure that only one thread of execution can enter block of code,
         * wrap the code to get the database inside a synchronized block so that
         * it helps eliminate race condition where multiple threads ask for a
         * database instance at the same time.
         *
         * @param context will be needed by the database builder
         *
         * @return the database as a NetIncomeRoomDatabase
         */
        fun getDatabase(context: Context): NetIncomeRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NetIncomeRoomDatabase::class.java,
                    "net_income_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance

                return instance
            }
        }
    }
}