package com.example.tetrizzzz.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tetrizzzz.models.Score
import com.example.tetrizzzz.repositories.ScoreRepository
import kotlinx.coroutines.launch

class ScoreViewModel : ViewModel() {
    private val _scoreList: MutableLiveData<List<Score>> = MutableLiveData(emptyList())
    val scoreList: LiveData<List<Score>> = _scoreList

    fun loadScores(context: Context) {
        viewModelScope.launch {
            val scores = ScoreRepository.getAll(context)
            _scoreList.postValue(scores)
        }
    }

    fun insertScore(context: Context, score: Score, onSaved: () -> Unit) {
        viewModelScope.launch {
            ScoreRepository.insert(context, score)
            loadScores(context)
            onSaved()
        }
    }
}
