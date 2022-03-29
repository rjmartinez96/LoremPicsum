package com.example.lorempicsum.network

class SharedRepository {

    suspend fun getDetailsById(id:Int): GetDetailsByIdResponse?{
        val request = NetworkLayer.detailsClient.getDetailsById(id)

        if(request.failed || !request.isSuccessful){
            return null
        }

        return request.body
    }
}