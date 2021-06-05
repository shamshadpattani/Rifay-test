package com.shamshad.myapplication.data.remote

import com.shamshad.myapplication.data.model.ListData
import com.shamshad.myapplication.data.model.PostItem
import retrofit2.Response
import retrofit2.http.*

interface APIInterface {

    @GET("/kotlintest")
    suspend fun getList(): Response<List<ListData>>

    @POST("/kotlintest")
    suspend fun setPost(@Body post: PostItem): Response<String>

    companion object {
        const val BASE_API_URL = "http://fastingconsole.us-east-1.elasticbeanstalk.com"
    }
}
