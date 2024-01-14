package component.pictures

import android.app.FragmentManager
import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import co.touchlab.kermit.Logger
import component.pictures.model.Bitmap
import component.pictures.model.MediaSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.suspendCoroutine

internal class MediaPickerControllerAndroid(
    private val logger: Logger,
    private val activity: Lazy<ComponentActivity>,
) : MediaPickerController {
    var fragmentManager: FragmentManager? = null

    override suspend fun pickImage(source: MediaSource): Bitmap? {
        bind()

        if (fragmentManager == null) {
            logger.e { "Fragment manager is null" }
            return null
        }


        val imagePickerFragment: ImagePickerFragment = withContext(Dispatchers.Main) {
            ImagePickerFragment.newInstance(1024, 1024).also {
                fragmentManager!!
                    .beginTransaction()
                    .add(it, this::class.simpleName)
                    .commitNow()
            }
        }

        val bitmap = suspendCoroutine { continuation ->
            val action: (Result<android.graphics.Bitmap>) -> Unit = { continuation.resumeWith(it) }
            when (source) {
                MediaSource.GALLERY -> imagePickerFragment.pickGalleryImage(action)
                MediaSource.CAMERA -> imagePickerFragment.pickCameraImage(action)
            }
        }

        return Bitmap(bitmap)
    }

    private fun bind() {
        this.fragmentManager = activity.value.fragmentManager

        val observer = object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroyed(source: LifecycleOwner) {
                this@MediaPickerControllerAndroid.fragmentManager = null
                source.lifecycle.removeObserver(this)
            }
        }
        MainScope().launch {
            activity.value.lifecycle.addObserver(observer)
        }
    }
}
