package ch.proliferate.globule.model

import android.content.Context
import ch.proliferate.globule.R
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class CountryPolygons(private val activity: Context) {

  private var countryPolygons: Map<String, List<List<LatLng>>> = emptyMap()
  private var countryMiddlePoints: MutableMap<String, LatLng> = mutableMapOf()
  // private var sharedPref = activity.getSharedPreferences("countryPolygons", Context.MODE_PRIVATE)

  suspend fun loadData() {

    val jsonObject =
        withContext(Dispatchers.IO) {
          try {
            val inputStream = activity.resources.openRawResource(R.raw.countries)
            val geoJsonString = inputStream.bufferedReader().use { it.readText() }
            JSONObject(geoJsonString)
          } catch (e: Exception) {
            e.printStackTrace()
            null
          }
        }
    jsonObject?.let { countryPolygons = parseJson(it) }
    loadCountryMiddlePoints()
  }

  private fun parseJson(jsonObject: JSONObject): Map<String, List<List<LatLng>>> {
    val polygonsMap = mutableMapOf<String, List<List<LatLng>>>()
    val features = jsonObject.getJSONArray("features")
    for (i in 0 until features.length()) {
      val feature = features.getJSONObject(i)
      val properties = feature.getJSONObject("properties")
      val countryName = properties.getString("ADMIN")
      val geometry = feature.getJSONObject("geometry")
      val coordinates = geometry.getJSONArray("coordinates")
      val polygons = mutableListOf<List<LatLng>>()
      for (j in 0 until coordinates.length()) {
        val polygon = coordinates.getJSONArray(j)
        val latLngs = mutableListOf<LatLng>()
        for (k in 0 until polygon.length()) {
          val ring = polygon.getJSONArray(k)
          for (l in 0 until ring.length()) {
            val point = ring.getJSONArray(l)
            val lat = point.getDouble(1)
            val lng = point.getDouble(0)
            latLngs.add(LatLng(lat, lng))
          }
        }
        polygons.add(latLngs)
      }
      polygonsMap[countryName] = polygons
    }
    return polygonsMap
  }

  private suspend fun loadCountryMiddlePoints() {
    withContext(Dispatchers.IO) {
      try {
        val inputStream = activity.resources.openRawResource(R.raw.countries_mid_points)
        val reader = inputStream.bufferedReader()
        reader.useLines { lines ->
          lines.drop(1).forEach { line ->
            val tokens = line.split(",")
            if (tokens.size == 4) {
              val countryName = tokens[3]
              val latitude = tokens[1].toDouble()
              val longitude = tokens[2].toDouble()
              countryMiddlePoints[countryName] = LatLng(latitude, longitude)
            }
          }
        }
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }
  }

  fun getPolygonsForCountry(countryName: String): List<List<LatLng>> {
    return countryPolygons[countryName] ?: emptyList()
  }

  fun getMiddlePointForCountry(countryName: String): LatLng? {
    return countryMiddlePoints[countryName]
  }
}
