package com.example.microserveis

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.microserveis.dao.ServerConfig
import com.example.microserveis.dao.ServerConfigDao

@Database(entities = [ServerConfig::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun configDao(): ServerConfigDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "microserveis-db"
                ).build().also { INSTANCE = it }
            }
    }
}
