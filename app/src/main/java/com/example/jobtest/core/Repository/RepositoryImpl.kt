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

class RepositoryImpl(private val remoteDatasource: RemoteDatasource) : Repository {

    // Fetch Vacancies
    override suspend fun getVacancies(): List<Vacancy> {
        try {
            val response = remoteDatasource.fetchVacancies()  // Fetch the response
            // If response is successful, parse the body into a list of Vacancies
            return parseVacancyResponse(response)
        } catch (e: Exception) {
            Log.e("Repository", "Error fetching vacancies: ${e.message}")
            return emptyList() // Return an empty list in case of error
        }
    }

    // Fetch Offers
    override suspend fun getOffer(): List<Offer> {
        try {
            val response = remoteDatasource.fetchOffers()  // Fetch the response
            // If response is successful, parse the body into a list of Offers
            return parseOfferResponse(response)
        } catch (e: Exception) {
            Log.e("Repository", "Error fetching offers: ${e.message}")
            return emptyList() // Return an empty list in case of error
        }
    }

    // Parse the response for Vacancies
    private fun parseVacancyResponse(response: Response<ResponseBody>): List<Vacancy> {
        return if (response.isSuccessful) {
            try {
                val responseBody = response.body()?.string()
                // Assuming the response is an object with a "vacancies" field
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

    // Parse the response for Offers (similar to Vacancies)
    private fun parseOfferResponse(response: Response<ResponseBody>): List<Offer> {
        return if (response.isSuccessful) {
            try {
                val responseBody = response.body()?.string()
                // Assuming the response is an object with an "offers" field
                val jsonObject = JSONObject(responseBody)
                val offersJsonArray = jsonObject.optJSONArray("offers")

                // Convert the JSON array into a list of Offer objects
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
