package components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import feature.debug.model.LatLng

@Composable
expect fun Map(
    modifier: Modifier,
    onMarkerClick: (LatLng) -> Unit,
    location: LatLng,
    onPositionChange: (LatLng) -> Unit,
    marketTitle: String?,
    isInteractionEnabled: Boolean,
)
