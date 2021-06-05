package com.shamshad.myapplication.data.remote

import android.app.Application
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIClient {

 /*   fun getApiService(): APIInterface {
            return getApiService(ListRepository(context))
        }*/

    fun getApiService(context: Application): APIInterface {
       var apiService: APIInterface? = null
        val httpClient = OkHttpClient.Builder()
            val retrofit = Retrofit.Builder()
                    .baseUrl(APIInterface.BASE_API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build()
            apiService = retrofit.create(APIInterface::class.java)
        return apiService!!
    }
}