package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.tabIndex
import com.varabyte.kobweb.silk.components.icons.mdi.MdiPsychology
import web.util.onEnterKeyDown

@Composable
fun ImproveWithButton(
    tooltipText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AppIconButton(
        onClick = { onClick() },
        modifier = modifier
            .tabIndex(0)
            .onEnterKeyDown(onClick)
    ) {
        MdiPsychology()
    }
    AppTooltip(tooltipText)
}
