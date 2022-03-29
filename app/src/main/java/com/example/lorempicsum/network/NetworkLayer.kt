package com.example.lorempicsum.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkLayer {
    val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://picsum.photos/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val detailsService: LoremPictureDetailsService by lazy {
        retrofit.create(LoremPictureDetailsService::class.java)
    }

    val detailsClient = ApiClient(detailsService)

    val okHttpClient = OkHttpClient()
    val randomRequest = Request.Builder()
        .url("https://picsum.photos/400/400")
        .build()
}