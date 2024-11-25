package ch.proliferate.globule.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ch.proliferate.globule.ui.components.PolygonShape
import ch.proliferate.globule.ui.components.PolygonType
import kotlinx.coroutines.launch

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {

  val radiusWeight = remember { Animatable(0.3f) }

  val rotationAngle = remember { Animatable(0.0f) }

  val fillColorAlpha = remember { Animatable(0.5f) }

  fun springSpec(): SpringSpec<Float> {
    return spring(dampingRatio = Spring.DampingRatioHighBouncy, stiffness = Spring.StiffnessLow)
  }

  LaunchedEffect(Unit) {
    launch { radiusWeight.animateTo(targetValue = 1.0f, animationSpec = springSpec()) }

    launch { rotationAngle.animateTo(targetValue = 360.0f, animationSpec = springSpec()) }
    launch { fillColorAlpha.animateTo(targetValue = 0.1f, animationSpec = springSpec()) }
  }

  PolygonShape(
      modifier = Modifier,
      polygonType = PolygonType.CIRCLE,
      radius = 300f * radiusWeight.value,
      color = Color.Red)
  PolygonShape(
      modifier = Modifier,
      polygonType = PolygonType.CIRCLE,
      isFilled = true,
      radius = 300f * radiusWeight.value,
      color = Color.Red.copy(alpha = fillColorAlpha.value))
  PolygonShape(
      modifier = Modifier,
      polygonType = PolygonType.TRIANGLE,
      isStarred = true,
      radius = 300f * radiusWeight.value,
      color = Color.Blue,
      rotateAngle = rotationAngle.value)
  PolygonShape(
      modifier = Modifier,
      isFilled = true,
      polygonType = PolygonType.HEXAGON,
      radius = 300f * radiusWeight.value,
      color = Color.Blue.copy(alpha = fillColorAlpha.value),
      rotateAngle = rotationAngle.value)
  PolygonShape(
      modifier = Modifier,
      polygonType = PolygonType.HEXAGON,
      radius = 300f * radiusWeight.value,
      color = Color.Blue,
      rotateAngle = rotationAngle.value)
}
