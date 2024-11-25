package ch.proliferate.globule.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import ch.proliferate.globule.ui.navigation.NavigationManager
import ch.proliferate.globule.viewmodel.UserSessionViewModel
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract

@Composable
fun LoginScreen(navManager: NavigationManager, modifier: Modifier = Modifier) {
  val userSessionViewModel: UserSessionViewModel = hiltViewModel()

  val signInLauncher = rememberLauncherForActivityResult(
    contract = FirebaseAuthUIActivityResultContract(),
  ) { result ->
    userSessionViewModel.onSignInResult(result)
    if (userSessionViewModel.userSessionState.value.userLoggedIn) {
      navManager.login()
    }
  }

  val signInIntent = userSessionViewModel.getSignInIntent()

  Column(modifier = modifier.fillMaxSize(),
    verticalArrangement = Arrangement.SpaceAround,
    horizontalAlignment = Alignment.CenterHorizontally) {
    Text("Globule")
    Button(
        onClick = {
          signInLauncher.launch(signInIntent)
        }) {Text("Sign-in with Google")}
    Button(
      onClick = {
        navManager.login()
      }) {Text("Continue as guest")}
  }
}
