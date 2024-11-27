package ch.proliferate.globule.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ch.proliferate.globule.R

val Ubuntu = FontFamily(Font(R.font.ubuntu_regular), Font(R.font.ubuntu_bold, FontWeight.Bold))

fun ubuntuTextStyle(fontSize: Int) = TextStyle(
    fontFamily = Ubuntu,
    fontSize = fontSize.sp
)

val Typography =
    Typography(
      displayLarge = ubuntuTextStyle(57),
      displayMedium = ubuntuTextStyle(45),
      displaySmall = ubuntuTextStyle(36),
      headlineLarge = ubuntuTextStyle(32),
      headlineMedium = ubuntuTextStyle(28),
      headlineSmall = ubuntuTextStyle(24),
      titleLarge = ubuntuTextStyle(22),
      titleMedium = ubuntuTextStyle(16),
      titleSmall = ubuntuTextStyle(14),
      bodyLarge = ubuntuTextStyle(16),
      bodyMedium = ubuntuTextStyle(14),
      bodySmall = ubuntuTextStyle(12),
      labelLarge = ubuntuTextStyle(14),
      labelMedium = ubuntuTextStyle(12),
      labelSmall = ubuntuTextStyle(11)
    )
