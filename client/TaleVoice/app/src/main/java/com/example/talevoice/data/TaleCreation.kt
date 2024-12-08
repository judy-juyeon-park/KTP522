package com.example.talevoice.data

data class TaleCreation(
    val taleId: String,
    val title: String,
    val contents: List<String> // 5페이지 분량의 동화 내용
)
