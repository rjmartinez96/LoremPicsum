package com.example.lorempicsum

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SharedViewModel:ViewModel() {
    private val repository = SharedRepository()

    private val _detailsByIdLiveData = MutableLiveData<GetDetailsByIdResponse?>()
    //Do not allow layer listening to live data to change it
    val detailsByLiveData: LiveData<GetDetailsByIdResponse?> = _detailsByIdLiveData

    fun refreshDetails(id: Int){
        viewModelScope.launch {
            val response = repository.getDetailsById(id)
            _detailsByIdLiveData.postValue(response)
        }
    }

}