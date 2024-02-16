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
import web.compose.material3.component.FilledButton
import web.compose.material3.component.TextButton
import web.compose.material3.component.labs.OutlinedSegmentedButtonSet
import web.compose.material3.component.labs.SegmentedButton

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
                FilledButton(
                    onClick = { onCategoryClick(category) },
                    content = { Text(category.name) }
                )
            } else {
                TextButton(
                    onClick = { onCategoryClick(category) },
                    content = { Text(category.name) }
                )
            }
        }
        Spacer()
        OutlinedSegmentedButtonSet {
            categoryFilters.forEach { filter ->
                SegmentedButton(
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
