package ch.proliferate.globule.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import ch.proliferate.globule.model.CountryPolygons
import ch.proliferate.globule.ui.screens.LoginScreen
import ch.proliferate.globule.ui.screens.MapScreen
import ch.proliferate.globule.ui.screens.ProfileScreen

sealed class Route(val asString: String) {
  object Main : Route("Main")

  object Profile : Route("Profile")

  object Login : Route("Login")
}

class NavigationManager(private val navController: NavHostController) {

  @Composable
  fun ScreenFromRoute(route: Route, isDataLoaded: Boolean, countryPolygons: CountryPolygons) {
    when (route) {
      is Route.Main ->
          MapScreen(
              navManager = this, isDataLoaded = isDataLoaded, countryPolygons = countryPolygons)
      is Route.Profile -> ProfileScreen()
      is Route.Login -> LoginScreen(this)
    }
  }

  fun signOut() {
    navController.navigate(Route.Login.asString) {
      popUpTo(navController.graph.startDestinationId) { inclusive = true }
      launchSingleTop = true
    }
  }

  fun login() {
    navController.navigate(Route.Main.asString) {
      popUpTo(navController.graph.startDestinationId) { inclusive = true }
      launchSingleTop = true
    }
  }
}
