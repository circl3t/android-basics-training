package ch.proliferate.globule.ui.screens

import android.util.Log
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ch.proliferate.globule.model.CountryPolygons
import ch.proliferate.globule.ui.navigation.NavigationManager
import ch.proliferate.globule.ui.navigation.Route
import ch.proliferate.globule.viewmodel.MapViewModel
import ch.proliferate.globule.viewmodel.UserSessionViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
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

  val userSessionViewModel: UserSessionViewModel = hiltViewModel()
  val mapViewModel: MapViewModel = hiltViewModel()
  val singapore = LatLng(1.35, 103.87)
  val singaporeMarkerState = rememberMarkerState(position = singapore)
  val cameraPositionState = rememberCameraPositionState {
    position = CameraPosition.fromLatLngZoom(singapore, 10f)
  }

  val userSessionState = userSessionViewModel.userSessionState.collectAsStateWithLifecycle().value
  val mapState = mapViewModel.uiState.collectAsStateWithLifecycle().value
  val mapIsLoading = mapState.isLoading

  val infiniteTransition = rememberInfiniteTransition()

  val alpha by
      infiniteTransition.animateFloat(
          initialValue = 0.0f,
          targetValue = 0.4f,
          animationSpec =
              infiniteRepeatable(
                  animation = tween(2000, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
          label = "",
      )

  LaunchedEffect(isDataLoaded, mapState.selectedCountryName) {
    if (isDataLoaded && mapState.selectedCountryName.isNotEmpty()) {
      val polygons = countryPolygons.getPolygonsForCountry(mapState.selectedCountryName)
      mapViewModel.updatePolygons(polygons)
      val bounds = calculateLatLngBounds(polygons)
      if (bounds != null) {
        cameraPositionState.move(CameraUpdateFactory.newLatLngBounds(bounds, 100))
      }
      //            val middlePoint = countryPolygons.getMiddlePointForCountry(selectedCountryName)
      //            if (middlePoint != null) {
      //                cameraPositionState.position = CameraPosition.fromLatLngZoom(middlePoint,
      // cameraPositionState.position.zoom)
      //            }
    }
  }

  Box() {
    Column {
      Row {
        TextField(
            value = mapState.selectedCountryName,
            onValueChange = { mapViewModel.updateSelectedCountryName(it) },
            enabled = isDataLoaded,
            label = { Text(if (isDataLoaded) "Enter country name" else "Loading...") })
        Button(
            onClick = {
              userSessionViewModel.signOut()
              navManager.signOut()
            }) {
              Text("Sign out")
            }
        Button(
            onClick = { mapViewModel.toggleHighlight() },
            colors =
                ButtonColors(
                    containerColor = if (mapState.highlighted) Color.Green else Color.LightGray,
                    contentColor = if (mapState.highlighted) Color.Green else Color.LightGray,
                    disabledContainerColor = Color.Black,
                    disabledContentColor = Color.Black)) {
              Text("O")
            }
      }

      Text("hello ${userSessionState.userName}")
      Log.d("xx", userSessionState.userName)

      GoogleMap(
          modifier = Modifier.fillMaxSize(),
          cameraPositionState = cameraPositionState,
          onMapLoaded = { mapViewModel.finishLoading() },
      ) {
        mapState.polygons.forEach { points ->
          // Polyline(points = points, color = Color.Blue)
          Polygon(
              points = points,
              clickable = true,
              fillColor = Color.Magenta.copy(alpha = if (mapState.highlighted) 0.25f else 0f),
              strokeColor = Color.Transparent,
              onClick = { navManager.navigateTo(Route.Country.asString) })
        }
        if (!mapIsLoading) {
          Marker(state = singaporeMarkerState, title = "Singapore", snippet = "Marker in Singapore")
        }
      }
    }

    if (mapIsLoading) {
      Box(
          modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.80f)),
          contentAlignment = Alignment.Center) {
            LoadingScreen()
          }
    }
  }
}

fun calculateLatLngBounds(polygons: List<List<LatLng>>): LatLngBounds? {
  if (polygons.isEmpty()) return null
  val builder = LatLngBounds.builder()
  polygons.flatten().forEach { builder.include(it) }
  return builder.build()
}
