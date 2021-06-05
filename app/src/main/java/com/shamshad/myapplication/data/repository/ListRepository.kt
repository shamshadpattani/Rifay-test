package com.shamshad.myapplication.data.repository

import android.app.Application
import android.content.Context
import android.os.Handler
import android.util.Log
import com.shamshad.myapplication.data.remote.APIClient
import com.autosmarts.gondor.data.repository.utils.ResultOf
import com.shamshad.myapplication.data.model.ListData
import com.shamshad.myapplication.data.model.PostItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ListRepository (c: Application) {
    private var mContext: Application = c

    suspend fun getList(c:Context): Flow<ResultOf<List<ListData>>> {
        return flow {
            try {
                var resp = APIClient.getApiService(mContext).getList()

                if (resp.isSuccessful && resp.code() == 200) {
                    val savedData = resp.body() as List<ListData>
                    emit(ResultOf.Success(savedData ?: listOf()))
                } else {
                    emit(ResultOf.Error(Exception(resp.message())))
                }
            }catch (e: Exception) {
                emit(ResultOf.Error(e))
            }
        }.flowOn(Dispatchers.IO)
            }

    suspend fun setPost(title:String,descrp:String): Flow<ResultOf<String>> {
        return flow {
        try {
            val post: PostItem? =
                PostItem(title, descrp)
            var resp = APIClient.getApiService(mContext).setPost(post!!)

            if (resp.isSuccessful && resp.code() == 200) {
                val savedData = resp.body()
                emit(ResultOf.Success(savedData ?: ""))
            } else {
                emit(ResultOf.Error(Exception(resp.message())))
            }
        }catch (e: Exception) {
            emit(ResultOf.Error(e))
        }
        }.flowOn(Dispatchers.IO)
    }

}