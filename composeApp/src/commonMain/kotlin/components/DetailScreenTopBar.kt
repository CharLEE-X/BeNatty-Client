package components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun SimpleTopBar(
    modifier: Modifier = Modifier,
    headerText: String,
    onBackClicked: () -> Unit,
) {
    Column {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.background)
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = "Back",
                tint = MaterialTheme.colors.onSurface,
                modifier = Modifier
                    .size(32.dp)
                    .clickable(onClick = onBackClicked),
            )
            TopBarCenterAction(
                modifier = modifier.align(Alignment.Center),
                headerText = headerText
            )
        }
        Spacer(
            modifier = Modifier.background(MaterialTheme.colors.surface)
                .height(1.dp).fillMaxWidth()
        )
    }
}

@Composable
internal fun TopBarCenterAction(
    headerText: String,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = modifier,
    ) {
        BarTitleStyledHeader(headerText)
    }
}
