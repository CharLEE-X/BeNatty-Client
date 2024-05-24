package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import org.jetbrains.compose.web.css.CSSColorValue

@Composable
fun AppElevatedCard(
    modifier: Modifier = Modifier,
    elevation: Int? = null,
    shadowColor: CSSColorValue? = null,
    color: CSSColorValue? = null,
    content: @Composable () -> Unit
) {
//    ElevatedCard(
//        modifier = modifier,
//        elevation = elevation,
//        containerShape = 0.px,
//        shadowColor = shadowColor,
//        color = color,
//        content = content
//    )
}

@Composable
fun AppFilledCard(
    modifier: Modifier = Modifier,
    elevation: Int? = null,
    shadowColor: CSSColorValue? = null,
    content: @Composable () -> Unit
) {
//    FilledCard(
//        modifier = modifier,
//        elevation = elevation,
//        containerShape = 0.px,
//        shadowColor = shadowColor,
//        content = content
//    )
}
