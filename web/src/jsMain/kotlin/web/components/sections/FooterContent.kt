package web.components.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.ColumnScope
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.aspectRatio
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateColumns
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.translateX
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.navigation.OpenLinkStrategy
import com.varabyte.kobweb.navigation.open
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.text.SpanText
import component.localization.Strings
import component.localization.getString
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
import theme.MaterialTheme
import theme.roleStyle
import web.components.layouts.oneLayoutMaxWidth
import web.components.widgets.AppOutlinedIconButton
import web.components.widgets.AppTextButton
import web.components.widgets.ShimmerHeader
import web.components.widgets.ShimmerText
import web.compose.material3.component.Divider
import web.util.sectionsGap

@Composable
fun Footer(
    footerRoutes: FooterRoutes,
    onError: suspend (String) -> Unit,
    contentColor: CSSColorValue = MaterialTheme.colors.onSurface,
    isFullLayout: Boolean,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        FooterViewModel(
            scope = scope,
            onError = onError,
            footerRoutes = footerRoutes,
            goToCompanyWebsite = { window.open(it, OpenLinkStrategy.IN_NEW_TAB) }
        )
    }
    val state by vm.observeStates().collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .zIndex(2)
    ) {
        if (isFullLayout) {
            Column(
                modifier = Modifier
                    .position(Position.Relative)
                    .fillMaxWidth()
                    .margin(topBottom = sectionsGap)
                    .maxWidth(oneLayoutMaxWidth)
                    .padding(topBottom = 4.em)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(leftRight = 3.cssRem)
                        .display(DisplayStyle.Grid)
                        .gridTemplateColumns { repeat(4) { size(1.fr) } }
                        .gap(2.em)
                ) {
                    CanWeHelpYouSection(state, contentColor, vm)
                    CompanySection(state, contentColor, vm)
                    HelpSection(state, contentColor, vm)
                    FollowUsSection(state, contentColor)
                }
            }
            Divider(modifier = Modifier.background(MaterialTheme.colors.surface))
        }
        BottomSection(state, vm, contentColor)
    }
}

@Composable
private fun BottomSection(
    state: FooterContract.State,
    vm: FooterViewModel,
    contentColor: CSSColorValue
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .maxWidth(oneLayoutMaxWidth)
            .gap(1.em)
            .padding(topBottom = 1.em, leftRight = 3.em),
    ) {
        if (!state.isLoading) {
            state.companyInfo?.contactInfo?.companyName?.let { companyName ->
                if (state.companyInfo?.contactInfo?.companyWebsite?.isNotEmpty() == true) {
                    FooterTextButton(
                        text = "© ${state.year} $companyName",
                        onClick = { vm.trySend(FooterContract.Inputs.OnCompanyNameClick) },
                    )
                } else {
                    SpanText(
                        text = "© ${state.year} ${getString(Strings.CompanyName)}",
                        modifier = Modifier.color(contentColor)
                    )
                }
            }
        } else {
            ShimmerText(Modifier.width(100.px))
        }
        Spacer()

        if (!state.isLoading) {
            state.paymentMethods.forEach {
                PaymentMethodImage(
                    src = it.imageUrl,
                    alt = it.name
                )
            }
        } else {
            repeat(4) {
                ShimmerText(Modifier.width(60.px))
            }
        }
    }
}

@Composable
private fun FollowUsSection(
    state: FooterContract.State,
    contentColor: CSSColorValue
) {
    FooterSection(
        isLoading = state.isLoading,
        title = getString(Strings.FollowUs),
        contentColor = contentColor,
    ) {
        if (!state.isLoading) {
            Row(
                modifier = Modifier
                    .gap(1.em)
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
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .gap(1.em)
            ) {
                ShimmerHeader(Modifier.width(120.px))
                Row(Modifier.gap(1.em)) {
                    repeat(3) {
                        ShimmerHeader(Modifier.aspectRatio(1))
                    }
                }
            }
        }
    }
}

@Composable
private fun HelpSection(
    state: FooterContract.State,
    contentColor: CSSColorValue,
    vm: FooterViewModel
) {
    FooterSection(
        isLoading = state.isLoading,
        title = getString(Strings.Help),
        contentColor = contentColor,
    ) {
        if ((!state.isLoading)) {
            FooterTextButton(
                text = getString(Strings.TrackOrder),
                onClick = { vm.trySend(FooterContract.Inputs.OnTrackOrderClick) },
            )
            FooterTextButton(
                text = getString(Strings.Shipping),
                onClick = { vm.trySend(FooterContract.Inputs.OnShippingClick) },
            )
            FooterTextButton(
                text = getString(Strings.Returns),
                onClick = { vm.trySend(FooterContract.Inputs.OnReturnsClick) },
            )
        } else {
            ShimmerFooterSection()
        }
    }
}

@Composable
private fun CompanySection(
    state: FooterContract.State,
    contentColor: CSSColorValue,
    vm: FooterViewModel
) {
    FooterSection(
        isLoading = state.isLoading,
        title = getString(Strings.Company),
        contentColor = contentColor,
    ) {
        if (!state.isLoading) {
            FooterTextButton(
                text = getString(Strings.ContactUs),
                onClick = { vm.trySend(FooterContract.Inputs.OnPrivacyPolicyClicked) },
            )
            FooterTextButton(
                text = getString(Strings.AboutUs),
                onClick = { vm.trySend(FooterContract.Inputs.OnTermsOfServiceClicked) },
            )
            if (state.footerConfig?.showCareer == true) {
                FooterTextButton(
                    text = getString(Strings.Career),
                    onClick = { vm.trySend(FooterContract.Inputs.OnCareerClick) },
                )
            }
            if (state.footerConfig?.showPress == true) {
                FooterTextButton(
                    text = getString(Strings.Press),
                    onClick = { vm.trySend(FooterContract.Inputs.OnPressClick) },
                )
            }
            FooterTextButton(
                text = getString(Strings.PrivacyPolicy),
                onClick = { vm.trySend(FooterContract.Inputs.OnPrivacyPolicyClicked) },
            )
            if (state.isAdmin) {
                FooterTextButton(
                    text = getString(Strings.Admin),
                    onClick = { vm.trySend(FooterContract.Inputs.OnGoToAdminHome) },
                )
            }
        } else {
            ShimmerFooterSection()
        }
    }
}

@Composable
private fun CanWeHelpYouSection(
    state: FooterContract.State,
    contentColor: CSSColorValue,
    vm: FooterViewModel
) {
    FooterSection(
        isLoading = state.isLoading,
        title = getString(Strings.CanWeHelpYou).uppercase() + "?",
        contentColor = contentColor,
    ) {
        if (!state.isLoading) {
            AppTextButton(
                onClick = { vm.trySend(FooterContract.Inputs.OnTrackOrderClick) },
                modifier = Modifier.translateX((-12).px)
            ) {
                SpanText(text = getString(Strings.StartChat).uppercase())
            }
            SpanText(
                text = "${getString(Strings.From)} ${state.companyInfo?.openingTimes?.dayFrom} ${getString(Strings.To)} " +
                    "${state.companyInfo?.openingTimes?.dayTo} \n${getString(Strings.From).lowercase()} " +
                    "${state.companyInfo?.openingTimes?.open}" +
                    " ${getString(Strings.To)} ${state.companyInfo?.openingTimes?.close}.",
                modifier = Modifier
                    .roleStyle(MaterialTheme.typography.bodyMedium)
                    .color(contentColor)
            )
            state.companyInfo?.contactInfo?.phone?.let { phone ->
                SpanText(
                    text = "${getString(Strings.Tel)}: $phone".uppercase(),
                    modifier = Modifier
                        .padding(top = 1.em)
                        .roleStyle(MaterialTheme.typography.bodyMedium)
                        .color(contentColor)
                )
                SpanText(
                    text = "${getString(Strings.From)} ${state.companyInfo?.openingTimes?.dayFrom} ${getString(Strings.To)} " +
                        "${state.companyInfo?.openingTimes?.dayTo} ${getString(Strings.From).lowercase()} " +
                        "${state.companyInfo?.openingTimes?.open}" +
                        " ${getString(Strings.To)} ${state.companyInfo?.openingTimes?.close}.",
                    modifier = Modifier.roleStyle(MaterialTheme.typography.bodyMedium)
                        .color(contentColor)
                )
            }
            AppTextButton(
                onClick = { vm.trySend(FooterContract.Inputs.OnTrackOrderClick) },
                modifier = Modifier.translateX((-12).px)
            ) {
                SpanText(text = getString(Strings.SendEmail).uppercase())
            }
            SpanText(
                text = getString(Strings.WeWillReply),
                modifier = Modifier.roleStyle(MaterialTheme.typography.bodyMedium)
                    .color(contentColor)
            )
        } else {
            ShimmerFooterSection()
        }
    }
}

@Composable
private fun FooterSection(
    isLoading: Boolean,
    title: String,
    contentColor: CSSColorValue,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier.position(Position.Relative)
    ) {
        if (!isLoading) {
            SpanText(
                text = title.uppercase(),
                modifier = Modifier
                    .color(contentColor)
                    .margin(bottom = 0.25.em)
            )
        } else {
            ShimmerHeader()
        }
        content()
    }
}

@Composable
private fun ShimmerFooterSection() {
    val width = (20..80).random().px

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .gap(1.em)
    ) {
        ShimmerHeader(Modifier.width(60.px))
        repeat(4) {
            ShimmerText(Modifier.width(width))
        }
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
