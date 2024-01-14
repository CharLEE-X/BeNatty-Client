package theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp

object Dimensions {
    const val TOP_BAR_HEIGHT = 64
    const val BOTTOM_BAR_HEIGHT = 64
}

object Padding {
    val quarter = 4.dp
    val half = 8.dp
    val default = 16.dp
    val double = 32.dp
}

// This value is set only in iOS
var safePaddingValues by mutableStateOf(PaddingValues())
