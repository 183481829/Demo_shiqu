package com.xyl.demo01.utils

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
//    private const val BASE_URL = "http://test.shiqu.zhilehuo.com/"
    private const val BASE_URL = "http://shiqu.zhilehuo.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request()
                .newBuilder()
                .addHeader("Cookie", "sid=OFvEbpyl4PyKkc/cSjl2tW3g5Ga/z5DPSQRGQn8mJBs=")
                .build()
            chain.proceed(request)
        }
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://placeholder.com/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())  // 处理JSON解析
        .build()

    fun <T> createService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }
}