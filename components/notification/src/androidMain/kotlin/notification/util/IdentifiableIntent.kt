package notification.util

import android.content.Context
import android.content.Intent

internal class IdentifiableIntent(
    private val id: String,
    packageContext: Context,
    cls: Class<*>,
) : Intent(packageContext, cls) {
    override fun filterEquals(other: Intent?): Boolean {
        if (this === other) return true
        return !(javaClass != other?.javaClass || this.id != (other as IdentifiableIntent).id)
    }
}
