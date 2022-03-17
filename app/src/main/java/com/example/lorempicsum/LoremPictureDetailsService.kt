package com.example.lorempicsum

import retrofit2.Call
import retrofit2.http.GET

interface LoremPictureDetailsService {

    @GET("id/2/info")
    fun getPictureDetailsById(): Call<Any>
}