package ch.proliferate.globule.data.model

import com.google.android.gms.maps.model.LatLng

data class CountryMapsProperties(
    val polygons: List<List<LatLng>>,
    val capital: Capital? = null,
) {
  data class Capital(
      val name: String,
      val location: LatLng,
  )
}
