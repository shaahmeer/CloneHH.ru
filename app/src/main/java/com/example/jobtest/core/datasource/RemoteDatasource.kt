package com.example.jobtest.core.datasource

import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class RemoteDatasource @Inject constructor(
    private val fileDownloadService: FileDownloadService
) {

    suspend fun fetchOffers(): Response<ResponseBody> =
        fileDownloadService.downloadFile("1z4TbeDkbfXkvgpoJprXbN85uCcD7f00r", "download")

    suspend fun fetchVacancies(): Response<ResponseBody> =
        fileDownloadService.downloadFile("1z4TbeDkbfXkvgpoJprXbN85uCcD7f00r", "download")
}
