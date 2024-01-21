package web.compose.example

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import web.compose.extras.BottomNavigationBar
import web.compose.material3.fab.Fab
import web.compose.material3.icon.Icon
import web.compose.material3.navigationtab.NavigationTab
import web.compose.material3.navigationtab.label
import web.compose.material3.slot

@Composable
fun ExampleNavigationBar() {
    BottomNavigationBar {
        NavigationTab({ label = "Home" }) {
            Icon(
                modifier = Modifier.attrsModifier { slot = "activeIcon" },
                iconIdentifier = "home"
            )
            Icon(
                modifier = Modifier.attrsModifier { slot = "inactiveIcon" },
                iconIdentifier = "home"
            )
        }
        NavigationTab({ label = "Explore" }) {
            Icon(
                modifier = Modifier.attrsModifier {
                    slot = "activeIcon"
                },
                iconIdentifier = "public"
            )
            Icon(
                modifier = Modifier.attrsModifier { slot = "inactiveIcon" },
                iconIdentifier = "public"
            )
        }
        NavigationTab({ label = "Edit" }) {
            Icon(
                modifier = Modifier.attrsModifier {
                    slot = "activeIcon"
                },
                iconIdentifier = "edit_note"
            )
            Icon(
                modifier = Modifier.attrsModifier {
                    slot = "inactiveIcon"
                },
                iconIdentifier = "edit_note"
            )
        }
        Fab {
            Icon(
                modifier = Modifier.attrsModifier {
                    slot = "icon"
                },
                iconIdentifier = "add"
            )
        }
    }
}
