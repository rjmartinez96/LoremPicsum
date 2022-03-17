package com.example.lorempicsum

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
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

    val client = ApiClient(detailsService)
}