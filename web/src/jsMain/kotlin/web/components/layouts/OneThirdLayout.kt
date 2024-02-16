package web.components.layouts

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.AlignItems
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.RowScope
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.flex
import com.varabyte.kobweb.compose.ui.modifiers.flexWrap
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.justifyContent
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.minWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexWrap
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

val oneLayoutMaxWidth = 62.375.cssRem

@Composable
fun OneThirdLayout(
    title: String,
    gap: CSSLengthOrPercentageNumericValue = 1.5.em,
    onGoBack: () -> Unit,
    hasBackButton: Boolean,
    actions: @Composable RowScope.() -> Unit,
    content: @Composable () -> Unit,
    contentThird: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .maxWidth(oneLayoutMaxWidth)
            .margin(0.px)
            .padding(2.em)
            .gap(gap)
            .display(DisplayStyle.Flex)
            .flexWrap(FlexWrap.Wrap)
    ) {
        NavTopSection(
            title = title,
            onGoBack = onGoBack,
            hasBackButton = hasBackButton,
            actions = actions,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .gap(gap)
                .display(DisplayStyle.Flex)
                .flexWrap(FlexWrap.Wrap)
                .justifyContent(JustifyContent.Center)
                .alignItems(AlignItems.FlexStart)
        ) {
            Column(
                modifier = Modifier
                    .flex("2 2 30rem")
                    .minWidth(51.percent)
                    .gap(gap)
            ) {
                content()
            }
            Column(
                modifier = Modifier
                    .flex("1 1 15rem")
                    .minWidth(0.px)
                    .gap(gap)
            ) {
                contentThird()
            }
        }
        Box(Modifier.height(4.em))
    }
}
