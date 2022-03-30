package com.example.lorempicsum

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lorempicsum.network.GetDetailsByIdResponse
import com.example.lorempicsum.network.NetworkLayer
import com.example.lorempicsum.network.SharedRepository
import kotlinx.coroutines.*
import java.util.ArrayList

class SharedViewModel:ViewModel() {
    private val repository = SharedRepository()

    private val _detailsByIdLiveData = MutableLiveData<List<GetDetailsByIdResponse?>>()
    //Do not allow layer listening to live data to change it
    val detailsByLiveData: LiveData<List<GetDetailsByIdResponse?>> = _detailsByIdLiveData

    fun refreshDetails(id1: Int,id2: Int,id3: Int){
        val list = mutableListOf<GetDetailsByIdResponse?>()
        viewModelScope.launch {
            val task = listOf(
                async {
                    list.add(repository.getDetailsById(id1))
                },
                async {
                    list.add(repository.getDetailsById(id2))
                },
                async {
                    list.add(repository.getDetailsById(id3))
                },
            )
            task.awaitAll()

            _detailsByIdLiveData.postValue(list)
        }
    }

    fun getRandomIds(activity: MainActivity){
        val newIds = mutableListOf<Int>()
        val ioScope = CoroutineScope(Dispatchers.IO + Job())
        ioScope.launch {
            val job = ArrayList<Job>()

            for (i in 1..3){
                job.add(async {
                    val response = NetworkLayer.okHttpClient.newCall(NetworkLayer.randomRequest).execute()
                    newIds.add(response.header("picsum-id")?.toInt() ?: 0)
                })
            }

            job.joinAll()
            activity.runOnUiThread{activity.loadPictures(newIds)}
        }
    }

}