package krasnikov.project.animationapp.data.model

import com.google.gson.annotations.SerializedName

class Country(
    @SerializedName("name")
    val name: String,
    @SerializedName("population")
    val population: Int,
    @SerializedName("area")
    val area: Double
) {
    val populationDensity
        get() = population / area
}