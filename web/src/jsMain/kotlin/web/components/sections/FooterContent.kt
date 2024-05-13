package web.components.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.ColumnScope
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.aspectRatio
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.gridColumn
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateColumns
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.onKeyDown
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.navigation.OpenLinkStrategy
import com.varabyte.kobweb.navigation.open
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.layout.HorizontalDivider
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.colors.palette.background
import com.varabyte.kobweb.silk.theme.colors.palette.color
import com.varabyte.kobweb.silk.theme.colors.palette.toPalette
import component.localization.Strings
import component.localization.getString
import feature.shop.footer.FooterContract
import feature.shop.footer.FooterRoutes
import feature.shop.footer.FooterViewModel
import kotlinx.browser.window
import org.jetbrains.compose.web.attributes.AutoComplete
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import web.H3Variant
import web.HeadlineStyle
import web.components.layouts.oneLayoutMaxWidth
import web.components.widgets.AppDividerHorizontal
import web.components.widgets.AppFilledButton
import web.components.widgets.AppIconButton
import web.components.widgets.AppOutlinedTextField
import web.components.widgets.AppTextButton
import web.components.widgets.ShimmerHeader
import web.components.widgets.ShimmerText
import web.util.sectionsGap

@Composable
fun Footer(
    footerRoutes: FooterRoutes,
    onError: suspend (String) -> Unit,
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
            .backgroundColor(Color("#3e3e3e"))
            .color(ColorMode.current.opposite.toPalette().color)
    ) {
        if (isFullLayout) {
            Column(
                modifier = Modifier
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
                    CompanySection(state, vm)
                    HelpSection(state, vm)
                    FollowUsSection(state, vm)
                }
            }
            HorizontalDivider()
        }
        BottomSection(state, vm)
    }
}

@Composable
private fun BottomSection(
    state: FooterContract.State,
    vm: FooterViewModel,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .maxWidth(oneLayoutMaxWidth)
            .gap(1.em)
            .padding(top = 1.em, bottom = 3.em, leftRight = 3.em),
    ) {
        if (!state.isLoading) {
            state.companyInfo?.contactInfo?.companyName?.let { companyName ->
                if (state.companyInfo?.contactInfo?.companyWebsite?.isNotEmpty() == true) {
                    AppTextButton(
                        onClick = { vm.trySend(FooterContract.Inputs.OnCompanyNameClick) },
                        content = { SpanText(text = "© ${state.year} $companyName") }
                    )
                } else {
                    SpanText("© ${state.year} ${getString(Strings.CompanyName)}")
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
    vm: FooterViewModel
) {
    FooterSection(
        isLoading = state.isLoading,
        title = getString(Strings.ConnectWithUs),
        modifier = Modifier
            .gridColumn(3, 5)
    ) {
        if (!state.isLoading) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                AppOutlinedTextField(
                    text = state.connectEmail,
                    onTextChanged = { vm.trySend(FooterContract.Inputs.SetConnectEmail(it)) },
                    placeholder = getString(Strings.JoinOurEmailList),
                    autoComplete = AutoComplete.email,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onKeyDown { if (it.key == "Enter") vm.trySend(FooterContract.Inputs.OnConnectEmailSend) }
                )
                AppFilledButton(
                    onClick = { vm.trySend(FooterContract.Inputs.OnConnectEmailSend) },
                    bgColor = ColorMode.current.opposite.toPalette().background,
                    content = { SpanText(getString(Strings.Join)) },
                    modifier = Modifier.width(30.percent)
                )
            }
            Row(
                modifier = Modifier
                    .gap(1.em)
            ) {
                AppIconButton(
                    onClick = { },
                    icon = {
                        Image(
                            src = "/facebook.png",
                            alt = "Facebook",
                            modifier = Modifier.size(1.em)
                        )
                    }
                )
                AppIconButton(
                    onClick = { },
                    icon = {
                        Image(
                            src = "/twitter.png",
                            alt = "Twitter",
                            modifier = Modifier.size(1.em)
                        )
                    }
                )
                AppIconButton(
                    onClick = { },
                    icon = {
                        Image(
                            src = "/instagram.png",
                            alt = "Instagram",
                            modifier = Modifier.size(1.em)
                        )
                    }
                )
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
    vm: FooterViewModel
) {
    FooterSection(
        isLoading = state.isLoading,
        title = getString(Strings.Help),
    ) {
        if ((!state.isLoading)) {
            AppTextButton(
                onClick = { vm.trySend(FooterContract.Inputs.OnTrackOrderClick) },
                content = { SpanText(getString(Strings.TrackOrder)) },
            )
            AppTextButton(
                onClick = { vm.trySend(FooterContract.Inputs.OnShippingClick) },
                content = { SpanText(getString(Strings.Shipping)) },
            )
            AppTextButton(
                onClick = { vm.trySend(FooterContract.Inputs.OnReturnsClick) },
                content = { SpanText(getString(Strings.Returns)) },
            )
        } else {
            ShimmerFooterSection()
        }
    }
}

@Composable
private fun CompanySection(
    state: FooterContract.State,
    vm: FooterViewModel
) {
    FooterSection(
        isLoading = state.isLoading,
        title = getString(Strings.Company),
    ) {
        if (!state.isLoading) {
            AppTextButton(
                onClick = { vm.trySend(FooterContract.Inputs.OnPrivacyPolicyClicked) },
                content = { SpanText(getString(Strings.ContactUs)) },
            )
            AppTextButton(
                onClick = { vm.trySend(FooterContract.Inputs.OnTermsOfServiceClicked) },
                content = { SpanText(getString(Strings.AboutUs)) },
            )
            if (state.footerConfig?.showCareer == true) {
                AppTextButton(
                    onClick = { vm.trySend(FooterContract.Inputs.OnCareerClick) },
                    content = { SpanText(getString(Strings.Career)) },
                )
            }
            if (state.footerConfig?.showPress == true) {
                AppTextButton(
                    onClick = { vm.trySend(FooterContract.Inputs.OnPressClick) },
                    content = { SpanText(getString(Strings.Press)) },
                )
            }
            AppTextButton(
                onClick = { vm.trySend(FooterContract.Inputs.OnPrivacyPolicyClicked) },
                content = { SpanText(getString(Strings.PrivacyPolicy)) },
            )
            if (state.isAdmin) {
                AppTextButton(
                    onClick = { vm.trySend(FooterContract.Inputs.OnGoToAdminHome) },
                    content = { SpanText(getString(Strings.Admin)) },
                )
            }
        } else {
            ShimmerFooterSection()
        }
    }
}

@Composable
private fun FooterSection(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .position(Position.Relative)
            .gap(1.em)
    ) {
        if (isLoading) {
            ShimmerHeader()
        } else {
            SpanText(
                text = title.uppercase(),
                modifier = HeadlineStyle.toModifier(H3Variant)
                    .margin(top = 1.em, bottom = 0.25.em)
                    .color(ColorMode.current.opposite.toPalette().color)
            )
            AppDividerHorizontal(color = Colors.White)
            Box(Modifier.size(0.px))
            content()
        }
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
