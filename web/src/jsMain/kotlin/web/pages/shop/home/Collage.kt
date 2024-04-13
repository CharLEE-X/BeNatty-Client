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
import com.varabyte.kobweb.compose.foundation.layout.ColumnScope
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.aspectRatio
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
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
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onMouseEnter
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.scale
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.tabIndex
import com.varabyte.kobweb.compose.ui.modifiers.textShadow
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.base
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import data.GetLandingConfigQuery
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import theme.MaterialTheme
import web.HeadlineTextStyle
import web.components.widgets.AppElevatedButton
import web.components.widgets.AppElevatedCard
import web.components.widgets.Shimmer
import web.components.widgets.ShimmerButton
import web.components.widgets.ShimmerHeader
import web.components.widgets.ShimmerText
import web.util.glossy
import web.util.onEnterKeyDown

fun gridModifier(
    columns: Int = 3,
    gap: CSSLengthOrPercentageNumericValue = 1.em,
    rowMinHeight: CSSLengthOrPercentageNumericValue = 200.px
) = Modifier
    .fillMaxWidth()
    .display(DisplayStyle.Grid)
    .gridTemplateColumns { repeat(columns) { size(1.fr) } }
    .gridAutoRows { minmax(rowMinHeight, 1.fr) }
    .gap(gap)

val CollageBigItemStyle by ComponentStyle.base {
    Modifier
        .gridColumn(1, 3)
        .gridRow(1, 3)
}

@Composable
fun Collage(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    items: List<GetLandingConfigQuery.CollageItem>,
    shopNowText: String,
    onCollageItemClick: (GetLandingConfigQuery.CollageItem) -> Unit,
) {
    Column(
        modifier = gridModifier(columns = 3).then(modifier)
            .padding(leftRight = 24.px, top = 24.px, bottom = 48.px)
            .glossy()
    ) {
        if (!isLoading) {
            items.forEachIndexed { index, item ->
                CollageItem(
                    title = item.title ?: "",
                    description = item.description ?: "",
                    buttonText = if (index == 0) shopNowText else null,
                    onClick = { onCollageItemClick(item) },
                    textPosition = if (index == 0) TextPosition.Center else TextPosition.LeftBottom,
                    modifier = Modifier
                        .thenIf(index == 0) { CollageBigItemStyle.toModifier() }
                ) { imageModifier ->
                    Image(
                        src = item.media?.url ?: "",
                        alt = item.title ?: "",
                        modifier = imageModifier
                    )
                }
            }
        } else {
            ShimmerCollageItem(
                textPosition = TextPosition.Center,
                modifier = CollageBigItemStyle.toModifier().fillMaxSize(),
            ) {
                ShimmerHeader(Modifier.fillMaxWidth(60.percent))
                ShimmerText(Modifier.fillMaxWidth(70.percent))
                ShimmerButton(Modifier.fillMaxWidth(30.percent).margin(top = 0.5.em))
            }
            ShimmerCollageItem {
                ShimmerHeader(Modifier.fillMaxWidth())
                ShimmerHeader(Modifier.fillMaxWidth(50.percent))
                ShimmerText(Modifier.fillMaxWidth(80.percent))
            }
            ShimmerCollageItem {
                ShimmerHeader(Modifier.fillMaxWidth(40.percent))
                ShimmerHeader(Modifier.fillMaxWidth(90.percent))
                ShimmerHeader(Modifier.fillMaxWidth(60.percent))
                ShimmerText(Modifier.fillMaxWidth(80.percent))
                ShimmerText(Modifier.fillMaxWidth(60.percent))
            }
            ShimmerCollageItem {
                ShimmerHeader(Modifier.fillMaxWidth(50.percent))
                ShimmerText(Modifier.fillMaxWidth(80.percent))
                ShimmerText(Modifier.fillMaxWidth(40.percent))
            }
            ShimmerCollageItem {
                ShimmerHeader(Modifier.fillMaxWidth(60.percent))
                ShimmerText(Modifier.fillMaxWidth(80.percent))
                ShimmerText(Modifier.fillMaxWidth(40.percent))
            }
            ShimmerCollageItem {
                ShimmerHeader(Modifier.fillMaxWidth(40.percent))
                ShimmerText(Modifier.fillMaxWidth(80.percent))
            }
        }
    }
}

@Composable
fun ShimmerCollageItem(
    modifier: Modifier = Modifier
        .fillMaxSize()
        .aspectRatio(1.0),
    textPosition: TextPosition = TextPosition.LeftBottom,
    content: @Composable ColumnScope.() -> Unit,
) {
    Shimmer(
        textPosition = textPosition,
        modifier = modifier
            .padding(50.px)
    ) {
        content()
    }
}

enum class TextPosition {
    Center, LeftBottom, RightTop
}

@Composable
fun CollageItem(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    buttonText: String? = null,
    textPosition: TextPosition,
    onClick: () -> Unit,
    contentColor: Color = Colors.White,
    shadowColor: Color = Color.rgb(30, 30, 59),
    borderRadius: CSSLengthOrPercentageNumericValue = 12.px,
    image: @Composable (imageModifier: Modifier) -> Unit,
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
            .tabIndex(0)
            .onEnterKeyDown(onClick)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .backgroundColor(MaterialTheme.colors.surfaceContainerHighest)
                .onClick { onClick() }
        ) {
            image(
                Modifier
                    .fillMaxSize()
                    .borderRadius(borderRadius)
                    .objectFit(ObjectFit.Cover)
                    .thenIf(hovered) { Modifier.scale(1.04) }
                    .transition(CSSTransition("scale", 0.3.s, TransitionTimingFunction.Ease))
            )
            Column(
                horizontalAlignment = when (textPosition) {
                    TextPosition.Center -> Alignment.CenterHorizontally
                    TextPosition.LeftBottom -> Alignment.Start
                    TextPosition.RightTop -> Alignment.End
                },
                modifier = Modifier
                    .align(
                        when (textPosition) {
                            TextPosition.Center -> Alignment.Center
                            TextPosition.LeftBottom -> Alignment.BottomStart
                            TextPosition.RightTop -> Alignment.TopEnd
                        }
                    )
                    .padding(50.px)
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
                        containerColor = MaterialTheme.colors.primary,
                        modifier = Modifier
                            .margin(top = 30.px)
                            .size(200.px, 80.px)
                            .tabIndex(0)
                            .onEnterKeyDown(onClick)
                    ) {
                        SpanText(
                            text = it,
                            modifier = Modifier
                                .fontSize(1.5.em)
                                .color(MaterialTheme.colors.onPrimary)
                        )
                    }
                }
            }
        }
    }
}
