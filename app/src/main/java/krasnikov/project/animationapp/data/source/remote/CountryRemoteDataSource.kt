package krasnikov.project.animationapp.data.source.remote

import krasnikov.project.animationapp.data.source.ICountryDataSource
import krasnikov.project.animationapp.data.model.Country
import krasnikov.project.animationapp.data.source.remote.api.CountryService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CountryRemoteDataSource(private val countryService: CountryService) : ICountryDataSource {

    override fun getCountries(callback: (result: Result<List<Country>>) -> Unit) {
        countryService.getCountries().enqueue(object : Callback<List<Country>> {
            override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                if (response.isSuccessful) {
                    response.body()?.let { callback.invoke(Result.success(it)) }
                }
            }

            override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                callback.invoke(Result.failure(t))
            }
        })
    }

    companion object {
        private var instance: CountryRemoteDataSource? = null

        @Synchronized
        fun getInstance(countryService: CountryService): CountryRemoteDataSource {
            if (instance == null) {
                instance = CountryRemoteDataSource(countryService)
            }
            return instance!!
        }
    }
}