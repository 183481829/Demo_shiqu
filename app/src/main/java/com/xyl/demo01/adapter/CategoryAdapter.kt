package com.xyl.demo01.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.xyl.demo01.R
import com.xyl.demo01.data.ArticleType


class CategoryAdapter(private val articleTypes: List<ArticleType>,
                      private val listener: OnItemClickListener
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    // 保存当前选中的项
    private var selectedPosition = -1
    interface OnItemClickListener {
        fun onItemClick(articleType: ArticleType)
    }
        override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_article_category, parent, false)
            return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder,  position: Int) {
        val featuredArticleType = ArticleType(0, "精选阅读")
        val modifiedArticleTypes = mutableListOf(featuredArticleType) // 创建新列表
        modifiedArticleTypes.addAll(articleTypes)
        // 获取当前的 ArticleType 并绑定 type 属性到 TextView 上
        val articleType = modifiedArticleTypes[holder.adapterPosition]
        holder.categoryName.text = articleType.type

        // 设置背景和字体颜色
        if (position == selectedPosition) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.blue))
            holder.categoryName.setTextColor(ContextCompat.getColor(holder.itemView.context, android.R.color.white))
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.gray))
            holder.categoryName.setTextColor(ContextCompat.getColor(holder.itemView.context, android.R.color.black))
        }
        // 设置点击事件
        holder.itemView.setOnClickListener {
            listener.onItemClick(articleType)

            // 更新选中的位置并刷新
            val previousPosition = selectedPosition
            selectedPosition = holder.adapterPosition

            // 刷新之前的项和当前项
            notifyItemChanged(previousPosition)
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return articleTypes.size
    }
    // ViewHolder 类，持有每个列表项的视图引用
    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName: TextView = itemView.findViewById(R.id.categoryName)
    }
}