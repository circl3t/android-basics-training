package ch.proliferate.globule.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
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
import ch.proliferate.globule.ui.navigation.NavigationManager
import ch.proliferate.globule.ui.navigation.Route
import ch.proliferate.globule.viewmodel.MapViewModel
import ch.proliferate.globule.viewmodel.SharedViewModel
import ch.proliferate.globule.viewmodel.UserSessionViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MapColorScheme
import com.google.maps.android.compose.ComposeMapColorScheme
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

val validCountryNames =
    listOf(
        "Afghanistan",
        "Albania",
        "Algeria",
        "Andorra",
        "Angola",
        "Antigua and Barbuda",
        "Argentina",
        "Armenia",
        "Australia",
        "Austria",
        "Azerbaijan",
        "Bahamas",
        "Bahrain",
        "Bangladesh",
        "Barbados",
        "Belarus",
        "Belgium",
        "Belize",
        "Benin",
        "Bhutan",
        "Bolivia",
        "Bosnia and Herzegovina",
        "Botswana",
        "Brazil",
        "Brunei",
        "Bulgaria",
        "Burkina Faso",
        "Burundi",
        "Cabo Verde",
        "Cambodia",
        "Cameroon",
        "Canada",
        "Central African Republic",
        "Chad",
        "Chile",
        "China",
        "Colombia",
        "Comoros",
        "Congo, Democratic Republic of the",
        "Congo, Republic of the",
        "Costa Rica",
        "Croatia",
        "Cuba",
        "Cyprus",
        "Czech Republic",
        "Denmark",
        "Djibouti",
        "Dominica",
        "Dominican Republic",
        "Ecuador",
        "Egypt",
        "El Salvador",
        "Equatorial Guinea",
        "Eritrea",
        "Estonia",
        "Eswatini",
        "Ethiopia",
        "Fiji",
        "Finland",
        "France",
        "Gabon",
        "Gambia",
        "Georgia",
        "Germany",
        "Ghana",
        "Greece",
        "Grenada",
        "Guatemala",
        "Guinea",
        "Guinea-Bissau",
        "Guyana",
        "Haiti",
        "Honduras",
        "Hungary",
        "Iceland",
        "India",
        "Indonesia",
        "Iran",
        "Iraq",
        "Ireland",
        "Israel",
        "Italy",
        "Jamaica",
        "Japan",
        "Jordan",
        "Kazakhstan",
        "Kenya",
        "Kiribati",
        "North Korea",
        "South Korea",
        "Kosovo",
        "Kuwait",
        "Kyrgyzstan",
        "Laos",
        "Latvia",
        "Lebanon",
        "Lesotho",
        "Liberia",
        "Libya",
        "Liechtenstein",
        "Lithuania",
        "Luxembourg",
        "Madagascar",
        "Malawi",
        "Malaysia",
        "Maldives",
        "Mali",
        "Malta",
        "Marshall Islands",
        "Mauritania",
        "Mauritius",
        "Mexico",
        "Micronesia",
        "Moldova",
        "Monaco",
        "Mongolia",
        "Montenegro",
        "Morocco",
        "Mozambique",
        "Myanmar",
        "Namibia",
        "Nauru",
        "Nepal",
        "Netherlands",
        "New Zealand",
        "Nicaragua",
        "Niger",
        "Nigeria",
        "North Macedonia",
        "Norway",
        "Oman",
        "Pakistan",
        "Palau",
        "Palestine",
        "Panama",
        "Papua New Guinea",
        "Paraguay",
        "Peru",
        "Philippines",
        "Poland",
        "Portugal",
        "Qatar",
        "Romania",
        "Russia",
        "Rwanda",
        "Saint Kitts and Nevis",
        "Saint Lucia",
        "Saint Vincent and the Grenadines",
        "Samoa",
        "San Marino",
        "Sao Tome and Principe",
        "Saudi Arabia",
        "Senegal",
        "Serbia",
        "Seychelles",
        "Sierra Leone",
        "Singapore",
        "Slovakia",
        "Slovenia",
        "Solomon Islands",
        "Somalia",
        "South Africa",
        "South Sudan",
        "Spain",
        "Sri Lanka",
        "Sudan",
        "Suriname",
        "Sweden",
        "Switzerland",
        "Syria",
        "Taiwan",
        "Tajikistan",
        "Tanzania",
        "Thailand",
        "Timor-Leste",
        "Togo",
        "Tonga",
        "Trinidad and Tobago",
        "Tunisia",
        "Turkey",
        "Turkmenistan",
        "Tuvalu",
        "Uganda",
        "Ukraine",
        "United Arab Emirates",
        "United Kingdom",
        "United States of America",
        "Uruguay",
        "Uzbekistan",
        "Vanuatu",
        "Vatican City",
        "Venezuela",
        "Vietnam",
        "Yemen",
        "Zambia",
        "Zimbabwe")

@Composable
fun MapScreen(
    navManager: NavigationManager,
    sharedViewModel: SharedViewModel
) {

  val userSessionViewModel: UserSessionViewModel = hiltViewModel()
  val mapViewModel: MapViewModel = hiltViewModel()

  val userSessionState = userSessionViewModel.userSessionState.collectAsStateWithLifecycle().value
  val mapState = mapViewModel.uiState.collectAsStateWithLifecycle().value

  val markerState = rememberMarkerState(position = mapState.markerPosition)
  val cameraPositionState = rememberCameraPositionState { position = mapState.cameraPosition }

  val mapIsLoading = mapState.isLoading

  val isDataLoaded = userSessionState.isDataLoaded

  LaunchedEffect(mapState.markerPosition) { markerState.position = mapState.markerPosition }

  LaunchedEffect(mapState.cameraPosition) { cameraPositionState.position = mapState.cameraPosition }

  LaunchedEffect(mapState.bounds) {
    if (mapState.bounds != null) {
      cameraPositionState.move(CameraUpdateFactory.newLatLngBounds(mapState.bounds, 100))
    }
  }

  Box() {
    Column {
      Row {
        var fieldValue by remember { mutableStateOf("") }
        TextField(
            value = fieldValue,
            onValueChange = {
              fieldValue = it
              if (fieldValue in validCountryNames) {
                sharedViewModel.updateSelectedCountry(fieldValue)
                mapViewModel.fetchCountryData(fieldValue)
              }
            },
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

      GoogleMap(
          modifier = Modifier.fillMaxSize(),
          cameraPositionState = cameraPositionState,
          onMapLoaded = { mapViewModel.finishLoading() },
          mapColorScheme = ComposeMapColorScheme.FOLLOW_SYSTEM
      ) {
        mapState.polygons.forEach { points ->
          // Polyline(points = points, color = Color.Blue)
          Polygon(
              points = points,
              clickable = true,
              fillColor = Color.Blue.copy(alpha = if (mapState.highlighted) 0.25f else 0f),
              strokeColor = Color.Transparent,
              onClick = { navManager.navigateTo(Route.Country.asString) })
        }
        if (!mapIsLoading) {
          Marker(state = markerState)
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
