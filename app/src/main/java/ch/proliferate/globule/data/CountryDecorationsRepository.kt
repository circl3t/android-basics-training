package ch.proliferate.globule.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CountryDecorationsRepository @Inject constructor(private val firestore: FirebaseFirestore) {
  fun setCountryDecorations(countryName: String, decorations: Map<String, Any>) {
    firestore
        .collection("country_decorations")
        .document(countryName)
        .set(decorations)
        .addOnSuccessListener { Log.d("logdd", "success") }
        .addOnFailureListener { e -> Log.d("logdd", e.message ?: "") }
  }

  fun getCountryDecorations(countryName: String, callback: (Map<String, Any>?) -> Unit) {
    firestore
        .collection("country_decorations")
        .document(countryName)
        .get()
        .addOnSuccessListener { document ->
          Log.d("get", "${document != null} ${document.exists()}")
          if (document != null && document.exists()) {
            Log.d("get", "entered if statement")
            callback(document.data)
            Log.d("get", "after call back")
          } else {
            // callback(null)
          }
        }
        .addOnFailureListener { e ->
          Log.d("get", e.message ?: "error")
          // callback(null)
        }
  }
}
