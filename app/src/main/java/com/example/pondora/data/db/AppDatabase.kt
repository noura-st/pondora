package com.example.pondora.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pondora.data.db.dao.ScoreDao
import com.example.pondora.data.db.dao.TaskDao
import com.example.pondora.data.db.dao.UserDao
import com.example.pondora.data.db.entity.ScoreEntity
import com.example.pondora.data.db.entity.TaskEntity
import com.example.pondora.data.db.entity.UserEntity
import com.example.pondora.util.Constants

@Database(
    entities = [TaskEntity::class, ScoreEntity::class, UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
    abstract fun scoreDao(): ScoreDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    Constants.DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}