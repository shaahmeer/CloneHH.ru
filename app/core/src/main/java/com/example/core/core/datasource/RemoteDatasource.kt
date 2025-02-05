package com.example.core.core.datasource

import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class RemoteDatasource @Inject constructor(
    private val fileDownloadService: FileDownloadService
) {

//    suspend fun fetchOffers(): Response<ResponseBody> =
//        fileDownloadService.downloadFile()

    suspend fun fetchData(): Response<ResponseBody> =
        fileDownloadService.downloadFile()
}
