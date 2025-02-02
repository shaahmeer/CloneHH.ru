package com.example.jobtest.core.datasource

import com.example.jobtest.core.data.VacancyData
import okhttp3.ResponseBody
import retrofit2.Response

class RemoteDatasource(private val fileDownloadService: FileDownloadService) {

    suspend fun fetchOffers(): Response<ResponseBody> =
        fileDownloadService.downloadFile("1z4TbeDkbfXkvgpoJprXbN85uCcD7f00r", "download")

    suspend fun fetchVacancies(): Response<ResponseBody> =
        fileDownloadService.downloadFile("1z4TbeDkbfXkvgpoJprXbN85uCcD7f00r", "download")


}
