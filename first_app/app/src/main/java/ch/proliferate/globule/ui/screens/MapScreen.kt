package ch.proliferate.globule.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ch.proliferate.globule.model.CountryPolygons
import ch.proliferate.globule.ui.navigation.NavigationManager
import ch.proliferate.globule.viewmodel.AppViewModel
import ch.proliferate.globule.viewmodel.MapViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    navManager: NavigationManager,
    isDataLoaded: Boolean,
    countryPolygons: CountryPolygons
) {

  val viewModel: AppViewModel = hiltViewModel()
  val mapViewModel: MapViewModel = hiltViewModel()
  val singapore = LatLng(1.35, 103.87)
  val singaporeMarkerState = rememberMarkerState(position = singapore)
  val cameraPositionState = rememberCameraPositionState {
    position = CameraPosition.fromLatLngZoom(singapore, 10f)
  }

  val mapState = mapViewModel.uiState.collectAsStateWithLifecycle()
  val isLoading = mapState.value.isLoading

  val infiniteTransition = rememberInfiniteTransition()

  var polygons by remember { mutableStateOf<List<List<LatLng>>>(emptyList()) }

  var selectedCountryName by remember { mutableStateOf<String>("") }

  var highlighted by remember { mutableStateOf(true) }

  val alpha by
      infiniteTransition.animateFloat(
          initialValue = 0.0f,
          targetValue = 0.4f,
          animationSpec =
              infiniteRepeatable(
                  animation = tween(2000, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
      )

  LaunchedEffect(isDataLoaded, selectedCountryName) {
    if (isDataLoaded && selectedCountryName.isNotEmpty()) {
      polygons = countryPolygons.getPolygonsForCountry(selectedCountryName)
    }
  }

  Box() {
    Column {
      Row {
        TextField(
            value = selectedCountryName,
            onValueChange = {
              selectedCountryName = it
              val middlePoint = countryPolygons.getMiddlePointForCountry(it)
              if (middlePoint != null) {
                cameraPositionState.position = CameraPosition.fromLatLngZoom(middlePoint, 5f)
              }
            },
            enabled = isDataLoaded,
            label = { Text(if (isDataLoaded) "Enter country name" else "Loading...") })
        Button(
            onClick = {
              viewModel.signOut()
              navManager.signOut()
            }) {
              Text("Sign out")
            }
        Button(
          onClick = {highlighted = !highlighted},
          colors = ButtonColors(
            containerColor = if (highlighted) Color.Green else Color.LightGray,
            contentColor = if (highlighted) Color.Green else Color.LightGray,
            disabledContainerColor = Color.Black,
            disabledContentColor = Color.Black
          )
        ) {
          Text("O")
        }
      }

      GoogleMap(
          modifier = Modifier.fillMaxSize(),
          cameraPositionState = cameraPositionState,
          onMapLoaded = { mapViewModel.finishLoading() }) {
            polygons.forEach { points ->
              // Polyline(points = points, color = Color.Blue)
              Polygon(
                  points = points,
                  fillColor = Color.Green.copy(alpha = if (highlighted) 0.5f else 0f),
                  strokeColor = Color.Transparent)
            }
            if (!isLoading) {
              Marker(
                  state = singaporeMarkerState,
                  title = "Singapore",
                  snippet = "Marker in Singapore")
            }
          }
    }

    if (isLoading) {
      Box(
          modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.80f)),
          contentAlignment = Alignment.Center) {
            LoadingScreen()
          }
    }
  }
}
