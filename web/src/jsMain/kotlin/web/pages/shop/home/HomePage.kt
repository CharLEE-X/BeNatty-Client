package web.pages.shop.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.aspectRatio
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.gridAutoRows
import com.varabyte.kobweb.compose.ui.modifiers.gridColumn
import com.varabyte.kobweb.compose.ui.modifiers.gridRow
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateColumns
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.silk.components.text.SpanText
import feature.shop.home.HomeViewModel
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.css.px
import web.components.layouts.MainParams
import web.components.layouts.ShopMainLayout
import web.components.layouts.oneLayoutMaxWidth

@Composable
fun HomeContent(
    mainParams: MainParams,
    goToCatalogue: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        HomeViewModel(
            scope = scope,
            onError = mainParams.onError
        )
    }
    val state by vm.observeStates().collectAsState()

    ShopMainLayout(
        title = state.strings.title,
        mainParams = mainParams,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .maxWidth(oneLayoutMaxWidth)
                .margin(0.px)
                .padding(leftRight = 20.px, topBottom = 100.px)
        ) {
            Collage(
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun Collage(
    modifier: Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .display(DisplayStyle.Grid)
            .gridTemplateColumns { repeat(3) { size(1.fr) } }
            .gridAutoRows { minmax(200.px, 1.fr) }
            .gap(1.em)
    ) {
        CollageItem(
            title = "Build your style",
            color = Colors.Purple,
            modifier = Modifier
                .gridColumn(1, 3)
                .gridRow(1, 3)
        )
        CollageItem(
            title = "Stay cozy and Stylish",
            color = Colors.Aquamarine,
        )
        CollageItem(
            title = "Warm, comfortable and chic",
            color = Colors.Orange,
        )
        CollageItem(
            title = "Woman",
            color = Colors.AliceBlue,
            modifier = Modifier
        )
        CollageItem(
            title = "Man",
            color = Colors.DarkSeaGreen,
            modifier = Modifier
        )
        CollageItem(
            title = "Sale",
            color = Colors.LightCoral,
            modifier = Modifier
        )
    }
}

@Composable
private fun CollageItem(
    modifier: Modifier = Modifier,
    title: String,
    color: CSSColorValue,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .position(Position.Relative)
            .aspectRatio(1.0)
            .borderRadius(12.px)
            .backgroundColor(color)
    ) {
        SpanText(title)
    }
}
