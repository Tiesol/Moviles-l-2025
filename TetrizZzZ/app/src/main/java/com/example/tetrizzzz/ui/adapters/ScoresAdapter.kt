package com.example.tetrizzzz.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tetrizzzz.databinding.ScoreItemLayoutBinding
import com.example.tetrizzzz.models.Score

class ScoreAdapter(
    var scores: ArrayList<Score>
) : RecyclerView.Adapter<ScoreAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ScoreItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = scores.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(scores[position])
    }

    fun setData(newScores: List<Score>) {
        scores.clear()
        scores.addAll(newScores)
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ScoreItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(score: Score) {
            binding.txtNickname.text = score.nickName
            binding.txtScore.text = score.score.toString()
        }
    }
}
