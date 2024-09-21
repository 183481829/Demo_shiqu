package com.xyl.demo01.activity

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.viewpager2.widget.ViewPager2
import com.xyl.demo01.R
import com.xyl.demo01.adapter.ArticleContentAdapter
import com.xyl.demo01.data.ArticleContent
import com.xyl.demo01.data.SentenceByXF
import com.xyl.demo01.databinding.ActivityCommonKnowledgeBinding
import com.xyl.demo01.databinding.ActivityLibraryBinding
import com.xyl.demo01.viewModel.ArticleViewModel

class CommonKnowledgeActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null
    // ViewModel 实例
    private lateinit var articleViewModel: ArticleViewModel
    private lateinit var articleContentAdapter: ArticleContentAdapter
    private lateinit var binding: ActivityCommonKnowledgeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common_knowledge)
        binding= ActivityCommonKnowledgeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 初始化 MediaPlayer 实例
        mediaPlayer = MediaPlayer()
        // 初始化 ViewModel
        articleViewModel = ViewModelProvider(this).get(ArticleViewModel::class.java)
        articleViewModel.fetchArticleDetail(6)
        articleViewModel.articleContentList.observe(this){articleContentList->

            // 记录当前页面播放的音频位置
            var currentPlayingPosition = -1
            var isPlaying = false // 当前是否正在播放

            // 设置适配器
            articleContentAdapter = ArticleContentAdapter(
                articleContentList,
                mediaPlayer,
                isPlaying
            ) { newPlayingState ->
                // 更新播放状态
                isPlaying = newPlayingState
            }

            binding.viewPager.adapter = articleContentAdapter

            // 设置 ViewPager2 的页面滑动监听
            binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    // 如果页面切换了，释放之前的音频资源
                    if (position != currentPlayingPosition ) {

                        // 播放当前页面的音频
                        val articleContent = articleContentList[position]
                        mediaPlayer?.apply {
                            reset()
                            setDataSource(articleContent.audioUrl)
                            prepare()  // 准备音频
                            start()    // 开始播放
                        }

                        // 更新播放状态和当前播放的页面位置
                        isPlaying = true
                        currentPlayingPosition = position
                    }
                }
            })
        }



    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }

    // 播放当前页面的音频
    private fun playAudioForPage(articleContent: ArticleContent) {
        // 释放当前音频
        mediaPlayer?.release()

        // 播放新的音频
        mediaPlayer = MediaPlayer().apply {
            setDataSource(articleContent.audioUrl)
            prepare()
            start()
        }
    }

}