package ch.proliferate.globule.data.model

data class CountryQuiz(
    val questions: List<Question>,
) {
  init {
    require(questions.isNotEmpty()) { "Quiz can't be empty" }
  }
}
