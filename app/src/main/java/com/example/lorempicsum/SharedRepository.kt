package com.example.lorempicsum

import android.util.Log
import okhttp3.*

class SharedRepository {

    suspend fun getDetailsById(id:Int):GetDetailsByIdResponse?{
        val request = NetworkLayer.detailsClient.getDetailsById(id)

        if(request.failed || !request.isSuccessful){
            return null
        }

        return request.body
    }
}