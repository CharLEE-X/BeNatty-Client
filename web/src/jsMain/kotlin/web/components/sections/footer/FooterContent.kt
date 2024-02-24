package web.components.sections.footer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.ColumnScope
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateColumns
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.translateX
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.navigation.OpenLinkStrategy
import com.varabyte.kobweb.navigation.open
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.text.SpanText
import feature.shop.footer.FooterContract
import feature.shop.footer.FooterRoutes
import feature.shop.footer.FooterViewModel
import kotlinx.browser.window
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.value
import theme.MaterialTheme
import theme.roleStyle
import web.components.layouts.oneLayoutMaxWidth
import web.components.widgets.AppOutlinedIconButton
import web.components.widgets.AppTextButton
import web.compose.material3.component.Divider

@Composable
fun Footer(
    footerRoutes: FooterRoutes,
    onError: suspend (String) -> Unit,
    contentColor: CSSColorValue = MaterialTheme.colors.onSurface.value(),
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        FooterViewModel(
            scope = scope,
            onError = onError,
            footerRoutes = footerRoutes,
            goToCompanyWebsite = { window.open("https://www.varabyte.com", OpenLinkStrategy.IN_NEW_TAB) }
        )
    }
    val state by vm.observeStates().collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .backgroundColor(MaterialTheme.colors.surface.value())
            .margin(top = 2.em)
            .zIndex(2)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .maxWidth(oneLayoutMaxWidth)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 1.5.cssRem, bottom = 2.5.em, leftRight = 3.cssRem)
                    .display(DisplayStyle.Grid)
                    .gridTemplateColumns { repeat(4) { size(1.fr) } }
                    .gap(2.em)
            ) {
                FooterSection(
                    title = state.strings.canWeHelpYou.uppercase() + "?",
                    contentColor = contentColor,
                ) {
                    AppTextButton(
                        onClick = { vm.trySend(FooterContract.Inputs.OnTrackOrderClick) },
                        modifier = Modifier.translateX((-12).px)
                    ) {
                        SpanText(text = state.strings.startChat.uppercase())
                    }
                    SpanText(
                        text = "${state.strings.from} ${state.openingTimes.dayFrom} ${state.strings.to} " +
                            "${state.openingTimes.dayTo} ${state.strings.from.lowercase()} ${state.openingTimes.open}" +
                            " ${state.strings.to} ${state.openingTimes.close}.",
                        modifier = Modifier
                            .roleStyle(MaterialTheme.typography.bodyMedium)
                            .color(contentColor)
                    )
                    SpanText(
                        text = "${state.strings.tel}: ${state.contactInfo.phone}".uppercase(),
                        modifier = Modifier
                            .padding(top = 1.em)
                            .roleStyle(MaterialTheme.typography.bodyMedium)
                            .color(contentColor)
                    )
                    SpanText(
                        text = "${state.strings.from} ${state.openingTimes.dayFrom} ${state.strings.to} " +
                            "${state.openingTimes.dayTo} ${state.strings.from.lowercase()} ${state.openingTimes.open}" +
                            " ${state.strings.to} ${state.openingTimes.close}.",
                        modifier = Modifier.roleStyle(MaterialTheme.typography.bodyMedium)
                            .color(contentColor)
                    )
                    AppTextButton(
                        onClick = { vm.trySend(FooterContract.Inputs.OnTrackOrderClick) },
                        modifier = Modifier.translateX((-12).px)
                    ) {
                        SpanText(text = state.strings.sendEmail.uppercase())
                    }
                    SpanText(
                        text = state.strings.weWillReply,
                        modifier = Modifier.roleStyle(MaterialTheme.typography.bodyMedium)
                            .color(contentColor)
                    )
                }
                FooterSection(
                    title = state.strings.help,
                    contentColor = contentColor,
                ) {
                    FooterTextButton(
                        text = state.strings.trackOrder,
                        onClick = { vm.trySend(FooterContract.Inputs.OnTrackOrderClick) },
                    )
                    FooterTextButton(
                        text = state.strings.shipping,
                        onClick = { vm.trySend(FooterContract.Inputs.OnShippingClick) },
                    )
                    FooterTextButton(
                        text = state.strings.returns,
                        onClick = { vm.trySend(FooterContract.Inputs.OnReturnsClick) },
                    )
                    FooterTextButton(
                        text = state.strings.accessibility,
                        onClick = { vm.trySend(FooterContract.Inputs.OnFAQsClick) },
                    )
                }

                FooterSection(
                    title = state.strings.company,
                    contentColor = contentColor,
                ) {
                    FooterTextButton(
                        text = state.strings.contactUs,
                        onClick = { vm.trySend(FooterContract.Inputs.OnPrivacyPolicyClicked) },
                    )
                    FooterTextButton(
                        text = state.strings.aboutUs,
                        onClick = { vm.trySend(FooterContract.Inputs.OnTermsOfServiceClicked) },
                    )
                    if (state.showCareer) {
                        FooterTextButton(
                            text = state.strings.career,
                            onClick = { vm.trySend(FooterContract.Inputs.OnCareerClick) },
                        )
                    }
                    if (state.showPress) {
                        FooterTextButton(
                            text = state.strings.press,
                            onClick = { vm.trySend(FooterContract.Inputs.OnPressClick) },
                        )
                    }
                    if (state.isAdmin) {
                        FooterTextButton(
                            text = state.strings.admin,
                            onClick = { vm.trySend(FooterContract.Inputs.OnGoToAdminHome) },
                        )
                    }
                }
                FooterSection(
                    title = state.strings.followUs,
                    contentColor = contentColor,
                ) {
                    Row(
                        modifier = Modifier
                            .gap(1.em)
                            .margin(top = 1.em)
                    ) {
                        AppOutlinedIconButton(
                            onClick = { },
                        ) {
                            Image(
                                src = "/facebook.png",
                                alt = "Facebook",
                                modifier = Modifier.size(1.em)
                            )
                        }
                        AppOutlinedIconButton(
                            onClick = { },
                        ) {
                            Image(
                                src = "/twitter.png",
                                alt = "Twitter",
                                modifier = Modifier.size(1.em)
                            )
                        }
                        AppOutlinedIconButton(
                            onClick = { },
                        ) {
                            Image(
                                src = "/instagram.png",
                                alt = "Instagram",
                                modifier = Modifier.size(1.em)
                            )
                        }
                    }
                }
            }
        }
        Divider()
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .maxWidth(oneLayoutMaxWidth)
                .gap(1.em)
                .padding(topBottom = 1.em, leftRight = 3.em),
        ) {
            FooterTextButton(
                text = "© ${state.year} ${state.strings.companyName}",
                onClick = { vm.trySend(FooterContract.Inputs.OnCompanyNameClick) },
            )
            Spacer()
            state.paymentMethods.forEach {
                PaymentMethodImage(
                    src = it.imageUrl,
                    alt = it.name
                )
            }
        }
    }
}

@Composable
private fun FooterSection(
    title: String,
    contentColor: CSSColorValue,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier.position(Position.Relative)
    ) {
        SpanText(
            text = title,
            modifier = Modifier
                .fontWeight(FontWeight.SemiBold)
                .margin(bottom = 1.em)
                .color(contentColor)
        )
        content()
    }
}

@Composable
private fun FooterTextButton(
    text: String,
    onClick: () -> Unit,
) {
    AppTextButton(
        onClick = { onClick() },
        modifier = Modifier.translateX((-12).px)
    ) {
        SpanText(text = text)
    }
}


@Composable
private fun PaymentMethodImage(
    src: String,
    alt: String,
) {
    Image(
        src = src,
        alt = alt,
        modifier = Modifier
            .size(2.em)
            .userSelect(UserSelect.None)
    )
}
