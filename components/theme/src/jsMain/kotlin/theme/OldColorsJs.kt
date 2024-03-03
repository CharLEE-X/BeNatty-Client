package theme

import org.jetbrains.compose.web.css.Color


object OldColorsJs {
    private val oldColors = OldColors

    val primaryLighter = oldColors.primaryLighter.rgba()
    val primary = oldColors.primary.rgba()
    val primaryDarker = oldColors.primaryDarker.rgba()

    val secondaryLighter = oldColors.secondaryLighter.rgba()
    val secondary = oldColors.secondary.rgba()
    val secondaryDarker = oldColors.secondaryDarker.rgba()

    val lightGray = oldColors.lightGray.rgba()
    val lightGrayDarker = Color("#f2f2f2")
    val darkGray = oldColors.darkGray.rgba()
    val white = oldColors.white.rgba()
    val green = oldColors.green.rgba()
    val red = oldColors.red.rgba()
    val purple = oldColors.purple.rgba()

    val neutral50 = oldColors.neutral50.rgba()
    val neutral100 = oldColors.neutral100.rgba()
    val neutral200 = oldColors.neutral200.rgba()
    val neutral300 = oldColors.neutral300.rgba()
    val neutral400 = oldColors.neutral400.rgba()
    val neutral500 = oldColors.neutral500.rgba()
    val neutral600 = oldColors.neutral600.rgba()
    val neutral700 = oldColors.neutral700.rgba()
    val neutral800 = oldColors.neutral800.rgba()
    val neutral900 = oldColors.neutral900.rgba()
}
