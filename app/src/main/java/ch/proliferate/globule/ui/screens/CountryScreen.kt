package ch.proliferate.globule.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ch.proliferate.globule.ui.components.Quiz
import ch.proliferate.globule.ui.navigation.NavigationManager
import ch.proliferate.globule.viewmodel.CountryViewModel
import ch.proliferate.globule.viewmodel.SharedViewModel
import coil3.compose.AsyncImage

@Composable
fun CountryScreen(navigationManager: NavigationManager, sharedViewModel: SharedViewModel) {

  val countryViewModel = hiltViewModel<CountryViewModel>()
  val countryState = countryViewModel.uiState.collectAsStateWithLifecycle().value
  val sharedState = sharedViewModel.uiState.collectAsStateWithLifecycle().value

  LaunchedEffect(Unit) {
    countryViewModel.getQuizAndImageURL(sharedViewModel.uiState.value.selectedCountry)
  }

  val navManager = navigationManager
  Column(
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.fillMaxSize()) {
        if (countryState.quizHidden) {
          Column(modifier = Modifier.weight(10f).fillMaxSize()) {
            AsyncImage(
              modifier = Modifier.weight(1f).fillMaxWidth(),
              model = countryState.imageURL,
              contentDescription = "Image for ${sharedState.selectedCountry}",
              contentScale = ContentScale.Crop
            )
            Box(modifier = Modifier.weight(3f).fillMaxWidth()) {
              TextButton(onClick = {countryViewModel.showQuiz()}) {
                Text("open quiz")
              }
            }
          }
        } else {
          Quiz(modifier = Modifier.weight(10f), countryName = sharedState.selectedCountry, quiz = countryState.quiz,
            onHideClick = {countryViewModel.hideQuiz()})
        }
        Button(modifier = Modifier.weight(1f), onClick = { navManager.goBack() }) { Text("Go back") }
      }
}
