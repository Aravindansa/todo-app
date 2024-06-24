package com.sa.mytodo.data.data_sources.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sa.mytodo.domain.model.Todo

@Database(entities = [Todo::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun appDao(): ToDoDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context,
                    AppDatabase::class.java,
                    "dataBase_news")
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
