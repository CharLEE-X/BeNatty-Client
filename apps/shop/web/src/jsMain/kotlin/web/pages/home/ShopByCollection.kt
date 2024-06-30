package web.pages.home

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.aspectRatio
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.tabIndex
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import feature.shop.home.HomeContract
import feature.shop.home.HomeViewModel
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import web.H2Variant
import web.HeadlineStyle
import web.components.layouts.oneLayoutMaxWidth
import web.util.onEnterKeyDown

@Composable
fun ShopByCollection(vm: HomeViewModel, state: HomeContract.State) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .gap(2.em)
            .margin(top = 2.em)
            .padding(leftRight = 24.px)
    ) {
        SpanText(
            text = state.featuredCollections.title.uppercase(),
            modifier = HeadlineStyle.toModifier(H2Variant)
        )
        Row(
            modifier = gridModifier(5, gap = 1.5.em)
                .fillMaxWidth()
                .maxWidth(oneLayoutMaxWidth)
        ) {
            state.featuredCollections.items.forEach { collection ->
                Item(
                    url = collection.url,
                    title = collection.title,
                    onClick = { vm.trySend(HomeContract.Inputs.OnCollectionClicked(collection.title)) }
                )
            }
        }
    }
}

@Composable
private fun Item(
    url: String,
    title: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .gap(1.em)
            .onClick { onClick() }
            .onEnterKeyDown { onClick() }
            .tabIndex(0)
    ) {
        Image(
            src = url,
            alt = title,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1 / 2)
        )
        SpanText(text = title)
    }
}
