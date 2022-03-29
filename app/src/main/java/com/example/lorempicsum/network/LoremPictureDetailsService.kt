package com.example.lorempicsum.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface LoremPictureDetailsService {

    @GET("id/{id}/info")
    suspend fun getPictureDetailsById(
        @Path("id") id: Int
    ): Response<GetDetailsByIdResponse>
}