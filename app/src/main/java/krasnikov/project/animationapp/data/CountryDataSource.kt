package krasnikov.project.animationapp.data

import androidx.lifecycle.LiveData
import krasnikov.project.animationapp.data.model.Country

interface CountryDataSource {

    fun getObserveCountries(): LiveData<List<Country>>
}