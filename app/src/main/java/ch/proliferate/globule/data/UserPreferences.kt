package ch.proliferate.globule.data

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(@ApplicationContext context: Context) {
  private val sharedPreferences: SharedPreferences =
      context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
  var userLoggedIn: Boolean
    get() = sharedPreferences.getBoolean("user_logged_in", false)
    set(value) {
      with(sharedPreferences.edit()) {
        putBoolean("user_logged_in", value)
        apply()
      }
    }

  var userName: String?
    get() = sharedPreferences.getString("user_name", "")
    set(value) {
      with(sharedPreferences.edit()) {
        putString("user_name", value)
        apply()
      }
    }
}
