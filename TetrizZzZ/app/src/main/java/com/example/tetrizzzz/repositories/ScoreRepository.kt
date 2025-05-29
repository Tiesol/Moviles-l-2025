package com.example.tetrizzzz.repositories

import android.content.Context
import com.example.tetrizzzz.db.AppDatabase
import com.example.tetrizzzz.models.Score

object ScoreRepository {
    suspend fun insert(context: Context, score: Score) {
        AppDatabase.getInstance(context).scoreDao().insertScore(score)
    }

    suspend fun getAll(context: Context): List<Score> {
        return AppDatabase.getInstance(context).scoreDao().getAllScores()
    }
}
