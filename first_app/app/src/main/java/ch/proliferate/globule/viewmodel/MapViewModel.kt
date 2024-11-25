package ch.proliferate.globule.viewmodel

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class MapUIState(
    val isLoading: Boolean,
    val polygons: List<List<com.google.android.gms.maps.model.LatLng>> = emptyList(),
    val selectedCountryName: String = "",
    val highlighted: Boolean = true
)

@HiltViewModel
class MapViewModel @Inject constructor() : ViewModel() {
  private var _uiState = MutableStateFlow(MapUIState(isLoading = true))
  val uiState: StateFlow<MapUIState> = _uiState.asStateFlow()

  fun finishLoading() {
    _uiState.value = _uiState.value.copy(isLoading = false)
  }

  fun updatePolygons(polygons: List<List<LatLng>>) {
    _uiState.value = _uiState.value.copy(polygons = polygons)
  }

  fun updateSelectedCountryName(name: String) {
    _uiState.value = _uiState.value.copy(selectedCountryName = name)
  }

  fun toggleHighlight() {
    _uiState.value = _uiState.value.copy(highlighted = !_uiState.value.highlighted)
  }
}
