package krasnikov.project.animationapp.data.source

import androidx.lifecycle.LiveData
import krasnikov.project.animationapp.data.model.Country

interface ICountryRepository {

    fun observeCountries(): LiveData<List<Country>>

    fun observeCountry(name: String): LiveData<Country>
}