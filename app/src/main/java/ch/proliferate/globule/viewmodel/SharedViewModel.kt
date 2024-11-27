package ch.proliferate.globule.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class SharedUIState(
  val selectedCountry: String
)

@HiltViewModel
class SharedViewModel
@Inject
constructor(

): ViewModel() {
  private var _uiState = MutableStateFlow(SharedUIState(""))
  val uiState: StateFlow<SharedUIState> = _uiState.asStateFlow()
  fun updateSelectedCountry(name: String) {
    _uiState.value = _uiState.value.copy(name)
  }
}
