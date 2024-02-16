package preview

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import components.BottomBar
import feature.router.Screen

@Preview
@Composable
private fun BottomBarPreview() {
    MaterialTheme(lightColors()) {
        BottomBar(
            routes = listOf(
                Screen.Home,
            ),
            currentRoute = Screen.Home,
            onDestinationClick = {},
            show = true,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
