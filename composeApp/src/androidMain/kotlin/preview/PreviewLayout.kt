package preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import theme.Colors

@Composable
internal fun PreviewLayout(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(if (darkTheme) darkColors() else lightColors()) {
        Surface(
            color = if (darkTheme) Colors.Surface.dark else Colors.Surface.light,
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                content()
            }
        }
    }
}
