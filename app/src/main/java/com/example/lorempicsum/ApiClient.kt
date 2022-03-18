package com.example.lorempicsum

import okhttp3.Request
import retrofit2.Response
import java.lang.Exception

class ApiClient(
    private val detailsService: LoremPictureDetailsService
) {
    suspend fun getDetailsById(id: Int): SimpleResponse<GetDetailsByIdResponse> {
        return safeApiCall { detailsService.getPictureDetailsById(id) }
    }

    //Prevent app from crashing if the request fails or there is a network issue
    private inline fun <T> safeApiCall(apiCall: ()->Response<T>):SimpleResponse<T> {
        return try {
            SimpleResponse.success(apiCall.invoke())
        } catch (e: Exception){
            SimpleResponse.failure(e)
        }
    }
}