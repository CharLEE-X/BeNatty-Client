package components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
internal fun Thumbnail(
    modifier: Modifier = Modifier,
    url: String,
) {
    Surface(
        shape = CircleShape,
        elevation = 8.dp,
        modifier = modifier,
    ) {
        KamelImage(
            resource = asyncPainterResource(url),
            contentDescription = "Thumbnail",
            contentScale = ContentScale.Crop,
            onLoading = { progress -> CircularProgressIndicator(progress) },
            animationSpec = tween(),
        )
    }
}
