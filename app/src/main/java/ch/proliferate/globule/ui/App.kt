package ch.proliferate.globule.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ch.proliferate.globule.ui.navigation.NavigationManager
import ch.proliferate.globule.viewmodel.MapViewModel
import ch.proliferate.globule.viewmodel.SharedViewModel
import ch.proliferate.globule.viewmodel.UserSessionViewModel

@Composable
fun App() {
  val navController: NavHostController = rememberNavController()
  val navManager: NavigationManager = NavigationManager(navController)
  val userSessionViewModel: UserSessionViewModel = hiltViewModel()
  val sharedViewModel: SharedViewModel = hiltViewModel()
  Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = userSessionViewModel.getStartDestination(),
            modifier = Modifier.padding(innerPadding)) {
              val routes = userSessionViewModel.getRoutes()
              routes.forEach { route ->
                composable(route.asString) { navManager.ScreenFromRoute(route, sharedViewModel) }
              }
            }
      }
}
