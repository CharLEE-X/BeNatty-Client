package web.compose.example.components

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.cssRem
import web.compose.extras.text.LargeTitle
import web.compose.material3.component.labs.ElevatedCard
import web.compose.material3.component.labs.FilledCard
import web.compose.material3.component.labs.OutlinedCard
import web.compose.material3.component.labs.SegmentedButton
import web.compose.material3.component.labs.SegmentedButtonSet

@Composable
fun CardShowcase() {
    LargeTitle("Cards")

    FilledCard {
        CardContent()
    }
    OutlinedCard {
        CardContent()
    }
    ElevatedCard {
        CardContent()
    }

    SegmentedButtonSet {
        SegmentedButton(
            label = "Button 1",
        )
        SegmentedButton(
            label = "Button 2",
            selected = true,
        )
        SegmentedButton(
            label = "Button 3",
            selected = true,
        )
    }
}

@Composable
private fun CardContent() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .gap(1.cssRem)
            .margin(1.cssRem)
    ) {
        Image(
            src = "/kobweb-logo.png",
            description = "",
            modifier = Modifier
                .height(3.cssRem)
                .display(DisplayStyle.Block)
        )
        SpanText("Kobweb is the best!")
    }
}
