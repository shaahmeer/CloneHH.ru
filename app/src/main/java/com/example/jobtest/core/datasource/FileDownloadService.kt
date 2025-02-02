package com.example.jobtest.core.datasource

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FileDownloadService {
    @GET("uc")
    suspend fun downloadFile(
        @Query("id") fileId: String,
        @Query("export") export: String
    ): Response<ResponseBody>
}
