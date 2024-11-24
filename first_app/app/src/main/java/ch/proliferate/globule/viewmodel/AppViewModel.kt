package ch.proliferate.globule.viewmodel

import androidx.lifecycle.ViewModel
import ch.proliferate.globule.ui.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class UIState(val userLoggedIn: Boolean)

@HiltViewModel
class AppViewModel @Inject constructor() : ViewModel() {

  private var _uiState = MutableStateFlow(UIState(userLoggedIn = false))
  val uiState: StateFlow<UIState> = _uiState.asStateFlow()

  fun getStartDestination(): String {
    return if (_uiState.value.userLoggedIn) Route.Main.asString else Route.Login.asString
    return "Main"
  }

  fun getRoutes(): List<Route> {
    return listOf(Route.Main, Route.Profile, Route.Login)
  }

  fun signOut() {
    _uiState.value = _uiState.value.copy(userLoggedIn = false)
  }

  fun login() {
    _uiState.value = _uiState.value.copy(userLoggedIn = true)
  }
}
