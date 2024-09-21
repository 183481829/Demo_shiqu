package com.xyl.demo01.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xyl.demo01.R
import com.xyl.demo01.adapter.ArticleAdapter
import com.xyl.demo01.adapter.CategoryAdapter
import com.xyl.demo01.adapter.DifficultyAdapter
import com.xyl.demo01.data.Article
import com.xyl.demo01.data.ArticleType
import com.xyl.demo01.databinding.ActivityEditIinformationBinding
import com.xyl.demo01.databinding.ActivityLibraryBinding
import com.xyl.demo01.viewModel.ArticleViewModel

class LibraryActivity : AppCompatActivity() ,CategoryAdapter.OnItemClickListener,DifficultyAdapter.OnDifficultyClickListener,ArticleAdapter.OnItemClickListener{
    // ViewModel 实例
    private lateinit var articleViewModel: ArticleViewModel
    private lateinit var binding: ActivityLibraryBinding
    // 用于记录当前选择的分类 ID 和难度值
    private var selectedCategoryId: Int = 1
    private var selectedLexlie: Int = 600
    private var currentPage: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)
        binding= ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.categoryRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        binding.difficultyRecyclerView.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.articleRecyclerView.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        // 初始化 ViewModel
        articleViewModel = ViewModelProvider(this).get(ArticleViewModel::class.java)

        // 调用 ViewModel 中的函数，发起网络请求
        articleViewModel.fetchArticleTypeList()
        articleViewModel.fetchSelectList()
//        articleViewModel.fetchArticleList(selectedLexlie,selectedCategoryId,1)
        fetchArticles()
        // 观察 LiveData，当数据变化时执行相应操作
        articleViewModel.articleTypes.observe(this) { articleTypes ->
            // 绑定获取到的数据
            val adapter = CategoryAdapter(articleTypes,this)
            binding.categoryRecyclerView.adapter = adapter
            currentPage=1

        }
        articleViewModel.selectList.observe(this){selectList->

            var adapter = DifficultyAdapter(selectList, this)
            binding.difficultyRecyclerView.adapter=adapter
            currentPage=1

        }
        articleViewModel.articleList.observe(this){articleList->

            val adapter = ArticleAdapter(articleList, this)
            binding.articleRecyclerView.adapter = adapter

        }
        // 设置下拉刷新功能
        binding.swipeRefreshLayout.setOnRefreshListener {
            currentPage = 1 // 刷新时重置为第一页
            fetchArticles(refresh = true) // 调用获取文章的函数
        }

        // 设置上拉加载更多功能
        binding.articleRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
//                val visibleItemCount = layoutManager.childCount
//                val totalItemCount = layoutManager.itemCount
//                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.itemCount - 1) {
                    // 到达底部，加载更多
                    currentPage++
                    fetchArticles()
                }
            }
        })


    }

    override fun onItemClick(articleType: ArticleType) {
        Log.d("LibraryActivity", "Clicked Article Type ID: ${articleType.id}")
        // 更新选中的分类 ID
        selectedCategoryId = articleType.id
        articleViewModel.fetchArticleList(selectedLexlie,selectedCategoryId,1)
        currentPage=1


    }

    override fun onDifficultyClick(difficultyValue: Int) {
        selectedLexlie = difficultyValue
        Log.d("LibraryActivity", "Clicked difficultyValue: ${difficultyValue}")
        articleViewModel.fetchArticleList(selectedLexlie,selectedCategoryId,1)
        currentPage=1

    }

    override fun onItemClick(item: Article) {
        Log.d("LibraryActivity", "Clicked Article ID: ${item.id}")
    }
    // 根据选中的分类 ID 和难度值发起网络请求，支持下拉刷新和上拉加载更多
    private fun fetchArticles(refresh: Boolean = false) {
        if (refresh) {
            // 如果是下拉刷新，则清空之前的数据
            articleViewModel.fetchArticleList(selectedLexlie, selectedCategoryId, 1)
            binding.swipeRefreshLayout.isRefreshing = false // 刷新完毕后关闭加载动画
        } else {
            // 加载更多时请求下一页的数据
            println("Activity"+currentPage)
            articleViewModel.fetchArticleList(selectedLexlie, selectedCategoryId, currentPage)

        }
    }
}