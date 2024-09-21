package com.xyl.demo01.utils

import com.xyl.demo01.data.ApiResponse
import com.xyl.demo01.data.ArticleContent
import com.xyl.demo01.data.ArticleCotentResponse
import com.xyl.demo01.data.ArticleListResponse
import com.xyl.demo01.data.ArticleType
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {
    @GET("http://test.shiqu.zhilehuo.com/englishgpt/library/articleTypeList")
    suspend fun getArticleTypeList(): Response<ApiResponse<List<ArticleType>>>
    @GET("http://test.shiqu.zhilehuo.com/englishgpt/appArticle/selectList")
    suspend fun getSelectList(): Response<ApiResponse<List<Int>>>
    @GET("http://test.shiqu.zhilehuo.com/englishgpt/library/articleList")
    suspend fun getArticleListResponse(
        @Query("lexile") lexile: Int,   //难度值（从右侧顶部难度值接口选择的值）
        @Query("typeId") typeId: Int,   // 推荐阅读时传0或null，其余情况传左边列表接口的id
        @Query("page") page: Int        //当前请求的页数
    ): Response<ApiResponse<ArticleListResponse>>
    @GET("http://shiqu.zhilehuo.com/knowledge/article/getArticleDetail")
    suspend fun getArticleDetail(@Query("aid")aid: Int,
                                 @Header("Cookie") cookie: String,):Response<ApiResponse<ArticleCotentResponse>>

}