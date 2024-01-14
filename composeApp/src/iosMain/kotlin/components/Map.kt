package components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import feature.debug.model.LatLng
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.MapKit.MKAnnotationView
import platform.MapKit.MKCoordinateRegionMakeWithDistance
import platform.MapKit.MKMapView
import platform.MapKit.MKMapViewDelegateProtocol
import platform.MapKit.MKPointAnnotation
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun Map(
    modifier: Modifier,
    onMarkerClick: (LatLng) -> Unit,
    location: LatLng,
    onPositionChange: (LatLng) -> Unit,
    marketTitle: String?,
    isInteractionEnabled: Boolean,
) {

    var clLocation by remember {
        mutableStateOf(
            CLLocationCoordinate2DMake(
                location.latitude,
                location.longitude,
            )
        )
    }

    LaunchedEffect(clLocation) {
        clLocation = CLLocationCoordinate2DMake(
            location.latitude,
            location.longitude
        )
    }

    val annotation = remember {
        MKPointAnnotation(
            clLocation,
            title = null,
            subtitle = null
        )
    }

    val isMoved = remember { mutableStateOf(true) }

    val mkMapView = remember {
        MKMapView().apply {
            addAnnotation(annotation)
            setUserInteractionEnabled(isInteractionEnabled)
            showsUserLocation = true
            zoomEnabled = isInteractionEnabled
            pitchEnabled = isInteractionEnabled
            showsCompass = isInteractionEnabled
            showsScale = isInteractionEnabled
        }
    }

    val delegate = remember {
        MKDelegate(
            onMove = { onMove ->
                isMoved.value = onMove
                mkMapView.centerCoordinate.useContents {
                    onPositionChange(LatLng(latitude, longitude))
                }
            },
            onAnnotationClicked = { annotationView ->
                if (annotationView != null) {
                    val deviceLocation = annotationView.annotation?.coordinate?.useContents {
                        LatLng(latitude, longitude)
                    }
                    deviceLocation?.let {
                        onMarkerClick(it)
                    }
                } else {
                    onMarkerClick(location)
                }
            })
    }

    LaunchedEffect(isMoved) {
        if (isMoved.value) {
            mkMapView.centerCoordinate.useContents {
                onPositionChange(LatLng(latitude, longitude))
            }
        }
    }

    UIKitView(
        modifier = modifier.fillMaxSize(),
        interactive = true,
        factory = {
            mkMapView
        }, update = { view ->
            mkMapView.setRegion(
                MKCoordinateRegionMakeWithDistance(
                    centerCoordinate = clLocation,
                    10_000.0, 10_000.0
                ),
                animated = true
            )
            mkMapView.setDelegate(delegate)

            val pin = MKPointAnnotation()
            pin.setCoordinate(clLocation)
            marketTitle?.let { pin.setTitle(it) }

            var annotationView = mkMapView.dequeueReusableAnnotationViewWithIdentifier("custom")
            if (annotationView == null) {
                annotationView = MKAnnotationView(pin, "custom")
            }
            annotationView.canShowCallout = true

            mkMapView.addAnnotations(listOf(annotationView))
        }
    )
}

@Suppress("CONFLICTING_OVERLOADS", "PARAMETER_NAME_CHANGED_ON_OVERRIDE")
private class MKDelegate(
    private val onAnnotationClicked: (MKAnnotationView?) -> Unit,
    private val onMove: (Boolean) -> Unit,
) : NSObject(), MKMapViewDelegateProtocol {

    override fun mapView(mapView: MKMapView, regionDidChangeAnimated: Boolean) {
        onMove(regionDidChangeAnimated)
    }

    override fun mapView(mapView: MKMapView, didSelectAnnotationView: MKAnnotationView) {
        val annotationView = didSelectAnnotationView.annotation as MKAnnotationView
        onAnnotationClicked(annotationView)
    }

    override fun mapView(mapView: MKMapView, didDeselectAnnotationView: MKAnnotationView) {
        onAnnotationClicked(null)
    }
}
