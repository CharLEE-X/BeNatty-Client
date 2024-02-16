package web.components.layouts

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.silk.components.icons.mdi.MdiPsychology
import web.components.widgets.AppFilledIconButton
import web.components.widgets.AppTooltip

@Composable
fun ImproveWithButton(
    tooltipText: String,
    onImproveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AppFilledIconButton(
        onClick = { onImproveClick() },
        modifier = modifier
    ) {
        MdiPsychology()
    }
    AppTooltip(tooltipText)
}
