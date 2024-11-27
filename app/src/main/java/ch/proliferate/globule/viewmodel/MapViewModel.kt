package ch.proliferate.globule.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.proliferate.globule.data.CountryDecorationsRepository
import ch.proliferate.globule.data.repository.CountryRepository
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MapUIState(
    val isLoading: Boolean,
    val polygons: List<List<LatLng>> = emptyList(),
    //val selectedCountryName: String = "",
    val highlighted: Boolean = true,
    val capitalName: String = "",
    val bounds: LatLngBounds? = null,
    val cameraPosition: CameraPosition,
    val markerPosition: LatLng
)

@HiltViewModel
class MapViewModel
@Inject
constructor(
    private val decorationsRepository: CountryDecorationsRepository,
    private val countryRepository: CountryRepository
) : ViewModel() {

  private var _uiState =
      MutableStateFlow(
          MapUIState(
              isLoading = true,
              cameraPosition = CameraPosition.fromLatLngZoom(getStartLatLng(), 10f),
              markerPosition = getStartLatLng()))
  val uiState: StateFlow<MapUIState> = _uiState.asStateFlow()

  fun getStartLatLng(): LatLng {
    return LatLng(0.0, 0.0)
  }

  fun finishLoading() {
    _uiState.value = _uiState.value.copy(isLoading = false)
  }

  fun updatePolygons(polygons: List<List<LatLng>>) {
    _uiState.value = _uiState.value.copy(polygons = polygons)
  }

  fun fetchCountryData(name: String) {
    viewModelScope.launch {
      val polygons = countryRepository.getCountry(countryName = name)?.mapsProperties?.polygons
      var bounds: LatLngBounds? = null
      if (polygons != null) {
        updatePolygons(polygons)
        bounds = calculateLatLngBounds(polygons)
        if (bounds != null) {
          _uiState.value = _uiState.value.copy(bounds = bounds)
        }
      }
    }

    decorationsRepository.getCountryDecorations(
        name,
        callback = { data ->
          val capitalData = data?.get("capital") as? Map<String, Any>

          if (capitalData != null) {
            val latLng = capitalData["first"] as? Map<String, Double>
            val capitalName = capitalData["second"] as? String
            if (latLng != null && capitalName != null) {
              val lat = latLng["latitude"]
              val lng = latLng["longitude"]
              if (lat != null && lng != null) {
                _uiState.value =
                    _uiState.value.copy(
                        markerPosition = LatLng(lat, lng), capitalName = capitalName)
              }
            }
          }
        })
  }

  fun toggleHighlight() {
    _uiState.value = _uiState.value.copy(highlighted = !_uiState.value.highlighted)
  }

  private fun calculateLatLngBounds(polygons: List<List<LatLng>>): LatLngBounds? {
    if (polygons.isEmpty()) return null
    val builder = LatLngBounds.builder()
    polygons.flatten().forEach { builder.include(it) }
    return builder.build()
  }
}
