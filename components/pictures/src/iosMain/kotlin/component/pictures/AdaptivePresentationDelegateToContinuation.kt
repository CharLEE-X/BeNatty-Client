package component.pictures

import platform.UIKit.UIAdaptivePresentationControllerDelegateProtocol
import platform.UIKit.UIPresentationController
import platform.darwin.NSObject
import kotlin.coroutines.Continuation

internal class AdaptivePresentationDelegateToContinuation(
    private val continuation: Continuation<*>,
) : NSObject(), UIAdaptivePresentationControllerDelegateProtocol {
    override fun presentationControllerDidDismiss(presentationController: UIPresentationController) {
        continuation.resumeWith(Result.failure(Exception("Image picker cancelled")))
    }
}
