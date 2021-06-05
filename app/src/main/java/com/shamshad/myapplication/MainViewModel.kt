package com.shamshad.myapplication

import android.app.Application
import android.content.Context
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

    private val _posterror = MutableLiveData<String>()

    val posterror:LiveData<String> =_posterror

    private val _ispostAdd = MutableLiveData<Boolean>(false)
    val ispostAdd: LiveData<Boolean> = _ispostAdd

init {

}

    fun getList(context: Context) {
        viewModelScope.launch(Dispatchers.Default) {
            listRepo.getList(context).collect{ result ->
                when (result) {
                    is ResultOf.Success -> {
                        _dataLoading.postValue(false)
                        _listdata.postValue(result.value!!)
                    }
                    is ResultOf.Loading -> _dataLoading.postValue(true)
                    is ResultOf.Error -> {
                        _dataLoading.postValue(true)

                    }
                }
            }
        }
    }


    fun savePost(title:String,desc:String) {

        viewModelScope.launch(Dispatchers.IO) {
            listRepo.setPost(title,desc).collect{ result ->
                when (result) {
                    is ResultOf.Success -> {
                        _dataLoading.postValue(false)
                        _ispostAdd.postValue(true)
                        getList(getApplication())
                        //discardChanges();
                    }
                    is ResultOf.Loading -> _dataLoading.postValue(true)
                    is ResultOf.Error -> {
                        _posterror.postValue(result.exception.toString())
                        _dataLoading.postValue(true)
                    }

                }
            }
        }

    }
}




