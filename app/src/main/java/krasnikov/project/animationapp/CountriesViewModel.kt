package krasnikov.project.animationapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import krasnikov.project.animationapp.data.repository.CountryRepository


class CountriesViewModel(private val countryRepository: CountryRepository) : ViewModel() {

    val observeCountries by lazy { countryRepository.getObserveCountries() }

    class CountriesViewModelFactory(private val countryRepository: CountryRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CountriesViewModel::class.java)) {
                return CountriesViewModel(countryRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}