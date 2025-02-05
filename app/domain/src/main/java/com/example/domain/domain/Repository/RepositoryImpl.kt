package com.example.domain.domain.Repository

import android.util.Log
import com.example.core.core.data.Offer
import com.example.core.core.data.Vacancy
import com.example.core.core.datasource.RemoteDatasource
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val remoteDatasource: RemoteDatasource
) : Repository {


    override suspend fun getVacancies(): List<Vacancy> {
        return withContext(Dispatchers.IO) {
            runCatching {
                val response = remoteDatasource.fetchData()
                parseVacancyResponse(response)
            }.getOrElse {
                Log.e("Repository", "Error fetching vacancies: ${it.message}")
                emptyList<Vacancy>() // Return an empty list in case of error
            }
        }
    }


    override suspend fun getOffer(): List<Offer> {
        return withContext(Dispatchers.IO) {
            runCatching {
                val response = remoteDatasource.fetchData()
                parseOfferResponse(response)
            }.getOrElse {
                Log.e("Repository", "Error fetching vacancies: ${it.message}")
                emptyList<Offer>() // Return an empty list in case of error
            }
        }

    }


    private suspend fun parseVacancyResponse(response: Response<ResponseBody>): List<Vacancy> {
        return withContext(Dispatchers.IO) {
            runCatching {
                if (response.isSuccessful) {
                    val responseBody = response.body()?.string()
                    val jsonObject = JSONObject(responseBody)
                    val vacanciesJsonArray = jsonObject.optJSONArray("vacancies")

                    val listType = object : TypeToken<List<Vacancy>>() {}.type
                    Gson().fromJson(vacanciesJsonArray?.toString(), listType) ?: emptyList()
                } else {
                    Log.e("Repository", "Response not successful: ${response.code()}")
                    emptyList<Vacancy>()
                }
            }.getOrElse {
                Log.e("Repository", "Error parsing vacancies: ${it.message}")
                emptyList<Vacancy>()
            }
        }
    }


    private suspend fun parseOfferResponse(response: Response<ResponseBody>): List<Offer> {
        return withContext(Dispatchers.IO) {
            runCatching {
                if (response.isSuccessful) {
                    val responseBody = response.body()?.string()
                    val jsonObject = JSONObject(responseBody)
                    val offersJsonArray = jsonObject.optJSONArray("offers")


                    val listType = object : TypeToken<List<Offer>>() {}.type
                    Gson().fromJson(offersJsonArray?.toString(), listType) ?: emptyList()
                } else {
                    Log.e("Repository", "Response not successful: ${response.code()}")
                    emptyList<Offer>()
                }
            }.getOrElse {
                Log.e("Repository", "Error parsing offers: ${it.message}")
                emptyList()
            }


        }
    }
}
