package krasnikov.project.animationapp

import android.app.Application
import krasnikov.project.animationapp.data.source.remote.CountryRemoteDataSource
import krasnikov.project.animationapp.data.source.remote.api.CountryService
import krasnikov.project.animationapp.data.source.repository.CountryRepository

class App : Application() {
    private val countryService by lazy { CountryService.provideCountryService() }
    private val countryRemoteDataSource by lazy { CountryRemoteDataSource.getInstance(countryService) }
    val countryRepository by lazy { CountryRepository.getInstance(countryRemoteDataSource) }

}