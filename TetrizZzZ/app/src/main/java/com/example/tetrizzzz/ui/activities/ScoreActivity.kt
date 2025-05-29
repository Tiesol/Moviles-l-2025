package com.example.tetrizzzz.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tetrizzzz.databinding.ActivityScoreBinding
import com.example.tetrizzzz.models.Score
import com.example.tetrizzzz.ui.adapters.ScoreAdapter
import com.example.tetrizzzz.viewmodel.ScoreViewModel

class ScoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScoreBinding
    private val viewModel: ScoreViewModel by viewModels()
    private lateinit var adapter: ScoreAdapter
    private var scoreAlreadySaved = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val finalScore = intent.getIntExtra("FINAL_SCORE", 0)
        binding.inputNickname.hint = "Tu score: $finalScore"

        adapter = ScoreAdapter(arrayListOf())
        binding.ScoreRecycler.layoutManager = LinearLayoutManager(this)
        binding.ScoreRecycler.adapter = adapter

        // Cambiado: observe LiveData, no StateFlow
        viewModel.scoreList.observe(this) {
            adapter.setData(it)
        }

        binding.btnGuardar.setOnClickListener {
            if (!scoreAlreadySaved) {
                val nickname = binding.inputNickname.editText?.text.toString().trim()
                if (nickname.isNotEmpty()) {
                    val score = Score(nickName = nickname, score = finalScore)
                    viewModel.insertScore(this, score) {
                        scoreAlreadySaved = true
                        binding.btnGuardar.isEnabled = false
                    }
                }
            }
        }

        binding.btnReiniciar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        viewModel.loadScores(this)
    }
}
