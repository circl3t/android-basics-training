package ch.proliferate.globule.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.circle
import androidx.graphics.shapes.star
import androidx.graphics.shapes.toPath

enum class PolygonType {
  CIRCLE,
  TRIANGLE,
  SQUARE,
  PENTAGON,
  HEXAGON,
  HEPTAGON,
  OCTAGON
}

@Composable
fun PolygonShape(
    modifier: Modifier = Modifier,
    polygonType: PolygonType,
    isStarred: Boolean = false,
    isFilled: Boolean = false,
    radius: Float,
    rotateAngle: Float = 0f,
    color: Color,
    strokeWidth: Float = 3f,
) {

  val numVertices =
      when (polygonType) {
        PolygonType.CIRCLE -> 0
        PolygonType.TRIANGLE -> 3
        PolygonType.SQUARE -> 4
        PolygonType.PENTAGON -> 5
        PolygonType.HEXAGON -> 6
        PolygonType.HEPTAGON -> 7
        PolygonType.OCTAGON -> 8
      }

  Box(
      modifier =
          modifier.drawWithCache {
            val shape =
                if (numVertices == 0) {
                  RoundedPolygon.circle(
                      radius = radius, centerX = size.width / 2, centerY = size.height / 2)
                } else if (isStarred) {
                  RoundedPolygon.star(
                      numVerticesPerRadius = numVertices,
                      radius = radius,
                      centerX = size.width / 2,
                      centerY = size.height / 2)
                } else {
                  RoundedPolygon(
                      numVertices = numVertices,
                      radius = radius,
                      centerX = size.width / 2,
                      centerY = size.height / 2)
                }

            val shapePath = shape.toPath().asComposePath()
            onDrawBehind {
              withTransform({ rotate(rotateAngle) }) {
                drawPath(
                    shapePath,
                    color = color,
                    style = if (isFilled) Fill else Stroke(width = strokeWidth))
              }
            }
          })
}
