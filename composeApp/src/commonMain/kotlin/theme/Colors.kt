package theme

import androidx.compose.ui.graphics.Color

object Colors {

    // Bumble
    val lightYellow = Color(0xfffef7dc)
    val darkYellow = Color(0xfffcc53c)
    val yellow = Color(0xfffac13c)
    val veryLightGray = Color(0xfff6f6f6)
    val blue = Color(0xff229afc)
    val badge = Color(0xffed4245)
    val darkGreyTransparent = Color(0x8c444444)

    // Bumble looks like it has no secondary color so i'll give it green for now
    val green = Color(0xff00b338)
    val darkGreen = Color(0xff3db674)

    // All colors below will need removing
    val title = Color(0xff3498db)
    val title2 = Color(0xff00b338)

    object Icon {
        val active = Color(0xff79797C)
        val inActive = Color(0xffbcbcc1)
    }

    object Surface {
        val dark = Color(0xff110d00)
        val light = Color(0xfff2f2f7)
    }

    object Background {
        val dark = Color(0xff22829)
        val light = Color(0xffFFFFFF)
    }
}
