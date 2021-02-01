package krasnikov.project.animationapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import krasnikov.project.animationapp.data.source.repository.CountryRepository

class CountriesViewModel(private val countryRepository: CountryRepository) : ViewModel() {

    val observeCountries by lazy { countryRepository.observeCountries() }

    fun observeCountry(name: String) = countryRepository.observeCountry(name)

    class CountriesViewModelFactory(private val countryRepository: CountryRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CountriesViewModel(countryRepository) as T
        }
    }
}