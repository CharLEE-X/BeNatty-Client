package web.pages.product

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.silk.components.forms.Button
import com.varabyte.kobweb.silk.components.text.SpanText

@Composable
fun ProductPage(
    id: String,
    onGoBackClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SpanText("ProductDetails: $id")
            Button(
                onClick = { onGoBackClick() }
            ) {
                SpanText("Go back")
            }
        }
    }
}
