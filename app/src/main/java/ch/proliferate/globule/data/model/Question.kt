package ch.proliferate.globule.data.model

data class Question(val statement: String, val answer: Answer) {
  sealed class Answer {
    data class TrueFalse(val answer: Boolean) : Answer()

    data class MultipleChoice(val options: List<String>, val correctOption: Int) : Answer() {
      init {
        require(correctOption in options.indices) { "correctOption must be within bounds" }
      }
    }

    data class FillInBlank(val answer: String) : Answer()
  }
}
