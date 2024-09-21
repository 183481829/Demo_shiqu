package com.xyl.demo01.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xyl.demo01.R

class DifficultyAdapter(private val difficultyList: List<Int>,
                        private val listener: OnDifficultyClickListener): RecyclerView.Adapter<DifficultyAdapter.DifficultyViewHolder>() {
    interface OnDifficultyClickListener {
        fun onDifficultyClick(difficultyValue: Int)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DifficultyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_difficulty, parent, false)
        return DifficultyViewHolder(view)
    }

    override fun onBindViewHolder(holder: DifficultyViewHolder, position: Int) {
        val difficultyValue = difficultyList[position]
        holder.difficultyTextView.text = difficultyValue.toString()

        // 设置点击事件
        holder.itemView.setOnClickListener {
            listener.onDifficultyClick(difficultyValue)
        }
    }

    override fun getItemCount(): Int {
        return difficultyList.size
    }
    class DifficultyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val difficultyTextView: TextView = itemView.findViewById(R.id.difficultyTextView)
    }
}