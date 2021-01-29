package krasnikov.project.animationapp.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import krasnikov.project.animationapp.data.CountryDataSource
import krasnikov.project.animationapp.data.model.Country
import krasnikov.project.animationapp.data.remote.api.CountryService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class CountryRemoteDataSource(private val countryService: CountryService) : CountryDataSource {

    override fun getObserveCountries(): LiveData<List<Country>> {
        var countries: MutableLiveData<List<Country>> = MutableLiveData()

        countryService.getCountries().enqueue(object : Callback<List<Country>> {
            override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                if (response.isSuccessful) {
                    response.body()?.let { countries.postValue(it) }
                } else {
                    println("Server return an Error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                when (t) {
                    is IOException -> {
                        println("Network failure!!!")
                        t.printStackTrace()
                    }
                    else -> {
                        println("Error!!!")
                        t.printStackTrace()
                    }
                }
            }
        })
        return countries
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