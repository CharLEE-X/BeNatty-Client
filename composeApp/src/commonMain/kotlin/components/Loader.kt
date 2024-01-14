package components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun Loader() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp),
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colors.primary,
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}

