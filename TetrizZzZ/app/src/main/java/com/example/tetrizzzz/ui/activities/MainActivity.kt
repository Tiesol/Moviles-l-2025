package com.example.tetrizzzz.ui.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tetrizzzz.R
import com.example.tetrizzzz.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnRotar.setOnClickListener {
            binding.tableroView2.rotarFigura()
        }

        binding.btnAbajo.setOnClickListener {
            binding.tableroView2.moverFiguraHastaFondo()
        }

        binding.tableroView2.onScoreUpdated = { nuevoPuntaje ->
            binding.txtScore.text = "Score: $nuevoPuntaje"
        }
    }
}
