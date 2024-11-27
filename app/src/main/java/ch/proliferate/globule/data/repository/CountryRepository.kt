package ch.proliferate.globule.data.repository

import android.content.Context
import ch.proliferate.globule.R
import ch.proliferate.globule.data.model.Country
import ch.proliferate.globule.data.model.CountryMapsProperties
import com.google.android.gms.maps.model.LatLng
import com.google.gson.stream.JsonReader
import java.io.InputStreamReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CountryRepository(private val context: Context) {

  private var countries: List<Country> = emptyList()

  fun getCountry(countryName: String): Country? {
    return countries.find { country -> country.name == countryName }
  }

  suspend fun loadData() {
    withContext(Dispatchers.IO) {
      val inputStream = context.resources.openRawResource(R.raw.countries)
      val reader = JsonReader(InputStreamReader(inputStream, "UTF-8"))
      countries = parseJsonInitCountriesWithPolygons(reader)
    }
  }

  private fun parseJsonInitCountriesWithPolygons(reader: JsonReader): List<Country> {
    val countries: MutableList<Country> = mutableListOf()

    reader.beginObject()
    while (reader.hasNext()) {
      val name = reader.nextName()
      if (name == "features") {
        reader.beginArray()
        while (reader.hasNext()) {
          countries.add(parseCountry(reader))
        }
        reader.endArray()
      } else {
        reader.skipValue()
      }
    }
    reader.endObject()
    return countries
  }

  private fun parseCountry(reader: JsonReader): Country {
    var countryName = ""
    val polygons: MutableList<List<LatLng>> = mutableListOf()

    reader.beginObject()
    while (reader.hasNext()) {
      when (reader.nextName()) {
        "properties" -> {
          reader.beginObject()
          while (reader.hasNext()) {
            if (reader.nextName() == "ADMIN") {
              countryName = reader.nextString()
            } else {
              reader.skipValue()
            }
          }
          reader.endObject()
        }
        "geometry" -> {
          reader.beginObject()
          while (reader.hasNext()) {
            if (reader.nextName() == "coordinates") {
              reader.beginArray()
              while (reader.hasNext()) {
                reader.beginArray()
                while (reader.hasNext()) {
                  val polygon = mutableListOf<LatLng>()
                  reader.beginArray()
                  while (reader.hasNext()) {
                    reader.beginArray()
                    val lng = reader.nextDouble()
                    val lat = reader.nextDouble()
                    polygon.add(LatLng(lat, lng))
                    reader.endArray()
                  }
                  polygons.add(polygon)
                  reader.endArray()
                }
                reader.endArray()
              }
              reader.endArray()
            } else {
              reader.skipValue()
            }
          }
          reader.endObject()
        }
        else -> reader.skipValue()
      }
    }
    reader.endObject()

    val mapsProperties = CountryMapsProperties(polygons = polygons)
    return Country(name = countryName, mapsProperties = mapsProperties)
  }
}
