package web.components.layouts

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.silk.components.icons.mdi.MdiAdd
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.em
import theme.MaterialTheme
import theme.roleStyle
import web.components.widgets.PageHeader
import web.compose.material3.component.Divider
import web.compose.material3.component.FilledButton
import web.compose.material3.component.labs.SegmentedButton
import web.compose.material3.component.labs.SegmentedButtonSet

@Composable
fun ListPageLayout(
    title: String,
    createText: String,
    pressCreateToStartText: String,
    items: List<ListItem>,
    onAddClick: () -> Unit,
    onItemClick: (String) -> Unit,
    pageInfo: PageInfo? = null,
    onPageClick: (Int) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .gap(1.em)
    ) {
        PageHeader(title) {
            FilledButton(
                onClick = { onAddClick() },
                leadingIcon = { MdiAdd() },
            ) {
                SpanText(text = createText)
            }
        }
        if (items.isEmpty()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                SpanText(text = pressCreateToStartText)
            }
        } else {
            items.forEachIndexed { index, item ->
                Item(
                    name = item.name,
                    onClick = { onItemClick(item.id) },
                )
            }
            Spacer()
            PageNavigator(
                pageInfo = pageInfo,
                onPageClick = onPageClick,
            )
        }
    }
}

data class ListItem(
    val id: String,
    val name: String
)

data class PageInfo(
    val count: Int,
    val pages: Int,
    val prev: Int?,
    val next: Int?,
)

@Composable
private fun PageNavigator(
    pageInfo: PageInfo?,
    onPageClick: (Int) -> Unit,
) {
    if (pageInfo == null) {
        return
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .margin(topBottom = 1.em)
    ) {
        SegmentedButtonSet {
            if (pageInfo.pages == 0) {
                SegmentedButton(
                    label = "1",
                    selected = true,
                    onClick = { onPageClick(1) },
                )
            } else if (pageInfo.pages <= 5) {
                for (i in 1..pageInfo.pages) {
                    SegmentedButton(
                        label = i.toString(),
                        selected = i == pageInfo.next,
                        onClick = { onPageClick(i) },
                    )
                }
            } else {
                SegmentedButton(
                    label = "1",
                    selected = pageInfo.prev == 1,
                    onClick = { onPageClick(1) },
                )
                SegmentedButton(
                    label = "…",
                    selected = false,
                    onClick = {},
                )
                SegmentedButton(
                    label = pageInfo.prev.toString(),
                    selected = true,
                    onClick = { pageInfo.prev?.let { onPageClick(it) } },
                )
                SegmentedButton(
                    label = pageInfo.next.toString(),
                    selected = true,
                    onClick = { pageInfo.next?.let { onPageClick(it) } },
                )
                SegmentedButton(
                    label = "…",
                    selected = false,
                    onClick = {},
                )
                SegmentedButton(
                    label = pageInfo.pages.toString(),
                    selected = pageInfo.next == pageInfo.pages,
                    onClick = { onPageClick(pageInfo.pages) },
                )

            }
        }
    }
}

@Composable
fun Item(
    name: String,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .onClick { onClick() }
                .cursor(Cursor.Pointer)
        ) {
            SpanText(
                text = name,
                modifier = Modifier
                    .roleStyle(MaterialTheme.typography.bodyLarge)
                    .onClick { onClick() }
                    .cursor(Cursor.Pointer)
            )
        }
        Divider(modifier = Modifier.margin(topBottom = 0.5.em))
    }
}
