// 검색 결과 리스트 item
package com.example.gaebal_saebal_aos_ver2

class SearchResultData (
    val contentId: Int,
    val contentCategory: String,
    val contentWriteDate: String,
    val contentTitle: String,
    val contentHashtag: MutableList<String>
)