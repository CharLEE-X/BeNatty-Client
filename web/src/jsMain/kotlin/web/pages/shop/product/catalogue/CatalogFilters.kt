package web.pages.shop.product.catalogue

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.aspectRatio
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.flexWrap
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.mdi.MdiBrokenImage
import com.varabyte.kobweb.silk.components.text.SpanText
import component.localization.Strings
import component.localization.getString
import data.GetAllCategoriesAsMinimalQuery
import feature.product.catalog.CatalogContract
import feature.product.catalog.CatalogViewModel
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexWrap
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import theme.MaterialTheme
import web.compose.material3.component.Divider

@Composable
fun CatalogueFilters(
    modifier: Modifier,
    vm: CatalogViewModel,
    state: CatalogContract.State,
) {
    Column(
        modifier = modifier
            .gap(1.em)
    ) {
        Divider()
        MatchAllSection(
            matchAllText = getString(Strings.MatchAll),
            categories = state.categories,
            onClick = { vm.trySend(CatalogContract.Inputs.OnCategoryClicked(it)) }
        )
    }
}

@Composable
private fun MatchAllSection(
    matchAllText: String,
    categories: List<GetAllCategoriesAsMinimalQuery.GetAllCategoriesAsMinimal>,
    onClick: (String) -> Unit
) {
    SpanText(
        text = matchAllText,
        modifier = Modifier
            .color(MaterialTheme.colors.onSurface)
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .display(DisplayStyle.Flex)
            .flexWrap(FlexWrap.Wrap)
            .gap(0.5.em)
    ) {
        categories.forEach { category ->
            CategoryFilter(
                category = category,
                onClick = { onClick(category.id) }
            )
        }
    }
}

@Composable
fun CategoryFilter(
    category: GetAllCategoriesAsMinimalQuery.GetAllCategoriesAsMinimal,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .gap(0.5.em)
            .onClick { onClick() }
            .userSelect(UserSelect.None)
            .cursor(Cursor.Pointer)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth(33.percent)
                .aspectRatio(1f)
                .color(MaterialTheme.colors.surface)
                .borderRadius(12.px)
        ) {
            category.imageUrl?.let { image ->
                Image(
                    src = image,
                    alt = category.name,
                    modifier = Modifier
                        .size(48.px)
                )
            } ?: run {
                MdiBrokenImage(
                    modifier = Modifier
                        .size(48.px)
                        .color(MaterialTheme.colors.onSurface)
                )
            }
        }
        SpanText(
            text = category.name,
            modifier = Modifier
                .color(MaterialTheme.colors.onSurface)
        )
    }
}

