package com.example.tetrizzzz.db.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.tetrizzzz.models.Score

@Dao
interface ScoreDao {
    @Query("SELECT * FROM Score ORDER BY score DESC")
    suspend fun getAllScores(): List<Score>
    @Insert
    suspend fun insertScore(score: Score): Long
}