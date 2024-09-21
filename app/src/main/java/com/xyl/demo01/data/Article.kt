package com.xyl.demo01.data

data class Article(
    val id: Int,
    val title: String,
    val wordNum: Int,
    val lexile: Int,
    val typeId: Int,
    val type: String,
    val cover: String,
    val clickRatio: Double?,
    val accuracy: Double?,
    val accuracyRatio: Double?,
    val color: String?,
    val isRead: Int,
    val readTime: String?


)
