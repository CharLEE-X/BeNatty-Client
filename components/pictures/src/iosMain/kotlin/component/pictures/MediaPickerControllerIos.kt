package component.pictures

import co.touchlab.kermit.Logger
import component.pictures.model.Bitmap
import component.pictures.model.MediaSource
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.CoreServices.kUTTypeImage
import platform.CoreServices.kUTTypeMovie
import platform.CoreServices.kUTTypeVideo
import platform.Foundation.CFBridgingRelease
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UIViewController
import platform.UIKit.presentationController
import kotlin.coroutines.suspendCoroutine

internal class MediaPickerControllerIos(
    private val logger: Logger,
    private val getViewController: () -> UIViewController,
) : MediaPickerController {
    override suspend fun pickImage(source: MediaSource): Bitmap =
        try {
            withContext(Dispatchers.Main.immediate) {
                logger.d { "Picking image from $source." }
                // strong reference to delegate (view controller have weak ref)
                @Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
                var delegatePtr: ImagePickerDelegateToContinuation?

                @Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
                var presentationDelegate: AdaptivePresentationDelegateToContinuation?

                val media =
                    suspendCoroutine { continuation ->
                        val localDelegatePtr = ImagePickerDelegateToContinuation(continuation)
                        delegatePtr = localDelegatePtr
                        val localPresentationDelegatePtr = AdaptivePresentationDelegateToContinuation(continuation)
                        presentationDelegate = localPresentationDelegatePtr

                        val controller = UIImagePickerController()
                        controller.sourceType = source.toSourceType()
                        controller.mediaTypes = listOf(kImageType)
                        controller.delegate = localDelegatePtr
                        getViewController().presentViewController(
                            controller,
                            animated = true,
                            completion = null,
                        )
                        controller.presentationController?.delegate = localPresentationDelegatePtr
                    }
                delegatePtr = null
                presentationDelegate = null

                logger.d { "Picked image from $source." }
                media.preview
            }
        } catch (e: Exception) {
            logger.e(e) { "Failed to pick image from $source." }
            throw e
        }

    private fun MediaSource.toSourceType(): UIImagePickerControllerSourceType =
        when (this) {
            MediaSource.GALLERY -> UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary
            MediaSource.CAMERA -> UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera
        }

    @OptIn(ExperimentalForeignApi::class)
    internal companion object {
        val kImageType = CFBridgingRelease(kUTTypeImage) as String
        val kVideoType = CFBridgingRelease(kUTTypeVideo) as String
        val kMovieType = CFBridgingRelease(kUTTypeMovie) as String
    }
}
