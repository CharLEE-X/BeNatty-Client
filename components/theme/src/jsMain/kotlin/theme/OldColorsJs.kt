package theme

import org.jetbrains.compose.web.css.Color


object OldColorsJs {
    private val oldColors = OldColors

    val primaryLighter = oldColors.primaryLighter.rgb()
    val primary = oldColors.primary.rgb()
    val primaryDarker = oldColors.primaryDarker.rgb()

    val secondaryLighter = oldColors.secondaryLighter.rgb()
    val secondary = oldColors.secondary.rgb()
    val secondaryDarker = oldColors.secondaryDarker.rgb()

    val lightGray = oldColors.lightGray.rgb()
    val lightGrayDarker = Color("#f2f2f2")
    val darkGray = oldColors.darkGray.rgb()
    val white = oldColors.white.rgb()
    val green = oldColors.green.rgb()
    val red = oldColors.red.rgb()
    val purple = oldColors.purple.rgb()

    val neutral50 = oldColors.neutral50.rgb()
    val neutral100 = oldColors.neutral100.rgb()
    val neutral200 = oldColors.neutral200.rgb()
    val neutral300 = oldColors.neutral300.rgb()
    val neutral400 = oldColors.neutral400.rgb()
    val neutral500 = oldColors.neutral500.rgb()
    val neutral600 = oldColors.neutral600.rgb()
    val neutral700 = oldColors.neutral700.rgb()
    val neutral800 = oldColors.neutral800.rgb()
    val neutral900 = oldColors.neutral900.rgb()
}
