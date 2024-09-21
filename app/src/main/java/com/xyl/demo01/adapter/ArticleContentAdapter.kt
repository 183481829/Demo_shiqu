package com.xyl.demo01.adapter

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.xyl.demo01.R
import com.xyl.demo01.data.ArticleContent
import com.xyl.demo01.data.SentenceByXF
import kotlin.math.log

class ArticleContentAdapter( private val articleContentList: List<ArticleContent>,
                             private val mediaPlayer: MediaPlayer?, // 传递 MediaPlayer
                             private var isPlaying: Boolean,        // 是否正在播放
                             private val onPlayPauseClicked: (Boolean) -> Unit // 点击按钮后的回调
                             )
    : RecyclerView.Adapter<ArticleContentAdapter.ArticleContentViewHolder>(){
//    private var mediaPlayer: MediaPlayer? = null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArticleContentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_article_content_page, parent, false)
        return ArticleContentViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ArticleContentViewHolder,
        position: Int
    ) {
        val articleContent = articleContentList[position]

        // 动态设置标题、页码、句子内容

        holder.pageNumberTextView.text = "${position + 1}/${articleContentList.size}"
        holder.sentenceTextView.text = articleContent.sentence



        // 加载图片
        Glide.with(holder.itemView.context)
            .load(articleContent.imgUrl)
            .into(holder.contentImageView)
        // 更新进度条的进度，基于当前页和总页数
        val progress = ((position + 1) * 100) / articleContentList.size
        holder.progressBar.progress = progress

//        // 释放当前播放的音频（如果有）
//        mediaPlayer?.release()
//
//        // 准备新的音频
//        mediaPlayer = MediaPlayer().apply {
//            setDataSource(articleContent.audioUrl)
//            Log.d("TAG", "当前pos"+position+"onBindViewHolder: "+articleContent.audioUrl)
//            prepare()
//            start()
//        }

        // 设置播放/暂停按钮
         isPlaying = true
        holder.audioControlButton.setImageResource(R.drawable.ic_pause) // 默认是播放中
        holder.audioControlButton.setOnClickListener {
            if (isPlaying) {
                mediaPlayer?.pause()
                holder.audioControlButton.setImageResource(R.drawable.ic_play)
            } else {
                mediaPlayer?.start()
                holder.audioControlButton.setImageResource(R.drawable.ic_pause)
            }
            isPlaying = !isPlaying
            // 通知外部控制播放状态
            onPlayPauseClicked(isPlaying)
        }

        // 设置音频播放完成时回调
        mediaPlayer?.setOnCompletionListener {
            holder.audioControlButton.setImageResource(R.drawable.ic_play)
            isPlaying = false
        }

        // 同步音频播放时间和句子高亮
        syncHighlight(holder.sentenceTextView, articleContent.sentenceByXFList)
    }

    override fun getItemCount(): Int {
        return articleContentList.size
    }

    class ArticleContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pageNumberTextView: TextView = itemView.findViewById(R.id.pageNumberTextView)
        val contentImageView: ImageView = itemView.findViewById(R.id.contentImageView)
        val sentenceTextView: TextView = itemView.findViewById(R.id.sentenceTextView)
        val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
        var audioControlButton:ImageButton=itemView.findViewById(R.id.audioControlButton)
    }
    private fun syncHighlight(sentenceTextView: TextView, sentenceByXFList: List<SentenceByXF>) {
        val sentence = sentenceTextView.text.toString()
        val handler = Handler(Looper.getMainLooper())

        handler.post(object : Runnable {
            override fun run() {
                mediaPlayer?.let {
                    val currentPosition = it.currentPosition // 获取当前播放的毫秒数

                    // 创建一个新的 SpannableString，每次重置高亮状态
                    val spannableString = SpannableString(sentence)
                    var currentIndex =0
                    // 遍历 SentenceByXFList，找到当前时间对应的单词
                    sentenceByXFList.forEach { xf ->
                        val startTime = xf.wb // 开始时间，单位是毫秒
                        val endTime = xf.we   // 结束时间，单位是毫秒

                        // 如果当前播放时间在单词的时间范围内
                        if (currentPosition in startTime..endTime) {
                            // 在 sentence 中查找单词的位置
                             currentIndex+=xf.word.length-1
                            var wordStartIndex = sentence.indexOf(xf.word,currentIndex)

                            // 找到单词并且确保逐个匹配，不高亮重复位置
                            while (wordStartIndex != -1) {
                                val wordEndIndex = wordStartIndex + xf.word.length

                                    // 高亮当前时间范围内的匹配单词
                                spannableString.setSpan(
                                        ForegroundColorSpan(Color.RED), // 高亮颜色
                                        wordStartIndex,
                                        wordEndIndex,
                                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                                    )
                                    break

                            }
                        }
                    }

                    // 设置高亮后的文本
                    sentenceTextView.text = spannableString

                }

                // 每隔100毫秒检查一次
                handler.postDelayed(this, 100)
            }
        })
    }
}