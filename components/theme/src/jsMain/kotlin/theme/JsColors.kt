import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.rgb

fun androidx.compose.ui.graphics.Color.toJsColor(): JsColor {
    return JsColor(
        hex = value.toString(),
        rgb = rgb(r = red, g = green, b = blue),
    )
}

data class JsColor(
    val hex: String,
    val rgb: CSSColorValue,
)
