package web.components.sections.desktopNav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.AlignItems
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.functions.clamp
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.animation
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderBottom
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.flex
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.onAnimationEnd
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onMouseEnter
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.setVariable
import com.varabyte.kobweb.compose.ui.modifiers.translateX
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.silk.components.animation.Keyframes
import com.varabyte.kobweb.silk.components.animation.toAnimation
import com.varabyte.kobweb.silk.components.overlay.Overlay
import com.varabyte.kobweb.silk.components.overlay.OverlayVars
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import org.jetbrains.compose.web.css.AnimationDirection
import org.jetbrains.compose.web.css.AnimationFillMode
import org.jetbrains.compose.web.css.AnimationTimingFunction
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import theme.toSitePalette
import web.components.layouts.Category
import web.components.layouts.CategoryFilter

@Composable
private fun NavLink(
    text: String,
    onClick: () -> Unit,
    isCurrent: Boolean,
    isSpecial: Boolean,
    activeColor: Color,
    inactiveColor: Color,
    specialColor: Color
) {
    var isHovered by remember { mutableStateOf(false) }

    val textColor = if (isSpecial) {
        specialColor
    } else if (isHovered || isCurrent) activeColor else inactiveColor

    val indicatorColor = if (isCurrent) activeColor else Colors.Transparent

    Box(
        modifier = Modifier.borderBottom(2.px, LineStyle.Solid, indicatorColor)
    ) {
        SpanText(
            text = text,
            modifier = Modifier
                .padding(0.5.cssRem)
                .borderRadius(0.5.cssRem)
                .color(textColor)
                .cursor(Cursor.Pointer)
                .onMouseEnter { isHovered = true }
                .onMouseLeave { isHovered = false }
                .onClick { onClick() }
                .alignItems(AlignItems.FlexStart)
                .gap(10.px)
                .flex(0, 0),
        )
    }
}

val SideMenuSlideInAnim by Keyframes {
    from {
        Modifier.translateX(100.percent)
    }

    to {
        Modifier
    }
}

// Note: When the user closes the side menu, we don't immediately stop rendering it (at which point it would disappear
// abruptly). Instead, we start animating it out and only stop rendering it when the animation is complete.
enum class SideMenuState {
    CLOSED,
    OPEN,
    CLOSING;

    fun close() = when (this) {
        CLOSED -> CLOSED
        OPEN -> CLOSING
        CLOSING -> CLOSING
    }
}

@Composable
fun CategoryBar(
    categories: List<Category>,
    currentCategory: Category?,
    onCategoryClick: (Category) -> Unit,
    categoryFilters: List<CategoryFilter>,
    currentCategoryFilter: CategoryFilter?,
    onCategoryFilterClick: (CategoryFilter) -> Unit,
    horizontalMargin: CSSSizeValue<CSSUnit.em>,
) {
    val colorMode by ColorMode.currentState

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(leftRight = horizontalMargin)
            .gap(1.cssRem)
            .fontSize(1.cssRem)
            .display(DisplayStyle.LegacyInlineFlex),
    ) {
        categories.forEach { category ->
            NavLink(
                text = category.name,
                isCurrent = category == currentCategory,
                isSpecial = category == Category.Promos,
                onClick = { onCategoryClick(category) },
                activeColor = colorMode.toSitePalette().primary,
                inactiveColor = colorMode.toSitePalette().neutral700,
                specialColor = colorMode.toSitePalette().error,
            )
        }
        Spacer()
        categoryFilters.forEach { categoryFilter ->
            NavLink(
                text = categoryFilter.name,
                isCurrent = categoryFilter == currentCategoryFilter,
                isSpecial = false,
                onClick = { onCategoryFilterClick(categoryFilter) },
                activeColor = colorMode.toSitePalette().primary,
                inactiveColor = colorMode.toSitePalette().neutral700,
                specialColor = colorMode.toSitePalette().error,
            )
        }
    }
}

@Composable
private fun SideMenu(menuState: SideMenuState, close: () -> Unit, onAnimationEnd: () -> Unit) {
    Overlay(
        Modifier
            .setVariable(OverlayVars.BackgroundColor, Colors.Transparent)
            .onClick { close() },
    ) {
        key(menuState) { // Force recompute animation parameters when close button is clicked
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(clamp(8.cssRem, 33.percent, 10.cssRem))
                    .align(Alignment.CenterEnd)
                    // Close button will appear roughly over the hamburger button, so the user can close
                    // things without moving their finger / cursor much.
                    .padding(top = 1.cssRem, leftRight = 1.cssRem)
                    .gap(1.5.cssRem)
                    .backgroundColor(ColorMode.current.toSitePalette().nearBackground)
                    .animation(
                        SideMenuSlideInAnim.toAnimation(
                            duration = 200.ms,
                            timingFunction = if (menuState == SideMenuState.OPEN) AnimationTimingFunction.EaseOut else AnimationTimingFunction.EaseIn,
                            direction = if (menuState == SideMenuState.OPEN) AnimationDirection.Normal else AnimationDirection.Reverse,
                            fillMode = AnimationFillMode.Forwards,
                        ),
                    )
                    .borderRadius(topLeft = 2.cssRem)
                    .onClick { it.stopPropagation() }
                    .onAnimationEnd { onAnimationEnd() },
                horizontalAlignment = Alignment.End,
            ) {
//                CloseButton(onClick = { close() })
                Column(
                    Modifier
                        .padding(right = 0.75.cssRem)
                        .gap(1.5.cssRem)
                        .fontSize(1.4.cssRem),
                    horizontalAlignment = Alignment.End,
                ) {
//                    CategoryBar()
                }
            }
        }
    }
}
