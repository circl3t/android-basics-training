package ch.proliferate.globule.viewmodel

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.proliferate.globule.model.UserPreferences
import ch.proliferate.globule.ui.navigation.Route
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UserSessionState(val userLoggedIn: Boolean, val userName: String)

@HiltViewModel
class UserSessionViewModel @Inject constructor(private val userPreferences: UserPreferences) : ViewModel() {

  private var _userSessionState = MutableStateFlow(UserSessionState(userLoggedIn = userPreferences.userLoggedIn, userName = userPreferences.userName ?: ""))
  val userSessionState: StateFlow<UserSessionState> = _userSessionState.asStateFlow()

  fun getSignInIntent(): Intent {
    val providers = listOf(
      AuthUI.IdpConfig.GoogleBuilder().build()
    )
    return AuthUI.getInstance()
      .createSignInIntentBuilder()
      .setAvailableProviders(providers)
      .build()
  }

  fun getStartDestination(): String {
    return if (_userSessionState.value.userLoggedIn) Route.Main.asString else Route.Login.asString
  }

  fun getRoutes(): List<Route> {
    return listOf(Route.Main, Route.Profile, Route.Login, Route.Country)
  }

  fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
    val response = result.idpResponse
    if (result.resultCode == RESULT_OK) {
      val userName = response?.email ?: ""
      signIn(userName)
    } else {
      Log.d("UserSessionViewModel", "Sign-in failed")
    }
  }

  fun signIn(userName: String) {
    viewModelScope.launch {
      userPreferences.userLoggedIn = true
      userPreferences.userName = userName
      _userSessionState.value = _userSessionState.value.copy(userLoggedIn = true, userName = userName)
    }
  }

  fun signOut() {
    viewModelScope.launch {
      userPreferences.userLoggedIn = false
      userPreferences.userName = ""
      _userSessionState.value = _userSessionState.value.copy(userLoggedIn = false, userName = "")
    }
  }

}
