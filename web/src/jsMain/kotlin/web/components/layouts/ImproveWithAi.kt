package web.components.layouts

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.RowScope
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.silk.components.icons.mdi.MdiPsychology
import org.jetbrains.compose.web.css.em
import web.components.widgets.AppTooltip
import web.compose.material3.component.FilledIconButton

@Composable
fun ImproveWithAiRow(
    tooltipText: String,
    onImproveClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .gap(1.em)
    ) {
        content()
        FilledIconButton(
            onClick = { onImproveClick() },
        ) {
            MdiPsychology()
        }
        AppTooltip(tooltipText)
    }
}
