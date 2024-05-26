package pages.home

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.aspectRatio
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.top
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import web.components.layouts.oneLayoutMaxWidth
import web.components.widgets.AppFilledButton
import web.components.widgets.ShimmerHeader
import web.components.widgets.ShimmerText

data class CategoryItem(val id: String, val title: String, val url: String)

@Composable
fun CategoriesSection(
    isLoading: Boolean,
    items: List<CategoryItem>,
    onItemClick: (title: String) -> Unit,
) {
    Row(
        modifier = gridModifier(columns = 3, gap = 1.5.em)
            .maxWidth(oneLayoutMaxWidth)
            .padding(leftRight = 24.px, top = 48.px, bottom = 1.5.em)
    ) {
        if (!isLoading) {
            items
                .take(3)
                .forEach { item ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                    ) {
                        Image(
                            src = item.url,
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
                                onClick = { onItemClick(item.id) },
                            ) {
                                SpanText(item.title.uppercase())
                            }
                        }
                    }
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
