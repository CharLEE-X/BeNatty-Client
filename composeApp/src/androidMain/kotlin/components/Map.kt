package components

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.charleex.benatty.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.CameraMoveStartedReason
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import feature.debug.model.LatLng as LatLngDebug

@Composable
actual fun Map(
    modifier: Modifier,
    onMarkerClick: (LatLngDebug) -> Unit,
    location: LatLngDebug,
    onPositionChange: (LatLngDebug) -> Unit,
    marketTitle: String?,
    isInteractionEnabled: Boolean,
) {
    val coroutineScope = rememberCoroutineScope()
    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(location) {
        cameraPositionState.animate(
            update = CameraUpdateFactory.newCameraPosition(
                CameraPosition(
                    LatLng(location.latitude, location.longitude),
                    12f,
                    0f,
                    0f,
                ),
            ),
        )
    }

    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            if (cameraPositionState.cameraMoveStartedReason == CameraMoveStartedReason.GESTURE) {
                cameraPositionState.position.target.let {
                    onPositionChange(
                        LatLngDebug(it.latitude, it.longitude),
                    )
                }
            }
        }
    }


    GoogleMapsComponent(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapClick = {
            onMarkerClick(LatLngDebug(it.latitude, it.longitude))
        },
        isInteractionEnabled = isInteractionEnabled,
    ) {

        CurrentLocationMarker(
            position = LatLng(location.latitude, location.longitude),
            iconRes = R.drawable.ic_maps_marker_user,
        ) {
            coroutineScope.launch {
                cameraPositionState.animate(
                    update = CameraUpdateFactory.newCameraPosition(
                        CameraPosition(it.position, 12f, 0f, 0f),
                    ),
                    durationMs = 400,
                )
            }
        }

        MapsMarker(
            position = LatLng(location.latitude, location.longitude),
            title = marketTitle,
            iconRes = R.drawable.ic_maps_marker,
        ) {
            onMarkerClick(
                LatLngDebug(it.position.latitude, it.position.longitude),
            )
        }
    }
}

@Composable
fun MapsMarker(
    position: LatLng,
    iconRes: Int,
    title: String?,
    onClick: (Marker) -> Unit,
) {
    val context = LocalContext.current
    val markerState = MarkerState(position = position)
    val icon = bitmapDescriptor(context, iconRes)

    MarkerInfoWindow(
        state = markerState,
        anchor = Offset(0.5f, 0.5f),
        icon = icon,
        title = title,
        snippet = title,
        tag = title,
        onClick = {
            it.showInfoWindow()
            onClick(it)
            true
        },

        ) {
        val color = MaterialTheme.colors.primary

        Column(
            modifier = Modifier.offset(y = (20).dp),
        ) {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .background(
                        color = color,
                        shape = RoundedCornerShape(4.dp),
                    )
                    .padding(horizontal = 8.dp, vertical = 5.dp),
            ) {
                Text(
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    text = it.title.toString(),
                )
            }
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(30.dp)
                    .offset(y = (-16).dp),
                imageVector = Icons.Default.ArrowDropDown,
                tint = color,
                contentDescription = null,
            )
        }
    }
}

@Composable
fun CurrentLocationMarker(
    position: LatLng,
    iconRes: Int,
    onClick: (Marker) -> Unit,
) {
    val context = LocalContext.current
    val markerState = MarkerState(position = position)
    val icon = bitmapDescriptor(context, iconRes)

    MarkerInfoWindow(
        state = markerState,
        anchor = Offset(0.5f, 0.5f),
        icon = icon,
        onClick = {
            onClick(it)
            true
        },
    )
}

fun bitmapDescriptor(
    context: Context,
    vectorResId: Int,
): BitmapDescriptor? {

    // retrieve the actual drawable
    val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    val bm = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888,
    )

    // draw it onto the bitmap
    val canvas = android.graphics.Canvas(bm)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bm)
}

@Composable
fun GoogleMapsComponent(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState,
    onMapClick: (LatLng) -> Unit,
    isInteractionEnabled: Boolean,
    content: (@Composable @GoogleMapComposable () -> Unit)?,
) {
    val context = LocalContext.current
    val mapProperties by remember {
        mutableStateOf(
            MapProperties(
                maxZoomPreference = 18f,
                minZoomPreference = 5f,
                isMyLocationEnabled = true,
                mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.google_map_style),
            ),
        )
    }
    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                mapToolbarEnabled = isInteractionEnabled,
                zoomControlsEnabled = isInteractionEnabled,
                myLocationButtonEnabled = isInteractionEnabled,
                zoomGesturesEnabled = isInteractionEnabled,
                scrollGesturesEnabled = isInteractionEnabled,
            ),
        )
    }

    GoogleMap(
        modifier = modifier,
        properties = mapProperties,
        uiSettings = mapUiSettings,
        cameraPositionState = cameraPositionState,
        onMapClick = onMapClick,
        content = content,
    )
}
