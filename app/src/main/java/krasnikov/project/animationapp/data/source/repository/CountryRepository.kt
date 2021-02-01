package krasnikov.project.animationapp.data.source.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import krasnikov.project.animationapp.data.source.ICountryDataSource
import krasnikov.project.animationapp.data.model.Country
import krasnikov.project.animationapp.data.source.ICountryRepository

class CountryRepository(private val remoteDataSource: ICountryDataSource) : ICountryRepository {

    private val cache = mutableListOf<Country>()

    override fun observeCountries(): LiveData<List<Country>> {
        val mutableLiveData = MutableLiveData<List<Country>>()

        if (cache.isEmpty()) {
            updateCountriesFromRemoteDataSource {
                mutableLiveData.postValue(cache)
            }
        } else {
            mutableLiveData.postValue(cache)
        }

        return mutableLiveData
    }

    override fun observeCountry(name: String): LiveData<Country> {
        val mutableLiveData = MutableLiveData<Country>()

        if (cache.isEmpty()) {
            updateCountriesFromRemoteDataSource {
                mutableLiveData.postValue(getCountryByName(name))
            }
        } else {
            mutableLiveData.postValue(getCountryByName(name))
        }

        return mutableLiveData
    }

    private fun updateCountriesFromRemoteDataSource(callbackFinished: () -> Unit) {
        remoteDataSource.getCountries {
            if (it.isSuccess) {
                cache.clear()
                cache.addAll(it.getOrDefault(emptyList()))
            }
            callbackFinished.invoke()
        }
    }

    private fun getCountryByName(name: String): Country? {
        return cache.find { country -> country.name == name }
    }

    companion object {
        private var instance: CountryRepository? = null

        @Synchronized
        fun getInstance(dataSource: ICountryDataSource): CountryRepository {
            if (instance == null) {
                instance = CountryRepository(dataSource)
            }
            return instance!!
        }
    }
}