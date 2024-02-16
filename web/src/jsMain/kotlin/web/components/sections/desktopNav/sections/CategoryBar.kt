package web.components.sections.desktopNav.sections

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.width
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Text
import web.Category
import web.CategoryFilter
import web.components.widgets.AppFilledButton
import web.components.widgets.AppOutlinedSegmentedButtonSet
import web.components.widgets.AppSegmentedButton
import web.components.widgets.AppTextButton

@Composable
fun CategoryBar(
    modifier: Modifier = Modifier,
    categories: List<Category>,
    onCategoryClick: (Category) -> Unit,
    categoryFilters: List<CategoryFilter>,
    currentCategoryFilter: CategoryFilter?,
    onCategoryFilterClick: (CategoryFilter) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.gap(1.cssRem)
    ) {
        categories.forEach { category ->
            if (category == Category.Promos) {
                AppFilledButton(
                    onClick = { onCategoryClick(category) },
                    content = { Text(category.name) }
                )
            } else {
                AppTextButton(
                    onClick = { onCategoryClick(category) },
                    content = { Text(category.name) }
                )
            }
        }
        Spacer()
        AppOutlinedSegmentedButtonSet {
            categoryFilters.forEach { filter ->
                AppSegmentedButton(
                    label = filter.name,
                    selected = currentCategoryFilter == filter,
                    onClick = { onCategoryFilterClick(filter) },
                    modifier = Modifier
                        .width(100.px)
                )
            }
        }
    }
}
