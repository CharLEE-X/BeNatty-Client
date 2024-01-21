package web.compose.example.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import web.compose.extras.text.LargeTitle
import web.compose.material3.disabled
import web.compose.material3.switch.Switch
import web.compose.material3.switch.selected

@Composable
fun SwitchShowcase() {
    var switchStatus by remember { mutableStateOf(false) }

    LargeTitle("Switch")
    Switch({
        selected(switchStatus)
        onClick { switchStatus = !switchStatus }
    })
    Switch({
        selected(switchStatus)
        onClick { switchStatus = !switchStatus }
        disabled()
    })
}
