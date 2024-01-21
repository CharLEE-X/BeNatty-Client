package web.compose.example.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.px
import web.compose.extras.Row
import web.compose.extras.text.LargeTitle
import web.compose.material3.badge.Badge
import web.compose.material3.icon.Icon
import web.compose.material3.iconbutton.TonalIconButton

@Composable
fun BadgeShowcase() {
    var badgeValue by remember { mutableStateOf(1) }

    LargeTitle("Badges")

    Row {
        TonalIconButton({
            style { padding(5.px) }
        }) {
            Icon("public")
            Badge()
        }

        TonalIconButton({
            onClick { badgeValue++ }
            style { padding(5.px) }
        }) {
            Icon("public")
            Badge(value = badgeValue.toString())
        }

        TonalIconButton({
            style { padding(5.px) }
        }) {
            Icon("public")
            Badge(value = "999+")
        }
    }
}
