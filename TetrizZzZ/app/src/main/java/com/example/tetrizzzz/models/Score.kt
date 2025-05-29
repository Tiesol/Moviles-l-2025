package com.example.tetrizzzz.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Score(
    var nickName: String,
    var score: Int
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

