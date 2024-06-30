package component.pictures

import component.pictures.model.Bitmap
import component.pictures.model.Media
import component.pictures.model.MediaType
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.AVAsset
import platform.AVFoundation.AVAssetImageGenerator
import platform.AVFoundation.AVURLAsset
import platform.CoreMedia.CMTimeMake
import platform.Foundation.NSURL
import platform.UIKit.UIImage
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerEditedImage
import platform.UIKit.UIImagePickerControllerImageURL
import platform.UIKit.UIImagePickerControllerMediaType
import platform.UIKit.UIImagePickerControllerMediaURL
import platform.UIKit.UIImagePickerControllerOriginalImage
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.darwin.NSObject
import kotlin.coroutines.Continuation
import kotlin.random.Random

@OptIn(ExperimentalForeignApi::class)
internal class ImagePickerDelegateToContinuation(
    private val continuation: Continuation<Media>,
) : NSObject(), UINavigationControllerDelegateProtocol, UIImagePickerControllerDelegateProtocol {
    override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
        picker.dismissModalViewControllerAnimated(true)
        continuation.resumeWith(Result.failure(Exception("Image picker cancelled")))
    }

    override fun imagePickerController(
        picker: UIImagePickerController,
        didFinishPickingMediaWithInfo: Map<Any?, *>,
    ) {
        val image =
            didFinishPickingMediaWithInfo[UIImagePickerControllerEditedImage] as? UIImage
                ?: didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage] as? UIImage
        val mediaUrl =
            didFinishPickingMediaWithInfo[UIImagePickerControllerMediaURL] as? NSURL
                ?: didFinishPickingMediaWithInfo[UIImagePickerControllerImageURL] as? NSURL
        val mediaType = didFinishPickingMediaWithInfo[UIImagePickerControllerMediaType] as? String

        picker.dismissViewControllerAnimated(true) {}

        val type =
            when (mediaType) {
                MediaPickerControllerIos.kMovieType,
                MediaPickerControllerIos.kVideoType,
                -> MediaType.VIDEO

                MediaPickerControllerIos.kImageType -> MediaType.PHOTO

                else -> {
                    continuation.resumeWith(Result.failure(IllegalArgumentException("unknown type $mediaType")))
                    return
                }
            }

        if (type == MediaType.VIDEO) {
            if (mediaUrl == null) {
                continuation.resumeWith(
                    Result.failure(Exception("No access to the file. Info: $didFinishPickingMediaWithInfo")),
                ) // TODO write some info
                return
            }

            val asset = AVURLAsset(uRL = mediaUrl, options = null)
            val media =
                Media(
                    name = mediaUrl.relativeString,
                    path = mediaUrl.path.orEmpty(),
                    preview = Bitmap(fetchThumbnail(videoAsset = asset)),
                    type = type,
                )
            continuation.resumeWith(Result.success(media))
        } else {
            if (image == null) {
                continuation.resumeWith(
                    Result.failure(Exception("No access to the file. Info: $didFinishPickingMediaWithInfo")),
                ) // TODO write some info
                return
            }

            val media =
                Media(
                    name = mediaUrl?.relativeString ?: Random.nextLong().toString(),
                    path = mediaUrl?.path.orEmpty(),
                    preview = Bitmap(image),
                    type = type,
                )
            continuation.resumeWith(Result.success(media))
        }
    }

    private fun fetchThumbnail(videoAsset: AVAsset): UIImage {
        val imageGenerator =
            AVAssetImageGenerator(
                asset = videoAsset,
            )
        imageGenerator.appliesPreferredTrackTransform = true
        val cgImage =
            imageGenerator.copyCGImageAtTime(
                requestedTime =
                CMTimeMake(
                    value = 0,
                    timescale = 1,
                ),
                actualTime = null,
                error = null,
            )
        return UIImage(cGImage = cgImage)
    }
}
