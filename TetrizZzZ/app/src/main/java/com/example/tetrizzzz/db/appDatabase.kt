package com.example.tetrizzzz.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tetrizzzz.db.Dao.ScoreDao
import com.example.tetrizzzz.models.Score

@Database(
    entities = [Score::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scoreDao(): ScoreDao

    companion object {
        fun getInstance(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "prueba-db"
            ).build()
        }
    }
}
