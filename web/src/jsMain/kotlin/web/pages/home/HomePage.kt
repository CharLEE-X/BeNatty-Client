package web.pages.home

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.layout.Surface
import com.varabyte.kobweb.silk.components.text.SpanText
import feature.router.RouterViewModel
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.value
import theme.MaterialTheme
import web.components.widgets.AppFilledCard
import web.compose.example.components.ChipShowcase

@Suppress("UNUSED_PARAMETER")
@Composable
fun HomeContent(
    router: RouterViewModel,
    onError: suspend (String) -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(70.percent)
                .borderRadius(1.em)
                .backgroundColor(MaterialTheme.colors.mdSysColorSurfaceContainer.value())
        ) {
            AppFilledCard(
                modifier = Modifier
                    .fillMaxWidth(70.percent)
                    .margin(1.5.em)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .gap(1.em)
                ) {
                    Image(
                        src = "https://lh3.googleusercontent.com/_mruGfyg4GvStbvd4jmO36feG6f_f9a1uleCaw5V0EurPd9iyTFFal6ypXEc5v6vXF_fFl-6AQDj9at442qvFBGQEoFh2OrMFIEL1Mnxaaea9kfqAwc=w1400-v0",
                        description = "NataliaShop logo",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.px)
                            .borderRadius(1.em)
                    )
                    SpanText("Home Page")
                    ChipShowcase()
                }
            }
        }
    }
}
