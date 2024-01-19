package com.charleex.nataliashop.pages

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.silk.components.forms.Button
import com.varabyte.kobweb.silk.components.text.SpanText

@Composable
fun Home(
    onProductClick: (String) -> Unit,
    onSignOutClick: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SpanText("Home")
            Button(
                onClick = { onProductClick("1") }
            ) {
                SpanText("Product 1")
            }
            Button(
                onClick = { onSignOutClick() }
            ) {
                SpanText("Sign out")
            }
        }
    }
}
