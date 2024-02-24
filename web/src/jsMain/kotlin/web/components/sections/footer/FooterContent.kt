package web.components.sections.footer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
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
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.text.SpanText
import feature.shop.footer.FooterContract
import feature.shop.footer.FooterRoutes
import feature.shop.footer.FooterViewModel
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.fr
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
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        FooterViewModel(
            scope = scope,
            onError = onError,
            footerRoutes = footerRoutes,
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
                Column(
                    modifier = Modifier
                        .roleStyle(MaterialTheme.typography.bodyMedium)
                ) {
                    SpanText(
                        text = "Can we help you?".uppercase(),
                        modifier = Modifier
                            .fontWeight(FontWeight.SemiBold)
                    )
                    SpanText(
                        text = "Start chat".uppercase(),
                        modifier = Modifier.padding(top = 1.em)
                    )
                    SpanText(
                        text = "From Mondays to Fridays from 9:00 to 18:00.",
                    )
                    SpanText(
                        text = "TEL: (123) 456-7890".uppercase(),
                        modifier = Modifier.padding(top = 1.em)
                    )
                    SpanText(
                        text = "From Mondays to Fridays from 9:00 to 18:00.",
                    )

                    SpanText(
                        text = "Send email".uppercase(),
                        modifier = Modifier.padding(top = 1.em)
                    )
                    SpanText(
                        text = "We'll reply ASAP.",
                    )
                }
                Column(
                    modifier = Modifier.position(Position.Relative)
                ) {
                    SpanText(
                        text = state.strings.help,
                        modifier = Modifier
                            .fontWeight(FontWeight.SemiBold)
                            .margin(bottom = 1.em, left = 1.em),
                    )
                    AppTextButton(
                        onClick = { vm.trySend(FooterContract.Inputs.OnTrackOrderClick) },
                    ) {
                        SpanText(state.strings.trackOrder)
                    }
                    AppTextButton(
                        onClick = { vm.trySend(FooterContract.Inputs.OnShippingClick) },
                    ) {
                        SpanText(state.strings.shipping)
                    }
                    AppTextButton(
                        onClick = { vm.trySend(FooterContract.Inputs.OnReturnsClick) },
                    ) {
                        SpanText(state.strings.returns)
                    }
                    AppTextButton(
                        onClick = { vm.trySend(FooterContract.Inputs.OnFAQsClick) },
                    ) {
                        SpanText(state.strings.faQs)
                    }
                }
                Column(
                    modifier = Modifier.position(Position.Relative)
                ) {
                    SpanText(
                        text = state.strings.company,
                        modifier = Modifier
                            .fontWeight(FontWeight.SemiBold)
                            .margin(bottom = 1.em, left = 1.em),
                    )
                    AppTextButton(
                        onClick = { vm.trySend(FooterContract.Inputs.OnPrivacyPolicyClicked) },
                    ) {
                        SpanText(state.strings.privacyPolicy)
                    }
                    AppTextButton(
                        onClick = { vm.trySend(FooterContract.Inputs.OnTermsOfServiceClicked) },
                    ) {
                        SpanText(state.strings.termsOfService)
                    }
                    if (state.showCareer) {
                        AppTextButton(
                            onClick = { vm.trySend(FooterContract.Inputs.OnCareerClick) },
                        ) {
                            SpanText(state.strings.career)
                        }
                    }
                    if (state.showPress) {
                        AppTextButton(
                            onClick = { vm.trySend(FooterContract.Inputs.OnPressClick) },
                        ) {
                            SpanText(state.strings.press)
                        }
                    }
                    if (state.isAdmin) {
                        AppTextButton(
                            onClick = { vm.trySend(FooterContract.Inputs.OnGoToAdminHome) },
                        ) {
                            SpanText(state.strings.admin)
                        }
                    }
                }
                Column(
                    modifier = Modifier.position(Position.Relative)
                ) {
                    SpanText(
                        text = state.strings.followUs,
                        modifier = Modifier
                            .fontWeight(FontWeight.SemiBold)
                            .margin(bottom = 1.em),
                    )
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .maxWidth(oneLayoutMaxWidth)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .gap(1.em)
                    .padding(topBottom = 1.em, leftRight = 3.em),
            ) {
                SpanText(
                    text = "© ${state.year} ${state.strings.companyName}",
                    modifier = Modifier.roleStyle(MaterialTheme.typography.bodyMedium),
                )
                Spacer()
                PaymentMethodImage(
                    src = "https://cdn-icons-png.flaticon.com/128/196/196578.png",
                    alt = "Visa"
                )
                PaymentMethodImage(
                    src = "https://cdn-icons-png.flaticon.com/128/14062/14062982.png",
                    alt = "Mastercard"
                )
                PaymentMethodImage(
                    src = "https://cdn-icons-png.flaticon.com/128/349/349228.png",
                    alt = "American Express"
                )
                PaymentMethodImage(
                    src = "https://cdn-icons-png.flaticon.com/128/196/196565.png",
                    alt = "Paypal"
                )
            }
        }
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
    )
}
