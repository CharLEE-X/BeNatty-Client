package web.pages.product.catalogue

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import web.AppColors
import web.components.layouts.oneLayoutMaxWidth
import web.components.widgets.TextLink

@Composable
fun AddSection() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .backgroundColor(AppColors.lightBg)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .maxWidth(oneLayoutMaxWidth)
                .padding(
                    topBottom = 1.5.em,
                    leftRight = 24.px
                )
        ) {
            AddItem(
                title = "Free delivery",
                linkText = "Terms apply",
                onClick = {}
            )
            AddItem(
                title = "Save up to 50%",
                linkText = "Shop sale",
                onClick = {}
            )
            AddItem(
                title = "Styling tips & more",
                linkText = "The blog",
                onClick = {}
            )
        }
    }
}

@Composable
private fun AddItem(
    title: String,
    linkText: String,
    onClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .gap(0.5.em)
            .fontSize(16.px)
    ) {
        SpanText(title)
        TextLink(
            text = linkText,
            onClick = onClick,
        )
    }
}
