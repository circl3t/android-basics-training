package ch.proliferate.globule.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import ch.proliferate.globule.ui.navigation.NavigationManager
import ch.proliferate.globule.viewmodel.AppViewModel

@Composable
fun LoginScreen(navManager: NavigationManager, modifier: Modifier = Modifier) {
  val appViewModel: AppViewModel = hiltViewModel()
  Column {
    Text("Login Screen.")
    Text("Sign in with google.")
    Text("Continue without signing in.")
    Button(
        onClick = {
          navManager.login()
          appViewModel.login()
        }) {}
  }
}
