package krasnikov.project.animationapp.data.source.remote.api

import krasnikov.project.animationapp.data.model.Country
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface CountryService {

    @GET("all")
    fun getCountries(): Call<List<Country>>

    companion object {
        private const val baseUrl = "https://restcountries.eu/rest/v2/"

        private val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        fun provideCountryService(): CountryService =
            retrofit.create(CountryService::class.java)
    }
}