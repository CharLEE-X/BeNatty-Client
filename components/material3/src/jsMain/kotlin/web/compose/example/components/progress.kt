package web.compose.example.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import kotlinx.coroutines.delay
import web.compose.extras.text.LargeTitle
import web.compose.material3.progress.CircularProgress
import web.compose.material3.progress.LinearProgress
import kotlin.time.Duration.Companion.seconds

@Composable
fun ProgressIndicatorShowcase() {
    var progress by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            progress = 0f
            delay(1.seconds)

            progress = .1f
            delay(1.seconds)

            progress = .6f
            delay(1.seconds)

            progress = 1f
            delay(1.seconds)
        }
    }

    LargeTitle("Progress Indicators")

    LinearProgress(
        value = progress,
        modifier = Modifier.fillMaxWidth(),
    )
    LinearProgress(
        value = progress,
        intermediate = true,
        fourColor = true,
        modifier = Modifier.fillMaxWidth(),
    )
    CircularProgress(
        value = progress,
    )
    CircularProgress(
        value = progress,
        intermediate = true,
        fourColor = true,
    )
}
