package component.pictures

import co.touchlab.kermit.Logger.Companion.withTag
import org.koin.core.qualifier.named
import org.koin.dsl.module

@Suppress("unused")
actual val picturesModule =
    module {
        single<MediaPickerController> {
            MediaPickerControllerAndroid(
                logger = withTag(MediaPickerController::class.simpleName!!),
                activity = inject(),
            )
        }
        single<PicturesService> {
            PicturesServiceAndroid(
                logger = withTag(PicturesService::class.simpleName!!),
                context = get(),
                activity = inject(),
                mediaPickerController = get(),
                cameraPermissionResultLauncher = get(named(CAMERA_PERMISSION_LAUNCHER)),
            )
        }
        single(named(ON_CAMERA_PERMISSION_GRANTED)) {
            // here we need to handle the result of the camera permission request
            // basically open up the camera

            { println("Camera permission granted. This method is injected from Koin") }
        }
    }

const val ON_CAMERA_PERMISSION_GRANTED = "ON_CAM_PERMISSION_GRANTED"
const val CAMERA_PERMISSION_LAUNCHER = "CAMERA_PERMISSION_LAUNCHER"
