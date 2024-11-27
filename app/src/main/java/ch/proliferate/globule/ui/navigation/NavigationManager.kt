package ch.proliferate.globule.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import ch.proliferate.globule.ui.screens.CountryScreen
import ch.proliferate.globule.ui.screens.LoginScreen
import ch.proliferate.globule.ui.screens.MapScreen
import ch.proliferate.globule.ui.screens.ProfileScreen
import ch.proliferate.globule.viewmodel.SharedViewModel

sealed class Route(val asString: String) {

  object Main : Route("Main")

  object Profile : Route("Profile")

  object Login : Route("Login")

  object Country : Route("Country")
}

class NavigationManager(private val navController: NavHostController) {

  @Composable
  fun ScreenFromRoute(route: Route, sharedViewModel: SharedViewModel) {
    when (route) {
      is Route.Main -> MapScreen(this, sharedViewModel)
      is Route.Profile -> ProfileScreen()
      is Route.Login -> LoginScreen(this)
      is Route.Country -> CountryScreen(this, sharedViewModel)
    }
  }

  fun navigateTo(routeString: String) {
    navController.navigate(routeString) {}
  }

  fun goBack() {
    navController.popBackStack()
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
