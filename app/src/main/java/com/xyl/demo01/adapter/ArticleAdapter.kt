package com.xyl.demo01.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xyl.demo01.R
import com.xyl.demo01.data.Article

class ArticleAdapter(private val articleList: List<Article>,
                     private val listener: OnItemClickListener)
    : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>(){
    interface OnItemClickListener {
        fun onItemClick(item: Article)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArticleAdapter.ArticleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_article_item, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleAdapter.ArticleViewHolder, position: Int) {
        val article = articleList[position]
        // 绑定数据
        holder.title.text = article.title
        holder.lexile.text = "难度: ${article.lexile}"
        holder.wordNum.text = "${article.wordNum}词"
        holder.type.text = article.type


        // 加载缩略图
        Glide.with(holder.cover.context)
            .load(article.cover)
            .into(holder.cover)


        // 动态修改类型背景颜色
        holder.type.setBackgroundColor(Color.parseColor(article.color?:"#ff33b5e5"))

        // 点击事件
        holder.itemView.setOnClickListener {
            listener.onItemClick(article)
        }
    }

    override fun getItemCount(): Int {
        return articleList.size
    }
    class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.titleTextView)
        val lexile: TextView = itemView.findViewById(R.id.lexileTextView)
        val wordNum: TextView = itemView.findViewById(R.id.wordNumTextView)
        val type: TextView = itemView.findViewById(R.id.typeTextView)
        val cover: ImageView = itemView.findViewById(R.id.coverImageView)
    }
}