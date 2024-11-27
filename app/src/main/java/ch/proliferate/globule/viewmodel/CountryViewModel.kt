package ch.proliferate.globule.viewmodel

import androidx.lifecycle.ViewModel
import ch.proliferate.globule.data.CountryDecorationsRepository
import ch.proliferate.globule.data.model.CountryQuiz
import ch.proliferate.globule.data.model.Question
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class CountryUIState(
  val quiz: CountryQuiz? = null,
  val quizHidden: Boolean = true,
  val imageURL: String? = null,
)

@HiltViewModel
class CountryViewModel
@Inject
constructor(
  private val decorationsRepository: CountryDecorationsRepository,
): ViewModel() {
  private var _uiState = MutableStateFlow(CountryUIState())
  val uiState: StateFlow<CountryUIState> = _uiState.asStateFlow()

  fun showQuiz() {
    _uiState.value = _uiState.value.copy(quizHidden = false)
  }

  fun hideQuiz() {
    _uiState.value = _uiState.value.copy(quizHidden = true)
  }



  fun getQuizAndImageURL(countryName: String) {
    decorationsRepository.getCountryDecorations(countryName, callback = {
      data ->
      val quizData = data?.get("Quiz") as? Map<String, Any>

      if (quizData != null) {
        val first = quizData["first"] as? Map<String, String>
        val second = quizData["second"] as? Map<String, String>
        val firstQuestion = Question(
          statement = first?.get("question") ?: "",
          answer = Question.Answer.FillInBlank(first?.get("answer") ?: "")
        )

        val secondQuestion = Question(
          statement = second?.get("question") ?: "",
          answer = Question.Answer.FillInBlank(second?.get("answer") ?: "")
        )
        _uiState.value = _uiState.value.copy(quiz = CountryQuiz(questions = listOf(firstQuestion, secondQuestion)))
      }

      val imageURLData = data?.get("imageURL") as? String

      if (imageURLData != null) {
        _uiState.value = _uiState.value.copy(imageURL = imageURLData)
      }
    })
  }

}
