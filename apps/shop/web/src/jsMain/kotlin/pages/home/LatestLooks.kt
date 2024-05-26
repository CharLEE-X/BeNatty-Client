package pages.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.minWidth
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.translateY
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.colors.palette.background
import com.varabyte.kobweb.silk.theme.colors.palette.toPalette
import component.localization.Strings
import component.localization.getString
import feature.shop.home.HomeContract
import feature.shop.home.HomeViewModel
import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.plus
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.times
import web.AppColors
import web.H2Variant
import web.HeadlineStyle
import web.components.widgets.AppFilledButton
import web.components.widgets.AppTextButton
import web.components.widgets.Spacer

@Composable
fun LatestLooks(vm: HomeViewModel, state: HomeContract.State) {
    val id = "latest-looks"
    val multiplier = 0.06
    val containerHeight = 700.px

    var imageHeight by remember { mutableStateOf(0.px) }
    var imageTranslateY by remember { mutableStateOf(0.px) }

    LaunchedEffect(window.outerHeight) {
        imageHeight = containerHeight + (window.outerHeight.px * multiplier) + 100.px
    }

    window.addEventListener("scroll", {
        val element = document.getElementById(id) ?: return@addEventListener
        val topVisible = element.getBoundingClientRect().top - window.innerHeight
        imageTranslateY = topVisible.px * multiplier

//        println(
//            """
//            windowHeight: ${window.outerHeight}
//            imageHeight: $imageHeight
//            imageTranslateY: $imageTranslateY
//        """.trimIndent()
//        )
    })

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(containerHeight)
            .margin(top = 2.em)
    ) {
        Box(
            modifier = Modifier
                .id(id)
                .fillMaxSize()
                .overflow(Overflow.Clip)
        ) {
            Image(
                src = "https://icon-shopify-theme.myshopify.com/cdn/shop/files/icon__hero--image-1.jpg?v=1635967751&width=3000",
                alt = "Latest Looks",
                modifier = Modifier
                    .minWidth(100.percent)
                    .height(imageHeight)
                    .objectFit(ObjectFit.Cover)
                    .translateY(imageTranslateY)
            )
            Column(
                modifier = Modifier
                    .zIndex(1)
                    .align(Alignment.BottomEnd)
                    .margin(
                        bottom = 4.em,
                        right = 8.em,
                    )
                    .padding(2.em)
                    .backgroundColor(ColorMode.current.toPalette().background)
            ) {
                SpanText(
                    text = getString(Strings.TheLatestLooks).uppercase(),
                    modifier = HeadlineStyle.toModifier(H2Variant)
                )
                SpanText(getString(Strings.LatestLooksDescription1))
                Row {
                    SpanText("${getString(Strings.Shop)}: ")
                    state.latestLooksCategories.forEachIndexed { index, item ->
                        val isLast = index == state.latestLooksCategories.size - 1

                        if (index > 0 && !isLast) {
                            SpanText(", ")
                        }

                        if (isLast) {
                            SpanText(" ${getString(Strings.And)} ")
                        }

                        AppTextButton(
                            onClick = { vm.trySend(HomeContract.Inputs.OnLatestLooksItemClick(index.toString())) },
                            content = { SpanText(item.title) }
                        )
                    }
                }
                Spacer(1.em)
                AppFilledButton(
                    onClick = { vm.trySend(HomeContract.Inputs.OnSeeMoreNewArrivalsClicked) },
                    content = { SpanText(getString(Strings.Explore)) }
                )
            }
        }
        FreeShipping(vm, state)
    }
}

@Composable
private fun FreeShipping(vm: HomeViewModel, state: HomeContract.State) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .backgroundColor(AppColors.lightBg)
            .padding(2.em)
    ) {
        SpanText(
            text = getString(Strings.FreeShipping).uppercase(),
            modifier = HeadlineStyle.toModifier(H2Variant)
        )
        Spacer(1.em)
        SpanText("Shop in confidence with our free shipping and free returns promise!")
    }
}
