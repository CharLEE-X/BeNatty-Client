package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.ui.Modifier
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.ContentBuilder
import web.compose.material3.common.MdElement
import web.compose.material3.component.labs.OutlinedSegmentedButtonSet
import web.compose.material3.component.labs.SegmentedButton

@Composable
fun AppOutlinedSegmentedButtonSet(
    modifier: Modifier = Modifier,
    containerHeight: CSSLengthOrPercentageNumericValue? = null,
    disabledIconColor: CSSColorValue? = null,
    disabledLabelTextColor: CSSColorValue? = null,
    disabledOutlineColor: CSSColorValue? = null,
    hoverStateLayerOpacity: CSSLengthOrPercentageNumericValue? = null,
    labelTextFont: String? = null,
    labelTextLineHeight: Height? = null,
    labelTextSize: CSSLengthOrPercentageNumericValue? = null,
    labelTextWeight: FontWeight? = null,
    outlineColor: CSSColorValue? = null,
    pressedStateLayerOpacity: CSSLengthOrPercentageNumericValue? = null,
    selectedContainerColor: CSSColorValue? = null,
    selectedFocusIconColor: CSSColorValue? = null,
    selectedFocusLabelTextColor: CSSColorValue? = null,
    selectedHoverIconColor: CSSColorValue? = null,
    selectedHoverLabelTextColor: CSSColorValue? = null,
    selectedHoverStateLayerColor: CSSColorValue? = null,
    selectedLabelTextColor: CSSColorValue? = null,
    selectedPressedIconColor: CSSColorValue? = null,
    selectedPressedLabelTextColor: CSSColorValue? = null,
    selectedPressedStateLayerColor: CSSColorValue? = null,
    shape: CSSLengthOrPercentageNumericValue? = 12.px,
    unselectedFocusIconColor: CSSColorValue? = null,
    unselectedFocusLabelTextColor: CSSColorValue? = null,
    unselectedHoverIconColor: CSSColorValue? = null,
    unselectedHoverLabelTextColor: CSSColorValue? = null,
    unselectedHoverStateLayerColor: CSSColorValue? = null,
    unselectedLabelTextColor: CSSColorValue? = null,
    unselectedPressedIconColor: CSSColorValue? = null,
    unselectedPressedLabelTextColor: CSSColorValue? = null,
    unselectedPressedStateLayerColor: CSSColorValue? = null,
    iconSize: CSSLengthOrPercentageNumericValue? = null,
    selectedIconColor: CSSColorValue? = null,
    unselectedIconColor: CSSColorValue? = null,
    shapeStartStart: CSSLengthOrPercentageNumericValue? = null,
    shapeStartEnd: CSSLengthOrPercentageNumericValue? = null,
    shapeEndEnd: CSSLengthOrPercentageNumericValue? = null,
    shapeEndStart: CSSLengthOrPercentageNumericValue? = null,
    content: ContentBuilder<MdElement>? = null
) {
    OutlinedSegmentedButtonSet(
        modifier = modifier,
        containerHeight = containerHeight,
        disabledIconColor = disabledIconColor,
        disabledLabelTextColor = disabledLabelTextColor,
        disabledOutlineColor = disabledOutlineColor,
        hoverStateLayerOpacity = hoverStateLayerOpacity,
        labelTextFont = labelTextFont,
        labelTextLineHeight = labelTextLineHeight,
        labelTextSize = labelTextSize,
        labelTextWeight = labelTextWeight,
        outlineColor = outlineColor,
        pressedStateLayerOpacity = pressedStateLayerOpacity,
        selectedContainerColor = selectedContainerColor,
        selectedFocusIconColor = selectedFocusIconColor,
        selectedFocusLabelTextColor = selectedFocusLabelTextColor,
        selectedHoverIconColor = selectedHoverIconColor,
        selectedHoverLabelTextColor = selectedHoverLabelTextColor,
        selectedHoverStateLayerColor = selectedHoverStateLayerColor,
        selectedLabelTextColor = selectedLabelTextColor,
        selectedPressedIconColor = selectedPressedIconColor,
        selectedPressedLabelTextColor = selectedPressedLabelTextColor,
        selectedPressedStateLayerColor = selectedPressedStateLayerColor,
        shape = shape,
        unselectedFocusIconColor = unselectedFocusIconColor,
        unselectedFocusLabelTextColor = unselectedFocusLabelTextColor,
        unselectedHoverIconColor = unselectedHoverIconColor,
        unselectedHoverLabelTextColor = unselectedHoverLabelTextColor,
        unselectedHoverStateLayerColor = unselectedHoverStateLayerColor,
        unselectedLabelTextColor = unselectedLabelTextColor,
        unselectedPressedIconColor = unselectedPressedIconColor,
        unselectedPressedLabelTextColor = unselectedPressedLabelTextColor,
        unselectedPressedStateLayerColor = unselectedPressedStateLayerColor,
        iconSize = iconSize,
        selectedIconColor = selectedIconColor,
        unselectedIconColor = unselectedIconColor,
        shapeStartStart = shapeStartStart,
        shapeStartEnd = shapeStartEnd,
        shapeEndEnd = shapeEndEnd,
        shapeEndStart = shapeEndStart,
        content = content
    )
}

@Composable
fun AppSegmentedButton(
    label: String? = null,
    selected: Boolean = false,
    onClick: () -> Unit = {},
    containerHeight: Height? = null,
    disabledIconColor: CSSColorValue? = null,
    disabledLabelTextColor: CSSColorValue? = null,
    disabledOutlineColor: CSSColorValue? = null,
    hoverStateLayerOpacity: CSSLengthOrPercentageNumericValue? = null,
    labelTextFont: String? = null,
    labelTextLineHeight: CSSLengthOrPercentageNumericValue? = null,
    labelTextSize: CSSLengthOrPercentageNumericValue? = null,
    labelTextWeight: Height? = null,
    outlineColor: CSSColorValue? = null,
    pressedStateLayerOpacity: CSSLengthOrPercentageNumericValue? = null,
    selectedContainerColor: CSSColorValue? = null,
    selectedFocusIconColor: CSSColorValue? = null,
    selectedFocusLabelTextColor: CSSColorValue? = null,
    selectedHoverIconColor: CSSColorValue? = null,
    selectedHoverLabelTextColor: CSSColorValue? = null,
    selectedHoverStateLayerColor: CSSColorValue? = null,
    selectedLabelTextColor: CSSColorValue? = null,
    selectedPressedIconColor: CSSColorValue? = null,
    selectedPressedLabelTextColor: CSSColorValue? = null,
    selectedPressedStateLayerColor: CSSColorValue? = null,
    shape: CSSLengthOrPercentageNumericValue? = 12.px,
    unselectedFocusIconColor: CSSColorValue? = null,
    unselectedFocusLabelTextColor: CSSColorValue? = null,
    unselectedHoverIconColor: CSSColorValue? = null,
    unselectedHoverLabelTextColor: CSSColorValue? = null,
    unselectedHoverStateLayerColor: CSSColorValue? = null,
    unselectedLabelTextColor: CSSColorValue? = null,
    unselectedPressedIconColor: CSSColorValue? = null,
    unselectedPressedLabelTextColor: CSSColorValue? = null,
    unselectedPressedStateLayerColor: CSSColorValue? = null,
    iconSize: CSSLengthOrPercentageNumericValue? = null,
    selectedIconColor: CSSColorValue? = null,
    unselectedIconColor: CSSColorValue? = null,
    spacingLeading: CSSLengthOrPercentageNumericValue? = null,
    spacingTrailing: CSSLengthOrPercentageNumericValue? = null,
    modifier: Modifier = Modifier,
) {
    SegmentedButton(
        label = label,
        selected = selected,
        onClick = onClick,
        containerHeight = containerHeight,
        disabledIconColor = disabledIconColor,
        disabledLabelTextColor = disabledLabelTextColor,
        disabledOutlineColor = disabledOutlineColor,
        hoverStateLayerOpacity = hoverStateLayerOpacity,
        labelTextFont = labelTextFont,
        labelTextLineHeight = labelTextLineHeight,
        labelTextSize = labelTextSize,
        labelTextWeight = labelTextWeight,
        outlineColor = outlineColor,
        pressedStateLayerOpacity = pressedStateLayerOpacity,
        selectedContainerColor = selectedContainerColor,
        selectedFocusIconColor = selectedFocusIconColor,
        selectedFocusLabelTextColor = selectedFocusLabelTextColor,
        selectedHoverIconColor = selectedHoverIconColor,
        selectedHoverLabelTextColor = selectedHoverLabelTextColor,
        selectedHoverStateLayerColor = selectedHoverStateLayerColor,
        selectedLabelTextColor = selectedLabelTextColor,
        selectedPressedIconColor = selectedPressedIconColor,
        selectedPressedLabelTextColor = selectedPressedLabelTextColor,
        selectedPressedStateLayerColor = selectedPressedStateLayerColor,
        shape = shape,
        unselectedFocusIconColor = unselectedFocusIconColor,
        unselectedFocusLabelTextColor = unselectedFocusLabelTextColor,
        unselectedHoverIconColor = unselectedHoverIconColor,
        unselectedHoverLabelTextColor = unselectedHoverLabelTextColor,
        unselectedHoverStateLayerColor = unselectedHoverStateLayerColor,
        unselectedLabelTextColor = unselectedLabelTextColor,
        unselectedPressedIconColor = unselectedPressedIconColor,
        unselectedPressedLabelTextColor = unselectedPressedLabelTextColor,
        unselectedPressedStateLayerColor = unselectedPressedStateLayerColor,
        iconSize = iconSize,
        selectedIconColor = selectedIconColor,
        unselectedIconColor = unselectedIconColor,
        spacingLeading = spacingLeading,
        spacingTrailing = spacingTrailing,
        modifier = modifier,
    )
}