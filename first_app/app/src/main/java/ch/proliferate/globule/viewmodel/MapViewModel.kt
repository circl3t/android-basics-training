package ch.proliferate.globule.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class MapUIState(val isLoading: Boolean)

@HiltViewModel
class MapViewModel @Inject constructor() : ViewModel() {
  private var _uiState = MutableStateFlow(MapUIState(isLoading = true))
  val uiState: StateFlow<MapUIState> = _uiState.asStateFlow()

  fun finishLoading() {
    _uiState.value = _uiState.value.copy(isLoading = false)
  }
}
