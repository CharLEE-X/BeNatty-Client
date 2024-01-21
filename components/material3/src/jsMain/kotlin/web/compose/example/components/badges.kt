package web.compose.example.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.padding
import org.jetbrains.compose.web.css.px
import web.compose.extras.Row
import web.compose.extras.text.LargeTitle
import web.compose.material3.component.Badge
import web.compose.material3.component.Icon
import web.compose.material3.component.TonalIconButton

@Composable
fun BadgeShowcase() {
    var badgeValue by remember { mutableStateOf(1) }

    LargeTitle("Badges")

    Row {
        TonalIconButton(
            modifier = Modifier.padding(5.px)
        ) {
            Icon("public")
            Badge()
        }

        TonalIconButton(
            onClick = { badgeValue++ },
            modifier = Modifier.padding(5.px)
        ) {
            Icon("public")
            Badge(value = badgeValue.toString())
        }

        TonalIconButton(
            modifier = Modifier.padding(5.px)
        ) {
            Icon("public")
            Badge(value = "999+")
        }
    }
}
