package com.xyl.demo01.viewModle

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xyl.demo01.data.ArticleType
import com.xyl.demo01.utils.ApiClient
import com.xyl.demo01.utils.ApiService
import kotlinx.coroutines.launch

class ArticleViewModel : ViewModel() {

    // LiveData 用来存储文章分类
    private val _articleTypes = MutableLiveData<List<ArticleType>>()
    val articleTypes: LiveData<List<ArticleType>> get() = _articleTypes

    // 使用 Retrofit 的接口
    private val apiService = ApiClient.createService(ApiService::class.java)

    // 发起网络请求的函数
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
}