package theme

import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.CSSNumeric
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSStyleVariable
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.CSSUnitValueTyped
import org.jetbrains.compose.web.css.StylePropertyString
import org.jetbrains.compose.web.css.StyleScope
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.fontFamily
import org.jetbrains.compose.web.css.fontSize
import org.jetbrains.compose.web.css.fontWeight
import org.jetbrains.compose.web.css.letterSpacing
import org.jetbrains.compose.web.css.lineHeight
import org.jetbrains.compose.web.css.value

val defaultFontScheme = SysFontScheme(
    displayLarge = FontConfig("Roboto", 57.sp, 64.sp, (-0.25 / 57).cssRem, "400"),
    displayMedium = FontConfig("Roboto", 45.sp, 52.sp, 0.cssRem, "400"),
    displaySmall = FontConfig("Roboto", 36.sp, 44.sp, 0.cssRem, "400"),
    headlineLarge = FontConfig("Roboto", 32.sp, 40.sp, 0.cssRem, "400"),
    headlineMedium = FontConfig("Roboto", 28.sp, 36.sp, 0.cssRem, "400"),
    headlineSmall = FontConfig("Roboto", 24.sp, 32.sp, 0.cssRem, "400"),
    titleLarge = FontConfig("Roboto", 22.sp, 28.sp, 0.cssRem, "400"),
    titleMedium = FontConfig("Roboto", 16.sp, 24.sp, (0.15 / 16).cssRem, "500"),
    titleSmall = FontConfig("Roboto", 14.sp, 20.sp, (0.1 / 14).cssRem, "500"),
    bodyLarge = FontConfig("Roboto", 16.sp, 24.sp, (0.5 / 16).cssRem, "400"),
    bodyMedium = FontConfig("Roboto", 14.sp, 20.sp, (0.25 / 14).cssRem, "400"),
    bodySmall = FontConfig("Roboto", 12.sp, 16.sp, (0.4 / 12).cssRem, "400"),
    labelLarge = FontConfig("Roboto", 14.sp, 20.sp, (0.1 / 14).cssRem, "500"),
    labelMedium = FontConfig("Roboto", 12.sp, 16.sp, (0.5 / 12).cssRem, "500"),
    labelSmall = FontConfig("Roboto", 11.sp, 16.sp, (0.5 / 11).cssRem, "500"),
)

open class TypeScaleTokens(
    val tokenBase: String
) {
    open inner class Role(
        tokenName: String
    ) {
        val font = CSSStyleVariable<StylePropertyString>("$tokenBase-$tokenName-font")
        val size = CSSStyleVariable<CSSNumeric>("$tokenBase-$tokenName-size")
        val lineHeight = CSSStyleVariable<CSSNumeric>("$tokenBase-$tokenName-line-height")
        val letterSpacing = CSSStyleVariable<CSSNumeric>("$tokenBase-$tokenName-letter-spacing")
        val weight = CSSStyleVariable<StylePropertyString>("$tokenBase-$tokenName-weight")
    }

    companion object {
        fun StyleScope.applyStyle(role: Role) {
            fontFamily(role.font.value().toString())
            fontSize(role.size.value())
            lineHeight(role.lineHeight.value())
            letterSpacing(role.letterSpacing.value())
            fontWeight(role.weight.value().toString())
        }
    }
}

object MdRefTypeFaceTokens {
    val mdRefTypeFaceBrand = CSSStyleVariable<CSSColorValue>("md-ref-typeface-brand")
}

data class FontConfig(
    val font: String,
    val size: CSSNumeric,
    val lineHeight: CSSNumeric,
    val letterSpacing: CSSNumeric,
    val weight: String
)

val Number.sp
    get(): CSSSizeValue<CSSUnit.rem> = CSSUnitValueTyped(this.toFloat() * 0.0625f, CSSUnit.rem)


data class SysFontScheme(
    val displayLarge: FontConfig,
    val displayMedium: FontConfig,
    val displaySmall: FontConfig,
    val headlineLarge: FontConfig,
    val headlineMedium: FontConfig,
    val headlineSmall: FontConfig,
    val titleLarge: FontConfig,
    val titleMedium: FontConfig,
    val titleSmall: FontConfig,
    val bodyLarge: FontConfig,
    val bodyMedium: FontConfig,
    val bodySmall: FontConfig,
    val labelLarge: FontConfig,
    val labelMedium: FontConfig,
    val labelSmall: FontConfig,
) {
    fun StyleScope.configureTokenValues(role: TypeScaleTokens.Role, fontConfig: FontConfig) {
        role.font(fontConfig.font)
        role.size(fontConfig.size)
        role.lineHeight(fontConfig.lineHeight)
        role.letterSpacing(fontConfig.letterSpacing)
        role.weight(fontConfig.weight)
    }

    fun asStyleSheet(): StyleSheet = StyleSheet().apply {
        universal style {
            configureTokenValues(MaterialTheme.typography.displayLarge, displayLarge)
            configureTokenValues(MaterialTheme.typography.displayMedium, displayMedium)
            configureTokenValues(MaterialTheme.typography.displaySmall, displaySmall)

            configureTokenValues(MaterialTheme.typography.headlineLarge, headlineLarge)
            configureTokenValues(MaterialTheme.typography.headlineMedium, headlineMedium)
            configureTokenValues(MaterialTheme.typography.headlineSmall, headlineSmall)

            configureTokenValues(MaterialTheme.typography.titleLarge, titleLarge)
            configureTokenValues(MaterialTheme.typography.titleMedium, titleMedium)
            configureTokenValues(MaterialTheme.typography.titleSmall, titleSmall)

            configureTokenValues(MaterialTheme.typography.bodyLarge, bodyLarge)
            configureTokenValues(MaterialTheme.typography.bodyMedium, bodyMedium)
            configureTokenValues(MaterialTheme.typography.bodySmall, bodySmall)

            configureTokenValues(MaterialTheme.typography.labelLarge, labelLarge)
            configureTokenValues(MaterialTheme.typography.labelMedium, labelMedium)
            configureTokenValues(MaterialTheme.typography.labelSmall, labelSmall)
        }
    }
}
