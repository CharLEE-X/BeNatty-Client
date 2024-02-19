package web.pages.shop.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.aspectRatio
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.boxShadow
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.gridAutoRows
import com.varabyte.kobweb.compose.ui.modifiers.gridColumn
import com.varabyte.kobweb.compose.ui.modifiers.gridRow
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateColumns
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.onMouseEnter
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.scale
import com.varabyte.kobweb.compose.ui.modifiers.textShadow
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.text.SpanText
import feature.shop.home.HomeContract
import feature.shop.home.HomeViewModel
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import web.components.layouts.MainParams
import web.components.layouts.ShopMainLayout
import web.components.layouts.oneLayoutMaxWidth
import web.components.widgets.AppElevatedButton

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
                vm = vm,
                state = state
            )
        }
    }
}

@Composable
private fun Collage(
    modifier: Modifier,
    vm: HomeViewModel,
    state: HomeContract.State,
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
            description = "Discover the latest trends",
//            url = "",
            url = "https://media.istockphoto.com/id/1339264709/photo/flat-lay-with-womans-clothes-and-accessories.jpg?s=1024x1024&w=is&k=20&c=jEVAHmR8cL6tB7FTN3cNM1WnGb5fb9sd2f69Lbu3TAU=",
            buttonText = "Shop now",
            onClick = {},
            modifier = Modifier
                .gridColumn(1, 3)
                .gridRow(1, 3)
        )
        CollageItem(
            title = "Stay cozy and Stylish",
            description = "Grab your hoodies today",
            url = "https://media.istockphoto.com/id/1339264709/photo/flat-lay-with-womans-clothes-and-accessories.jpg?s=2048x2048&w=is&k=20&c=NKhylkVx_RfvuB5TzfVukUvY1Moc6TnF7JgnVoEi944=",
            onClick = {},
        )
        CollageItem(
            title = "Warm, comfortable and chic",
            description = "Tell more about your product, collection...",
            url = "https://media.istockphoto.com/id/1838033271/photo/stylish-young-smiling-hipster-woman-with-color-hair-wearing-trendy-peach-color-coat-and-hat.jpg?s=2048x2048&w=is&k=20&c=jqsLjiTqefUV1xHGck_849vXNsJUWT9z_wBB9IGG7Xg=",
            onClick = {},
        )
        CollageItem(
            title = "Woman",
            description = "Top, Pants, Dress, Shoes, Bags, Accessories",
            url = "https://media.istockphoto.com/id/1947951512/photo/young-man-enjoying-carnival-at-home.jpg?s=2048x2048&w=is&k=20&c=xVkwAcW6_37iq6tawognAUrfCee2dhB0giNyYzoxUeA=",
            onClick = {},
        )
        CollageItem(
            title = "Man",
            description = "Top, Pants, Hoodie, Tracksuit.",
            url = "https://media.istockphoto.com/id/1455095734/photo/portrait-fashion-or-stylish-young-gen-z-woman-stand-in-a-warehouse-with-green-clothing-trendy.jpg?s=1024x1024&w=is&k=20&c=6_wNgjOYO75ZJY_zAWADgc-LingZk-GTtdl0ePugyjs=",
            onClick = {},
        )
        CollageItem(
            title = "Sale",
            description = "New markdowns: up to 50% off",
            url = "https://media.istockphoto.com/id/877121628/photo/autumn-female-clothing-and-accessories-on-pastel-background.jpg?s=2048x2048&w=is&k=20&c=FAmYXO9Z-H5DzUPHP7675S136jgCSOuJClN5Dvuleu0=",
            onClick = {},
        )
    }
}

@Composable
private fun CollageItem(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    url: String,
    buttonText: String? = null,
    onClick: () -> Unit,
    backgroundColor: CSSColorValue = Colors.DarkSeaGreen,
    contentColor: CSSColorValue = Colors.White,
    shadowColor: Color.Rgb = Colors.Black,
    borderRadius: CSSLengthOrPercentageNumericValue = 12.px,
) {
    var hovered by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .position(Position.Relative)
            .aspectRatio(1.0)
            .borderRadius(borderRadius)
            .backgroundColor(backgroundColor)
            .onMouseEnter { hovered = true }
            .onMouseLeave { hovered = false }
            .cursor(Cursor.Pointer)
            .overflow(Overflow.Hidden)
            .scale(if (hovered) 1.01 else 1.0)
            .transition(CSSTransition("scale", 0.3.s, TransitionTimingFunction.Ease))
    ) {
        Image(
            src = url,
            alt = title,
            modifier = Modifier
                .fillMaxSize()
                .borderRadius(borderRadius)
                .objectFit(ObjectFit.Cover)
                .thenIf(hovered) { Modifier.scale(1.04) }
                .transition(CSSTransition("scale", 0.3.s, TransitionTimingFunction.Ease))
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .boxShadow(
                    offsetX = 0.px,
                    offsetY = 0.px,
                    blurRadius = 80.px,
                    spreadRadius = 0.px,
                    color = shadowColor.toRgb().copy(alpha = 100),
                    inset = true
                )
                .thenIf(hovered) { Modifier.scale(1.05) }
                .transition(CSSTransition("scale", 0.3.s, TransitionTimingFunction.Ease))
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(if (buttonText != null) Alignment.Center else Alignment.CenterStart)
                .padding(2.em)
                .thenIf(hovered) { Modifier.scale(1.05) }
                .transition(CSSTransition("scale", 0.3.s, TransitionTimingFunction.Ease))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.gap(2.px)
            ) {
                SpanText(
                    text = title.uppercase(),
                    modifier = Modifier
                        .fontSize(36.px)
                        .color(contentColor)
                        .textShadow(
                            offsetX = 2.px,
                            offsetY = 2.px,
                            blurRadius = 8.px,
                            color = shadowColor.toRgb()
                        )
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.Start)
                        .height(2.px)
                        .fillMaxWidth(if (hovered) 100.percent else 0.percent)
                        .backgroundColor(contentColor)
                        .transition(CSSTransition("width", 0.3.s, TransitionTimingFunction.Ease))
                )
            }
            SpanText(
                text = description,
                modifier = Modifier
                    .color(contentColor)
                    .textShadow(
                        offsetX = 2.px,
                        offsetY = 2.px,
                        blurRadius = 8.px,
                        color = shadowColor.toRgb()
                    )
            )
            buttonText?.let {
                AppElevatedButton(
                    onClick = onClick,
                ) {
                    SpanText(text = it)
                }
            }
        }
    }
}
