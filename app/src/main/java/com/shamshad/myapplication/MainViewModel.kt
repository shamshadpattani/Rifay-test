package com.shamshad.myapplication

import android.app.Application
import androidx.lifecycle.*
import com.autosmarts.gondor.data.repository.utils.ResultOf
import com.shamshad.myapplication.data.model.ListData
import com.shamshad.myapplication.data.repository.ListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect



class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val listRepo: ListRepository = ListRepository(getApplication())

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _listdata = MutableLiveData<List<ListData>>()

     val listData:LiveData<List<ListData>> =_listdata

    val title: MutableLiveData<String> = MutableLiveData()
    val descrp: MutableLiveData<String> = MutableLiveData()
    private val _dismissDialog = MutableLiveData<Boolean>(false)
    val dismissDialog: LiveData<Boolean> = _dismissDialog
    private val _ispostAdd = MutableLiveData<Boolean>(false)
    val ispostAdd: LiveData<Boolean> = _ispostAdd

init {
    getList()
}


    private fun getList() {
        viewModelScope.launch(Dispatchers.Default) {
            listRepo.getList().collect{ result ->
                when (result) {
                    is ResultOf.Success -> {
                        _dataLoading.postValue(false)
                        _listdata.postValue(result.value!!)
                    }
                    is ResultOf.Loading -> _dataLoading.postValue(true)
                    is ResultOf.Error -> _dataLoading.postValue(false)
                }
            }
        }
    }



    fun discardChanges() {
        _dismissDialog.postValue(true)
    }
    fun activeDialog(){
        _dismissDialog.postValue(false)
        title.postValue("")
        descrp.postValue("")
    }

    fun savePost() {

        viewModelScope.launch(Dispatchers.IO) {
            listRepo.setPost(title.value.toString(),descrp.value.toString()).collect{ result ->
                when (result) {
                    is ResultOf.Success -> {
                        _dataLoading.postValue(false)
                        _ispostAdd.postValue(true)
                         getList()
                        discardChanges();
                    }
                    is ResultOf.Loading -> _dataLoading.postValue(true)
                    is ResultOf.Error -> _dataLoading.postValue(false)
                }
            }
        }

    }
}




