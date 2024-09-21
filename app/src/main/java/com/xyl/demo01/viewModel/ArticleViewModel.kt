package com.xyl.demo01.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xyl.demo01.data.Article
import com.xyl.demo01.data.ArticleContent
import com.xyl.demo01.data.ArticleListResponse
import com.xyl.demo01.data.ArticleType
import com.xyl.demo01.utils.ApiClient
import com.xyl.demo01.utils.ApiService
import kotlinx.coroutines.launch

class ArticleViewModel : ViewModel() {

    // LiveData 用来存储文章分类
    private val _articleTypes = MutableLiveData<List<ArticleType>>()
    val articleTypes: LiveData<List<ArticleType>> get() = _articleTypes

    private val _selectList = MutableLiveData<List<Int>>()
    val selectList: LiveData<List<Int>> get() = _selectList

    private val _articleList = MutableLiveData<List<Article>>()
    val articleList: LiveData<List<Article>> get() = _articleList
    // 添加变量用于存储总页数
    private var totalPages: Int = 1

    private val _articleContentList = MutableLiveData<List<ArticleContent>>()
    val articleContentList: LiveData<List<ArticleContent>> get() = _articleContentList


    // 使用 Retrofit 的接口
    private val apiService = ApiClient.createService(ApiService::class.java)

    // 请求文章类型
    fun fetchArticleTypeList() {
        viewModelScope.launch {
            try {
                val response = apiService.getArticleTypeList()
                if (response.isSuccessful) {
                    val articleTypes = response.body()?.data
                    articleTypes?.let {
                        // 更新 LiveData
                        _articleTypes.postValue(it)
                    }
                } else {
                    // 错误处理逻辑
                    Log.e("ArticleViewModel", "Error: ${response.code()}")
                }
            } catch (e: Exception) {
                // 异常处理
                Log.e("ArticleViewModel", "Exception: ${e.message}")
            }
        }
    }
    // 请求难度值
    fun fetchSelectList() {
        viewModelScope.launch {
            try {
                val response = apiService.getSelectList()
                if (response.isSuccessful) {
                    val selectList = response.body()?.data
                    selectList?.let {
                        // 更新 LiveData
                        _selectList.postValue(it)
                    }
                } else {
                    // 错误处理逻辑
                    Log.e("ArticleViewModel", "Error: ${response.code()}")
                }
            } catch (e: Exception) {
                // 异常处理
                Log.e("ArticleViewModel", "Exception: ${e.message}")
            }
        }
    }
    // 请求文章列表
    fun fetchArticleList(lexlie:Int,typeId:Int,page:Int) {
        viewModelScope.launch {
            try {
                val response = apiService.getArticleListResponse(lexlie,typeId,page)
                if (response.isSuccessful) {
                    var articleListResponse=response.body()?.data
                    // 获取总页数
                    totalPages = articleListResponse?.lastPage ?: 1
                    val newArticles = articleListResponse?.list ?: emptyList()

                    // 如果是第一页，替换文章列表，否则追加到现有文章列表中
                    if (page == 1) {
                        _articleList.postValue(newArticles)
                    } else if (articleListResponse?.hasNextPage == true||articleListResponse?.isLastPage==true){
                        val updatedArticles = _articleList.value.orEmpty() + newArticles
                        _articleList.postValue(updatedArticles)

                    }
                } else {
                    // 错误处理逻辑
                    Log.e("ArticleViewModel", "Error: ${response.code()}")
                }
            } catch (e: Exception) {
                // 异常处理
                Log.e("ArticleViewModel", "Exception: ${e.message}")
            }
        }
    }

    //请求文章内容
    fun fetchArticleDetail(aid: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.getArticleDetail(aid,"sid=i5VMMK2c7EEm5qK597kJeDqrel7NKCRqSQRGQn8mJBs=")
                if (response.isSuccessful) {
                    val articleContentList = response.body()?.data?.contentList
                    articleContentList?.let {
                        // Process the article content or update LiveData
                        _articleContentList.postValue(it)
                    }
                } else {
                    // Handle error response
                    Log.e("ArticleViewModel", "Error: ${response.code()}")
                }
            } catch (e: Exception) {
                // Handle exceptions
                Log.e("ArticleViewModel", "Exception: ${e.message}")
            }
        }
    }

}