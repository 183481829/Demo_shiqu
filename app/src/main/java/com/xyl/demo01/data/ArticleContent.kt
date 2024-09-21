package com.xyl.demo01.data

data class ArticleContent(
    val pageNum: Int,
    val imgUrl : String?,
    val audioUrl: String?,
    val audioDuration: Int,
    val sentence: String,
    val frameType: Int,
    val sentenceByXFList: List<SentenceByXF>
)

