package web.pages.shop.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.onMouseEnter
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.scale
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.textShadow
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.toModifier
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
import web.HeadlineTextStyle
import web.components.widgets.AppElevatedButton
import web.components.widgets.AppElevatedCard


@Composable
fun Collage(
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
        state.collageItems.forEachIndexed { index, item ->
            CollageItem(
                title = item.title,
                description = item.description,
                imageUrl = item.imageUrl,
                buttonText = if (index == 0) state.strings.shopNow else null,
                onClick = { vm.trySend(HomeContract.Inputs.OnCollageItemClick(item)) },
                isCentered = index == 0,
                modifier = Modifier
                    .thenIf(index == 0) {
                        Modifier
                            .gridColumn(1, 3)
                            .gridRow(1, 3)
                    }
            )
        }
    }
}

@Composable
private fun CollageItem(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    imageUrl: String,
    buttonText: String? = null,
    isCentered: Boolean,
    onClick: () -> Unit,
    contentColor: CSSColorValue = Colors.White,
    shadowColor: Color.Rgb = Colors.Black,
    borderRadius: CSSLengthOrPercentageNumericValue = 12.px,
) {
    var hovered by remember { mutableStateOf(false) }

    AppElevatedCard(
        elevation = 0,
        modifier = modifier
            .fillMaxSize()
            .position(Position.Relative)
            .aspectRatio(1.0)
            .borderRadius(borderRadius)
            .onMouseEnter { hovered = true }
            .onMouseLeave { hovered = false }
            .cursor(Cursor.Pointer)
            .overflow(Overflow.Hidden)
            .scale(if (hovered) 1.01 else 1.0)
            .transition(CSSTransition("scale", 0.3.s, TransitionTimingFunction.Ease))
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CollageImage(
                imageUrl = imageUrl,
                title = title,
                borderRadius = borderRadius,
                hovered = hovered
            )
            ImageOverlay(
                shadowColor = shadowColor,
                hovered = hovered
            )
            Column(
                horizontalAlignment = if (isCentered) Alignment.CenterHorizontally else Alignment.Start,
                modifier = Modifier
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
                        modifier = HeadlineTextStyle.toModifier()
                            .fontSize(2.em)
                            .color(contentColor)
                            .textShadow(
                                offsetX = 2.px,
                                offsetY = 2.px,
                                blurRadius = 8.px,
                                color = shadowColor
                            )
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.Start)
                            .height(2.px)
                            .fillMaxWidth(if (hovered) 100.percent else 0.percent)
                            .backgroundColor(contentColor)
                            .borderRadius(2.px)
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
                            color = shadowColor
                        )
                )
                buttonText?.let {
                    AppElevatedButton(
                        containerShape = 16.px,
                        onClick = onClick,
                        modifier = Modifier
                            .margin(top = 30.px)
                            .size(200.px, 80.px)
                    ) {
                        SpanText(
                            text = it,
                            modifier = Modifier.fontSize(1.5.em)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ImageOverlay(shadowColor: Color.Rgb, hovered: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .backgroundColor(shadowColor.toRgb().copy(alpha = 40))
            .boxShadow(
                offsetX = 0.px,
                offsetY = 0.px,
                blurRadius = 80.px,
                spreadRadius = 0.px,
                color = shadowColor.toRgb().copy(alpha = 120),
                inset = true
            )
            .thenIf(hovered) { Modifier.scale(1.05) }
            .transition(CSSTransition("scale", 0.3.s, TransitionTimingFunction.Ease))
    )
}

@Composable
private fun CollageImage(
    imageUrl: String,
    title: String,
    borderRadius: CSSLengthOrPercentageNumericValue,
    hovered: Boolean
) {
    Image(
        src = imageUrl,
        alt = title,
        modifier = Modifier
            .fillMaxSize()
            .borderRadius(borderRadius)
            .objectFit(ObjectFit.Cover)
            .thenIf(hovered) { Modifier.scale(1.04) }
            .transition(CSSTransition("scale", 0.3.s, TransitionTimingFunction.Ease))
    )
}
