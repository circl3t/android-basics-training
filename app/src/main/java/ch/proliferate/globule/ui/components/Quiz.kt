package ch.proliferate.globule.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import ch.proliferate.globule.data.model.CountryQuiz
import ch.proliferate.globule.data.model.Question
import ch.proliferate.globule.data.model.Question.Answer

@Composable
fun Quiz(modifier: Modifier, countryName: String, quiz: CountryQuiz?, onHideClick: () -> Unit) {
  var currentIndex by remember { mutableIntStateOf(0) }



  Column(
    modifier = modifier.fillMaxSize(),
  ) {
    if (quiz == null) { Text("no quiz to display :(") }
    else {
      Row (
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Box(modifier = Modifier
          .fillMaxHeight()
          .weight(2f))
        Column(modifier = Modifier
          .fillMaxHeight()
          .weight(4f)) {
          Text(modifier = Modifier
            .weight(1f)
            .fillMaxWidth(), text = "Quiz for", textAlign = TextAlign.Center, style = MaterialTheme.typography.headlineSmall)
          Text(modifier = Modifier
            .weight(3f)
            .fillMaxWidth(), text = countryName, textAlign = TextAlign.Center, style = MaterialTheme.typography.headlineLarge)
        }
        IconButton(modifier = Modifier
          .fillMaxHeight()
          .weight(2f), onClick = {onHideClick()}) {
          Icon(Icons.Filled.Close, contentDescription = "")
        }
      }
      Column(modifier = Modifier
        .weight(5f)
        .fillMaxWidth()) {
        Text(
          text = "Question ${currentIndex + 1} / ${quiz.questions.size}",
          textAlign = TextAlign.Center,
          style = MaterialTheme.typography.titleLarge
        )
        Text(
          text = quiz.questions[currentIndex].statement,
          textAlign = TextAlign.Justify
        )
      }
      AnswerField(modifier = Modifier.weight(2f).fillMaxWidth(), answer = quiz.questions[currentIndex].answer)
    }
  }
}

@Composable
fun AnswerField(modifier: Modifier = Modifier, answer: Answer) {
  when(answer) {
    is Answer.FillInBlank -> {
      TextField(
        modifier = modifier,
        value = "",
        onValueChange = {}
      )
    }
    is Answer.MultipleChoice -> {
      LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,

      ) {
        items(answer.options) { option ->
          Text(option)
        }
      }
    }
    is Answer.TrueFalse -> {
      Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceAround
      ) {
        Button(onClick = {}) { Text("true") }
        Button(onClick = {}) { Text("false") }
      }
    }
  }
}