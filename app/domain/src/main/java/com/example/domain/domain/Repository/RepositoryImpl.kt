package com.example.domain.domain.Repository

import android.util.Log
import com.example.core.core.data.Offer
import com.example.core.core.data.Vacancy
import com.example.core.core.datasource.RemoteDatasource
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val remoteDatasource: RemoteDatasource
) : Repository {

    override suspend fun getVacancies(): List<Vacancy> {
        return try {
            val response = remoteDatasource.fetchData()
            parseVacancyResponse(response)
        } catch (e: java.net.UnknownHostException) {
            Log.e("Repository", "No internet connection: ${e.message}")
            emptyList()
        } catch (e: Exception) {
            Log.e("Repository", "Error fetching vacancies: ${e.message}")
            emptyList()
        }
    }

    override suspend fun getOffer(): List<Offer> {
        return try {
            val response = remoteDatasource.fetchData()
            parseOfferResponse(response)
        } catch (e: java.net.UnknownHostException) {
            Log.e("Repository", "No internet connection: ${e.message}")
            emptyList()
        } catch (e: Exception) {
            Log.e("Repository", "Error fetching offers: ${e.message}")
            emptyList()
        }
    }


    private fun parseVacancyResponse(response: Response<ResponseBody>): List<Vacancy> {
        return try {
            if (response.isSuccessful) {
                response.body()?.string()?.let { responseBody ->
                    val jsonObject = JSONObject(responseBody)
                    val vacanciesJsonArray = jsonObject.optJSONArray("vacancies")

                    val listType = object : TypeToken<List<Vacancy>>() {}.type
                    Gson().fromJson(vacanciesJsonArray?.toString(), listType) ?: emptyList()
                } ?: emptyList()
            } else {
                Log.e("Repository", "Response not successful: ${response.code()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("Repository", "Error parsing vacancies: ${e.message}")
            emptyList()
        }
    }

    private fun parseOfferResponse(response: Response<ResponseBody>): List<Offer> {
        return try {
            if (response.isSuccessful) {
                response.body()?.string()?.let { responseBody ->
                    val jsonObject = JSONObject(responseBody)
                    val offersJsonArray = jsonObject.optJSONArray("offers")

                    val listType = object : TypeToken<List<Offer>>() {}.type
                    Gson().fromJson(offersJsonArray?.toString(), listType) ?: emptyList()
                } ?: emptyList()
            } else {
                Log.e("Repository", "Response not successful: ${response.code()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("Repository", "Error parsing offers: ${e.message}")
            emptyList()
        }
    }
}
