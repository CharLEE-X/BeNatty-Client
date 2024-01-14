package feature.updateProfile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun UpdateProfileContent(
    onError: suspend (String) -> Unit,
    onBackClicked: () -> Unit,
    isModalShowing: (isShowingModal: Boolean) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            text = "update profile",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp),
            style = MaterialTheme.typography.h6,
        )
    }
}
