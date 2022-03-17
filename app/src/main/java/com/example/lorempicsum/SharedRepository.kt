package com.example.lorempicsum

class SharedRepository {

    suspend fun getDetailsById(id:Int):GetDetailsByIdResponse?{
        val request = NetworkLayer.client.getDetailsById(id)

        if(request.failed || !request.isSuccessful){
            return null
        }

        return request.body
    }
}