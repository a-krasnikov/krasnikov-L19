package krasnikov.project.animationapp.data.source

import krasnikov.project.animationapp.data.model.Country

interface ICountryDataSource {

    fun getCountries(callback: (result: Result<List<Country>>) -> Unit)
}