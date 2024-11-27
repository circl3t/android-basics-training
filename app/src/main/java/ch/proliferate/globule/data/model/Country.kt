package ch.proliferate.globule.data.model

data class Country(
    val name: String,
    val mapsProperties: CountryMapsProperties,
    val quiz: CountryQuiz? = null,
)
