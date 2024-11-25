package ch.proliferate.globule.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ch.proliferate.globule.ui.navigation.NavigationManager

@Composable
fun CountryScreen(navigationManager: NavigationManager, modifier: Modifier = Modifier) {
  val navManager = navigationManager
  Column(
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = modifier.fillMaxSize()) {
        Text("under development")
        Button(onClick = { navManager.goBack() }) { Text("Go back") }
      }
}
