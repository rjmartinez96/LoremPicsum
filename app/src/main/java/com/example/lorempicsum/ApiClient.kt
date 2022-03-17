package com.example.lorempicsum

import retrofit2.Response

class ApiClient(
    private val detailsService: LoremPictureDetailsService
) {
    suspend fun getDetailsById(id: Int): Response<GetDetailsByIdResponse> {
        return detailsService.getPictureDetailsById(id)
    }
}