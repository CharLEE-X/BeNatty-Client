package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.ColumnScope
import com.varabyte.kobweb.compose.foundation.layout.RowScope
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.size
import org.jetbrains.compose.web.css.em

@Composable
fun Spacer(
    size: CSSLengthOrPercentageNumericValue = 1.em,
) {
    Box(Modifier.size(size))
}

@Composable
fun ColumnScope.FlexSpacer(modifier: Modifier = Modifier) {
    Box(modifier.weight(1f))
}

@Composable
fun RowScope.FlexSpacer(modifier: Modifier = Modifier) {
    Box(modifier.weight(1f))
}
