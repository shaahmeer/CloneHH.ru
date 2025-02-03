package com.example.jobtest.core.Repository

import android.util.Log
import com.example.jobtest.core.data.Offer
import com.example.jobtest.core.data.Vacancy
import com.example.jobtest.core.datasource.RemoteDatasource
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
        try {
            val response = remoteDatasource.fetchVacancies()  // Fetch the response
            return parseVacancyResponse(response)
        } catch (e: Exception) {
            Log.e("Repository", "Error fetching vacancies: ${e.message}")
            return emptyList() // Return an empty list in case of error
        }
    }


    override suspend fun getOffer(): List<Offer> {
        try {
            val response = remoteDatasource.fetchOffers()  // Fetch the response
            return parseOfferResponse(response)
        } catch (e: Exception) {
            Log.e("Repository", "Error fetching offers: ${e.message}")
            return emptyList() // Return an empty list in case of error
        }
    }


    private fun parseVacancyResponse(response: Response<ResponseBody>): List<Vacancy> {
        return if (response.isSuccessful) {
            try {
                val responseBody = response.body()?.string()
                val jsonObject = JSONObject(responseBody)
                val vacanciesJsonArray = jsonObject.optJSONArray("vacancies")

                // Convert the JSON array into a list of Vacancy objects
                val listType = object : TypeToken<List<Vacancy>>() {}.type
                return Gson().fromJson(vacanciesJsonArray?.toString(), listType) ?: emptyList()

            } catch (e: Exception) {
                Log.e("Repository", "Error parsing vacancies: ${e.message}")
                emptyList()
            }
        } else {
            Log.e("Repository", "Response not successful: ${response.code()}")
            emptyList()
        }
    }


    private fun parseOfferResponse(response: Response<ResponseBody>): List<Offer> {
        return if (response.isSuccessful) {
            try {
                val responseBody = response.body()?.string()
                val jsonObject = JSONObject(responseBody)
                val offersJsonArray = jsonObject.optJSONArray("offers")


                val listType = object : TypeToken<List<Offer>>() {}.type
                return Gson().fromJson(offersJsonArray?.toString(), listType) ?: emptyList()

            } catch (e: Exception) {
                Log.e("Repository", "Error parsing offers: ${e.message}")
                emptyList()
            }
        } else {
            Log.e("Repository", "Response not successful: ${response.code()}")
            emptyList()
        }
    }
}
