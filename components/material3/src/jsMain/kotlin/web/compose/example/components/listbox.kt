package web.compose.example.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import web.compose.extras.text.LargeLabel
import web.compose.extras.text.LargeTitle
import web.compose.material3.list.List
import web.compose.material3.list.ListItem
import web.compose.material3.list.headline
import web.compose.material3.list.supportingText

data class ListDataItem(
    val name: String, val organisation: String
)

val listData = listOf(
    ListDataItem("John Doe", "Example.org"),
    ListDataItem("Jane Doe", "Another.example.org")
)

@Composable
fun ListboxShowcase() {
    var selectedListItemValue by remember { mutableStateOf("Initial text field value") }

    LargeTitle("List box")

    LargeLabel("Selected item name: $selectedListItemValue")

    List {
        listData.forEach { listDateItem ->
            ListItem({
                headline = listDateItem.name
                supportingText = listDateItem.organisation
                onClick {
                    selectedListItemValue = listDateItem.name
                }
            })
        }
    }
}
