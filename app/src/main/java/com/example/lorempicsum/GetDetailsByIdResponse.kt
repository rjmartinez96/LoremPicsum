package com.example.lorempicsum

import com.squareup.moshi.Json

data class GetDetailsByIdResponse(
    val author: String,
    @Json(name = "download_url")
    val downloadUrl: String,
    val height: Int,
    val id: String,
    val url: String,
    val width: Int
)