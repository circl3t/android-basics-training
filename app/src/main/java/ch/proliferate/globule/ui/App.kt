package ch.proliferate.globule.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ch.proliferate.globule.model.CountryPolygons
import ch.proliferate.globule.ui.navigation.NavigationManager
import ch.proliferate.globule.viewmodel.UserSessionViewModel
import kotlinx.coroutines.launch

@Composable
fun App() {
  val viewModel: UserSessionViewModel = hiltViewModel()
  val navController: NavHostController = rememberNavController()
  val navManager: NavigationManager = NavigationManager(navController)
  val context = LocalContext.current
  val countryPolygons = remember { CountryPolygons(activity = context) }
  val scope = rememberCoroutineScope()
  var isDataLoaded by remember { mutableStateOf(false) }
  LaunchedEffect(Unit) {
    scope.launch {
      countryPolygons.loadData()
      isDataLoaded = true
    }
  }
  Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
    NavHost(
        navController = navController,
        startDestination = viewModel.getStartDestination(),
        modifier = Modifier.padding(innerPadding)) {
          val routes = viewModel.getRoutes()
          routes.forEach { route ->
            composable(route.asString) {
              navManager.ScreenFromRoute(route, isDataLoaded, countryPolygons)
            }
          }
        }
  }
}
// CI
