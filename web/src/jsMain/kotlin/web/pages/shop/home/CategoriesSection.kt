package web.pages.shop.home

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.aspectRatio
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.maxHeight
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.top
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.text.SpanText
import feature.shop.home.HomeContract
import feature.shop.home.HomeViewModel
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import web.components.widgets.AppFilledButton
import web.components.widgets.ShimmerHeader
import web.components.widgets.ShimmerText

@Composable
fun CategoriesSection(vm: HomeViewModel, state: HomeContract.State) {
    Row(
        modifier = gridModifier(columns = 3)
            .position(Position.Relative)
            .maxHeight(400.px)
            .padding(leftRight = 24.px, topBottom = 48.px)
    ) {
        if (!state.isLoading) {
            state.categorySection
                .take(3)
                .forEach { item ->
                    CategoryItem(
                        title = item.title,
                        url = item.url,
                        onClick = { vm.trySend(HomeContract.Inputs.OnBannerLeftClick) }
                    )
                }
        } else {
            ShimmerCollageItem(Modifier.fillMaxWidth().height(350.px)) {
                ShimmerHeader(Modifier.fillMaxWidth(60.percent))
                ShimmerText(Modifier.fillMaxWidth(50.percent))
            }
            ShimmerCollageItem(
                textPosition = TextPosition.RightTop,
                modifier = Modifier.fillMaxWidth().height(350.px)
            ) {
                ShimmerHeader(Modifier.fillMaxWidth(80.percent))
                ShimmerText(Modifier.fillMaxWidth(60.percent))
            }
        }
    }
}

@Composable
private fun CategoryItem(
    title: String,
    url: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        Image(
            src = url,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .position(Position.Relative)
                .top(70.percent)
        ) {
            AppFilledButton(
                onClick = onClick,
            ) {
                SpanText(title.uppercase())
            }
        }
    }
}
