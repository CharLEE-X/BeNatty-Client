package web.compose.material3.component

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Width
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import org.jetbrains.compose.web.attributes.AutoComplete
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.dom.Span
import org.w3c.dom.HTMLInputElement
import web.compose.material3.common.MdTagElement
import web.compose.material3.common.jsRequire
import web.compose.material3.common.slot

/**
 * [Documentation](https://github.com/material-components/material-web/blob/main/docs/components/text-field.md)
 * [All Tokens](https://github.com/material-components/material-web/blob/main/tokens/_md-comp-filled-text-field.scss)
 */
@Suppress("UnsafeCastFromDynamic")
@Composable
private fun MdTextFieldTagElement(
    modifier: Modifier,
    tagName: String,
    value: String,
    onInput: (String) -> Unit,
    label: String?,
    type: TextFieldType,
    placeholder: String?,
    leadingIcon: (@Composable () -> Unit)?,
    trailingIcon: (@Composable () -> Unit)?,
    required: Boolean,
    disabled: Boolean,
    autoComplete: AutoComplete?,
    error: Boolean,
    errorText: String?,
    prefixText: String?,
    suffixText: String?,
    rows: Int?,
    cols: Int?,
    max: String?,
    maxLength: Int?,
    min: String?,
    minLength: Int?,
    pattern: String?,
    readOnly: Boolean,
    multiple: Boolean,
    step: String?,
    hasLeadingIcon: Boolean,
    hasTrailingIcon: Boolean,
    supportingText: String?,
    textDirection: String?,
) {
    MdTagElement(
        tagName = "md-$tagName",
        applyAttrs = modifier
            .toAttrs {
                classes("md-text-field")

                prop({ element: HTMLInputElement, value: String -> element.value = value }, value)

                attr("type", type.value)
                label?.let { attr("label", it) }
                placeholder?.let { attr("placeholder", it) }
                if (hasLeadingIcon) attr("has-leading-icon", "")
                if (hasTrailingIcon) attr("has-trailing-icon", "")
                if (required) attr("required", "")
                if (error) attr("error", "")
                errorText?.let { attr("error-text", it) }
                prefixText?.let { attr("prefix-text", it) }
                suffixText?.let { attr("suffix-text", it) }
                if (disabled) attr("disabled", "")
                supportingText?.let { attr("supporting-text", it) }
                textDirection?.let { attr("text-direction", it) }
                rows?.let { attr("rows", it.toString()) }
                cols?.let { attr("cols", it.toString()) }
                max?.let { attr("max", it) }
                maxLength?.let { attr("max-length", it.toString()) }
                min?.let { attr("min", it) }
                minLength?.let { attr("min-length", it.toString()) }
                pattern?.let { attr("pattern", it) }
                if (readOnly) attr("readOnly", "")
                if (multiple) attr("multiple", "")
                step?.let { attr("step", it) }
                autoComplete?.let { attr("autocomplete", it.toString()) }

                addEventListener("input") {
                    onInput(it.nativeEvent.target.asDynamic().value.unsafeCast<String>())
                }
            },
    ) {
        leadingIcon?.let {
            Span({ slot = "leading-icon" }) { it() }
        }

        trailingIcon?.let {
            Span({ slot = "trailing-icon" }) { it() }
        }
    }.also { jsRequire("@material/web/textfield/$tagName.js") }
}

enum class TextFieldType(val value: String) {
    EMAIL("email"),
    NUMBER("number"),
    PASSWORD("password"),
    SEARCH("search"),
    TEL("tel"),
    TEXT("text"),
    URL("url"),
    TEXTAREA("textarea"),
}

@Composable
fun FilledTextField(
    modifier: Modifier = Modifier,
    value: String,
    onInput: (String) -> Unit,
    label: String? = null,
    type: TextFieldType = TextFieldType.TEXT,
    placeholder: String? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    required: Boolean = false,
    disabled: Boolean = false,
    autoComplete: AutoComplete = AutoComplete.off,
    error: Boolean = false,
    errorText: String? = null,
    prefixText: String? = null,
    suffixText: String? = null,
    rows: Int? = null,
    cols: Int? = null,
    max: String? = null,
    maxLength: Int? = null,
    min: String? = null,
    minLength: Int? = null,
    pattern: String? = null,
    readOnly: Boolean = false,
    multiple: Boolean = false,
    step: String? = null,
    hasLeadingIcon: Boolean = false,
    hasTrailingIcon: Boolean = false,
    supportingText: String? = null,
    textDirection: String? = null,

    activeIndicatorColor: CSSColorValue? = null,
    activeIndicatorHeight: String? = null,
    bottomSpace: CSSLengthOrPercentageNumericValue? = null,
    caretColor: CSSColorValue? = null,
    containerColor: CSSColorValue? = null,
    containerShape: CSSLengthOrPercentageNumericValue? = null,
    containerShapeEndEnd: CSSLengthOrPercentageNumericValue? = null,
    containerShapeEndStart: CSSLengthOrPercentageNumericValue? = null,
    containerShapeStartEnd: CSSLengthOrPercentageNumericValue? = null,
    containerShapeStartStart: CSSLengthOrPercentageNumericValue? = null,
    disabledActiveIndicatorColor: CSSColorValue? = null,
    disabledActiveIndicatorHeight: String? = null,
    disabledActiveIndicatorOpacity: String? = null,
    disabledContainerColor: CSSColorValue? = null,
    disabledContainerOpacity: String? = null,
    disabledInputTextColor: CSSColorValue? = null,
    disabledInputTextOpacity: CSSLengthOrPercentageNumericValue? = null,
    disabledLabelTextColor: CSSColorValue? = null,
    disabledLabelTextOpacity: CSSLengthOrPercentageNumericValue? = null,
    disabledLeadingIconColor: CSSColorValue? = null,
    disabledLeadingIconOpacity: CSSLengthOrPercentageNumericValue? = null,
    disabledSupportingTextColor: CSSColorValue? = null,
    disabledSupportingTextOpacity: CSSLengthOrPercentageNumericValue? = null,
    disabledTrailingIconColor: CSSColorValue? = null,
    disabledTrailingIconOpacity: CSSLengthOrPercentageNumericValue? = null,
    errorActiveIndicatorColor: CSSColorValue? = null,
    errorFocusActiveIndicatorColor: CSSColorValue? = null,
    errorFocusCaretColor: CSSColorValue? = null,
    errorFocusInputTextColor: CSSColorValue? = null,
    errorFocusLabelTextColor: CSSColorValue? = null,
    errorFocusLeadingIconColor: CSSColorValue? = null,
    errorFocusSupportingTextColor: CSSColorValue? = null,
    errorFocusTrailingIconColor: CSSColorValue? = null,
    errorHoverActiveIndicatorColor: CSSColorValue? = null,
    errorHoverInputTextColor: CSSColorValue? = null,
    errorHoverLabelTextColor: CSSColorValue? = null,
    errorHoverLeadingIconColor: CSSColorValue? = null,
    errorHoverStateLayerColor: CSSColorValue? = null,
    errorHoverStateLayerOpacity: String? = null,
    errorHoverSupportingTextColor: CSSColorValue? = null,
    errorHoverTrailingIconColor: CSSColorValue? = null,
    errorInputTextColor: CSSColorValue? = null,
    errorLabelTextColor: CSSColorValue? = null,
    errorLeadingIconColor: CSSColorValue? = null,
    errorSupportingTextColor: CSSColorValue? = null,
    errorTrailingIconColor: CSSColorValue? = null,
    focusActiveIndicatorColor: CSSColorValue? = null,
    focusActiveIndicatorHeight: String? = null,
    focusCaretColor: CSSColorValue? = null,
    focusInputTextColor: CSSColorValue? = null,
    focusLabelTextColor: CSSColorValue? = null,
    focusLeadingIconColor: CSSColorValue? = null,
    focusSupportingTextColor: CSSColorValue? = null,
    focusTrailingIconColor: CSSColorValue? = null,
    hoverActiveIndicatorColor: CSSColorValue? = null,
    hoverActiveIndicatorHeight: String? = null,
    hoverInputTextColor: CSSColorValue? = null,
    hoverLabelTextColor: CSSColorValue? = null,
    hoverLeadingIconColor: CSSColorValue? = null,
    hoverStateLayerColor: CSSColorValue? = null,
    hoverStateLayerOpacity: String? = null,
    hoverSupportingTextColor: CSSColorValue? = null,
    hoverTrailingIconColor: CSSColorValue? = null,
    inputTextColor: CSSColorValue? = null,
    inputTextFont: String? = null,
    inputTextLineHeight: CSSLengthOrPercentageNumericValue? = null,
    inputTextPlaceholderColor: CSSColorValue? = null,
    inputTextPrefixColor: CSSColorValue? = null,
    inputTextPrefixTrailingSpace: CSSLengthOrPercentageNumericValue? = null,
    inputTextSize: CSSLengthOrPercentageNumericValue? = null,
    inputTextSuffixColor: CSSColorValue? = null,
    inputTextSuffixLeadingSpace: CSSLengthOrPercentageNumericValue? = null,
    inputTextWeight: FontWeight? = null,
    labelTextColor: CSSColorValue? = null,
    labelTextFont: String? = null,
    labelTextLineHeight: CSSLengthOrPercentageNumericValue? = null,
    labelTextPopulatedLineHeight: CSSLengthOrPercentageNumericValue? = null,
    labelTextPopulatedSize: CSSLengthOrPercentageNumericValue? = null,
    labelTextSize: CSSLengthOrPercentageNumericValue? = null,
    labelTextWeight: FontWeight? = null,
    leadingIconColor: CSSColorValue? = null,
    leadingIconSize: CSSLengthOrPercentageNumericValue? = null,
    leadingSpace: CSSLengthOrPercentageNumericValue? = null,
    supportingTextColor: CSSColorValue? = null,
    supportingTextFont: String? = null,
    supportingTextLineHeight: CSSLengthOrPercentageNumericValue? = null,
    supportingTextSize: CSSLengthOrPercentageNumericValue? = null,
    supportingTextWeight: FontWeight? = null,
    topSpace: CSSLengthOrPercentageNumericValue? = null,
    trailingIconColor: CSSColorValue? = null,
    trailingIconSize: CSSLengthOrPercentageNumericValue? = null,
    trailingSpace: CSSLengthOrPercentageNumericValue? = null,
    withLabelBottomSpace: String? = null,
    withLabelTopSpace: String? = null,
) {
    val tag = "filled-text-field"
    MdTextFieldTagElement(
        tagName = tag,
        value = value,
        onInput = onInput,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        hasLeadingIcon = hasLeadingIcon,
        hasTrailingIcon = hasTrailingIcon,
        type = type,
        autoComplete = autoComplete,
        required = required,
        disabled = disabled,
        error = error,
        errorText = errorText,
        prefixText = prefixText,
        suffixText = suffixText,
        supportingText = supportingText,
        textDirection = textDirection,
        rows = rows,
        cols = cols,
        max = max,
        maxLength = maxLength,
        min = min,
        minLength = minLength,
        pattern = pattern,
        readOnly = readOnly,
        multiple = multiple,
        step = step,
        modifier = modifier.styleModifier {
            activeIndicatorColor?.let { property("--md-$tag-active-indicator-color", it.toString()) }
            activeIndicatorHeight?.let { property("--md-$tag-active-indicator-height", it) }
            bottomSpace?.let { property("--md-$tag-bottom-space", it.toString()) }
            caretColor?.let { property("--md-$tag-caret-color", it.toString()) }
            containerColor?.let { property("--md-$tag-container-color", it) }
            containerShape?.let { property("--md-$tag-container-shape", it.toString()) }
            containerShapeEndEnd?.let { property("--md-$tag-container-shape-end-end", it.toString()) }
            containerShapeEndStart?.let { property("--md-$tag-container-shape-end-start", it.toString()) }
            containerShapeStartEnd?.let { property("--md-$tag-container-shape-start-end", it.toString()) }
            containerShapeStartStart?.let { property("--md-$tag-container-shape-start-start", it.toString()) }
            disabledActiveIndicatorColor?.let { property("--md-$tag-disabled-active-indicator-color", it.toString()) }
            disabledActiveIndicatorHeight?.let { property("--md-$tag-disabled-active-indicator-height", it) }
            disabledActiveIndicatorOpacity?.let { property("--md-$tag-disabled-active-indicator-opacity", it) }
            disabledContainerColor?.let { property("--md-$tag-disabled-container-color", it.toString()) }
            disabledContainerOpacity?.let { property("--md-$tag-disabled-container-opacity", it) }
            disabledInputTextColor?.let { property("--md-$tag-disabled-input-text-color", it.toString()) }
            disabledInputTextOpacity?.let { property("--md-$tag-disabled-input-text-opacity", it.toString()) }
            disabledLabelTextColor?.let { property("--md-$tag-disabled-label-text-color", it.toString()) }
            disabledLabelTextOpacity?.let { property("--md-$tag-disabled-label-text-opacity", it.toString()) }
            disabledLeadingIconColor?.let { property("--md-$tag-disabled-leading-icon-color", it.toString()) }
            disabledLeadingIconOpacity?.let { property("--md-$tag-disabled-leading-icon-opacity", it.toString()) }
            disabledSupportingTextColor?.let { property("--md-$tag-disabled-supporting-text-color", it.toString()) }
            disabledSupportingTextOpacity?.let { property("--md-$tag-disabled-supporting-text-opacity", it.toString()) }
            disabledTrailingIconColor?.let { property("--md-$tag-disabled-trailing-icon-color", it.toString()) }
            disabledTrailingIconOpacity?.let { property("--md-$tag-disabled-trailing-icon-opacity", it.toString()) }
            errorActiveIndicatorColor?.let { property("--md-$tag-error-active-indicator-color", it.toString()) }
            errorFocusActiveIndicatorColor?.let { property("--md-$tag-error-focus-active-indicator-color", it) }
            errorFocusCaretColor?.let { property("--md-$tag-error-focus-caret-color", it.toString()) }
            errorFocusInputTextColor?.let { property("--md-$tag-error-focus-input-text-color", it.toString()) }
            errorFocusLabelTextColor?.let { property("--md-$tag-error-focus-label-text-color", it.toString()) }
            errorFocusLeadingIconColor?.let { property("--md-$tag-error-focus-leading-icon-color", it.toString()) }
            errorFocusSupportingTextColor?.let {
                property(
                    "--md-$tag-error-focus-supporting-text-color",
                    it.toString()
                )
            }
            errorFocusTrailingIconColor?.let { property("--md-$tag-error-focus-trailing-icon-color", it.toString()) }
            errorHoverActiveIndicatorColor?.let {
                property(
                    "--md-$tag-error-hover-active-indicator-color",
                    it.toString()
                )
            }
            errorHoverInputTextColor?.let { property("--md-$tag-error-hover-input-text-color", it.toString()) }
            errorHoverLabelTextColor?.let { property("--md-$tag-error-hover-label-text-color", it.toString()) }
            errorHoverLeadingIconColor?.let { property("--md-$tag-error-hover-leading-icon-color", it.toString()) }
            errorHoverStateLayerColor?.let { property("--md-$tag-error-hover-state-layer-color", it.toString()) }
            errorHoverStateLayerOpacity?.let { property("--md-$tag-error-hover-state-layer-opacity", it) }
            errorHoverSupportingTextColor?.let {
                property(
                    "--md-$tag-error-hover-supporting-text-color",
                    it.toString()
                )
            }
            errorHoverTrailingIconColor?.let { property("--md-$tag-error-hover-trailing-icon-color", it.toString()) }
            errorInputTextColor?.let { property("--md-$tag-error-input-text-color", it.toString()) }
            errorLabelTextColor?.let { property("--md-$tag-error-label-text-color", it.toString()) }
            errorLeadingIconColor?.let { property("--md-$tag-error-leading-icon-color", it.toString()) }
            errorSupportingTextColor?.let { property("--md-$tag-error-supporting-text-color", it.toString()) }
            errorTrailingIconColor?.let { property("--md-$tag-error-trailing-icon-color", it.toString()) }
            focusActiveIndicatorColor?.let { property("--md-$tag-focus-active-indicator-color", it.toString()) }
            focusActiveIndicatorHeight?.let { property("--md-$tag-focus-active-indicator-height", it) }
            focusCaretColor?.let { property("--md-$tag-focus-caret-color", it.toString()) }
            focusInputTextColor?.let { property("--md-$tag-focus-input-text-color", it.toString()) }
            focusLabelTextColor?.let { property("--md-$tag-focus-label-text-color", it.toString()) }
            focusLeadingIconColor?.let { property("--md-$tag-focus-leading-icon-color", it.toString()) }
            focusSupportingTextColor?.let { property("--md-$tag-focus-supporting-text-color", it.toString()) }
            focusTrailingIconColor?.let { property("--md-$tag-focus-trailing-icon-color", it.toString()) }
            hoverActiveIndicatorColor?.let { property("--md-$tag-hover-active-indicator-color", it.toString()) }
            hoverActiveIndicatorHeight?.let { property("--md-$tag-hover-active-indicator-height", it) }
            hoverInputTextColor?.let { property("--md-$tag-hover-input-text-color", it.toString()) }
            hoverLabelTextColor?.let { property("--md-$tag-hover-label-text-color", it.toString()) }
            hoverLeadingIconColor?.let { property("--md-$tag-hover-leading-icon-color", it.toString()) }
            hoverStateLayerColor?.let { property("--md-$tag-hover-state-layer-color", it.toString()) }
            hoverStateLayerOpacity?.let { property("--md-$tag-hover-state-layer-opacity", it) }
            hoverSupportingTextColor?.let { property("--md-$tag-hover-supporting-text-color", it.toString()) }
            hoverTrailingIconColor?.let { property("--md-$tag-hover-trailing-icon-color", it.toString()) }
            inputTextColor?.let { property("--md-$tag-input-text-color", it.toString()) }
            inputTextFont?.let { property("--md-$tag-input-text-font", it) }
            inputTextLineHeight?.let { property("--md-$tag-input-text-line-height", it) }
            inputTextPlaceholderColor?.let { property("--md-$tag-input-text-placeholder-color", it.toString()) }
            inputTextPrefixColor?.let { property("--md-$tag-input-text-prefix-color", it.toString()) }
            inputTextPrefixTrailingSpace?.let { property("--md-$tag-input-text-prefix-trailing-space", it.toString()) }
            inputTextSize?.let { property("--md-$tag-input-text-size", it.toString()) }
            inputTextSuffixColor?.let { property("--md-$tag-input-text-suffix-color", it.toString()) }
            inputTextSuffixLeadingSpace?.let { property("--md-$tag-input-text-suffix-leading-space", it.toString()) }
            inputTextWeight?.let { property("--md-$tag-input-text-weight", it.toString()) }
            labelTextColor?.let { property("--md-$tag-label-text-color", it.toString()) }
            labelTextFont?.let { property("--md-$tag-label-text-font", it) }
            labelTextLineHeight?.let { property("--md-$tag-label-text-line-height", it) }
            labelTextPopulatedLineHeight?.let { property("--md-$tag-label-text-populated-line-height", it) }
            labelTextPopulatedSize?.let { property("--md-$tag-label-text-populated-size", it.toString()) }
            labelTextSize?.let { property("--md-$tag-label-text-size", it.toString()) }
            labelTextWeight?.let { property("--md-$tag-label-text-weight", it.toString()) }
            leadingIconColor?.let { property("--md-$tag-leading-icon-color", it.toString()) }
            leadingIconSize?.let { property("--md-$tag-leading-icon-size", it.toString()) }
            leadingSpace?.let { property("--md-$tag-leading-space", it.toString()) }
            supportingTextColor?.let { property("--md-$tag-supporting-text-color", it.toString()) }
            supportingTextFont?.let { property("--md-$tag-supporting-text-font", it) }
            supportingTextLineHeight?.let { property("--md-$tag-supporting-text-line-height", it) }
            supportingTextSize?.let { property("--md-$tag-supporting-text-size", it.toString()) }
            supportingTextWeight?.let { property("--md-$tag-supporting-text-weight", it.toString()) }
            topSpace?.let { property("--md-$tag-top-space", it.toString()) }
            trailingIconColor?.let { property("--md-$tag-trailing-icon-color", it.toString()) }
            trailingIconSize?.let { property("--md-$tag-trailing-icon-size", it.toString()) }
            trailingSpace?.let { property("--md-$tag-trailing-space", it.toString()) }
            withLabelBottomSpace?.let { property("--md-$tag-with-label-bottom-space", it) }
            withLabelTopSpace?.let { property("--md-$tag-with-label-top-space", it) }
        }
    )
}

@Composable
fun OutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    onInput: (String) -> Unit,
    label: String? = null,
    type: TextFieldType = TextFieldType.TEXT,
    placeholder: String? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    required: Boolean = false,
    disabled: Boolean = false,
    autoComplete: AutoComplete = AutoComplete.off,
    error: Boolean = false,
    errorText: String? = null,
    prefixText: String? = null,
    suffixText: String? = null,
    rows: Int? = null,
    cols: Int? = null,
    max: String? = null,
    maxLength: Int? = null,
    min: String? = null,
    minLength: Int? = null,
    pattern: String? = null,
    readOnly: Boolean = false,
    multiple: Boolean = false,
    step: String? = null,
    hasLeadingIcon: Boolean = false,
    hasTrailingIcon: Boolean = false,
    supportingText: String? = null,
    textDirection: String? = null,

    bottomSpace: CSSLengthOrPercentageNumericValue? = null,
    caretColor: CSSColorValue? = null,
    containerShape: CSSLengthOrPercentageNumericValue? = null,
    containerShapeEndEnd: CSSLengthOrPercentageNumericValue? = null,
    containerShapeEndStart: CSSLengthOrPercentageNumericValue? = null,
    containerShapeStartEnd: CSSLengthOrPercentageNumericValue? = null,
    containerShapeStartStart: CSSLengthOrPercentageNumericValue? = null,
    disabledInputTextColor: CSSColorValue? = null,
    disabledInputTextOpacity: CSSLengthOrPercentageNumericValue? = null,
    disabledLabelTextColor: CSSColorValue? = null,
    disabledLabelTextOpacity: CSSLengthOrPercentageNumericValue? = null,
    disabledLeadingIconColor: CSSColorValue? = null,
    disabledLeadingIconOpacity: CSSLengthOrPercentageNumericValue? = null,
    disabledOutlineColor: CSSColorValue? = null,
    disabledOutlineOpacity: CSSLengthOrPercentageNumericValue? = null,
    disabledOutlineWidth: CSSLengthOrPercentageNumericValue? = null,
    disabledSupportingTextColor: CSSColorValue? = null,
    disabledSupportingTextOpacity: CSSLengthOrPercentageNumericValue? = null,
    disabledTrailingIconColor: CSSColorValue? = null,
    disabledTrailingIconOpacity: CSSLengthOrPercentageNumericValue? = null,
    errorFocusCaretColor: CSSColorValue? = null,
    errorFocusInputTextColor: CSSColorValue? = null,
    errorFocusLabelTextColor: CSSColorValue? = null,
    errorFocusLeadingIconColor: CSSColorValue? = null,
    errorFocusOutlineColor: CSSColorValue? = null,
    errorFocusSupportingTextColor: CSSColorValue? = null,
    errorFocusTrailingIconColor: CSSColorValue? = null,
    errorHoverInputTextColor: CSSColorValue? = null,
    errorHoverLabelTextColor: CSSColorValue? = null,
    errorHoverLeadingIconColor: CSSColorValue? = null,
    errorHoverOutlineColor: CSSColorValue? = null,
    errorHoverSupportingTextColor: CSSColorValue? = null,
    errorHoverTrailingIconColor: CSSColorValue? = null,
    errorInputTextColor: CSSColorValue? = null,
    errorLabelTextColor: CSSColorValue? = null,
    errorLeadingIconColor: CSSColorValue? = null,
    errorOutlineColor: CSSColorValue? = null,
    errorSupportingTextColor: CSSColorValue? = null,
    errorTrailingIconColor: CSSColorValue? = null,
    focusCaretColor: CSSColorValue? = null,
    focusInputTextColor: CSSColorValue? = null,
    focusLabelTextColor: CSSColorValue? = null,
    focusLeadingIconColor: CSSColorValue? = null,
    focusOutlineColor: CSSColorValue? = null,
    focusOutlineWidth: Width? = null,
    focusSupportingTextColor: CSSColorValue? = null,
    focusTrailingIconColor: CSSColorValue? = null,
    hoverInputTextColor: CSSColorValue? = null,
    hoverLabelTextColor: CSSColorValue? = null,
    hoverLeadingIconColor: CSSColorValue? = null,
    hoverOutlineColor: CSSColorValue? = null,
    hoverOutlineWidth: Width? = null,
    hoverSupportingTextColor: CSSColorValue? = null,
    hoverTrailingIconColor: CSSColorValue? = null,
    inputTextColor: CSSColorValue? = null,
    inputTextFont: String? = null,
    inputTextLineHeight: CSSLengthOrPercentageNumericValue? = null,
    inputTextPlaceholderColor: CSSColorValue? = null,
    inputTextPrefixColor: CSSColorValue? = null,
    inputTextPrefixTrailingSpace: CSSLengthOrPercentageNumericValue? = null,
    inputTextSize: CSSLengthOrPercentageNumericValue? = null,
    inputTextSuffixColor: CSSColorValue? = null,
    inputTextSuffixLeadingSpace: CSSLengthOrPercentageNumericValue? = null,
    inputTextWeight: FontWeight? = null,
    labelTextColor: CSSColorValue? = null,
    labelTextFont: String? = null,
    labelTextLineHeight: CSSLengthOrPercentageNumericValue? = null,
    labelTextPopulatedLineHeight: CSSLengthOrPercentageNumericValue? = null,
    labelTextPopulatedSize: CSSLengthOrPercentageNumericValue? = null,
    labelTextSize: CSSLengthOrPercentageNumericValue? = null,
    labelTextWeight: FontWeight? = null,
    leadingIconColor: CSSColorValue? = null,
    leadingIconSize: CSSLengthOrPercentageNumericValue? = null,
    leadingSpace: CSSLengthOrPercentageNumericValue? = null,
    outlineColor: CSSColorValue? = null,
    outlineWidth: Width? = null,
    supportingTextColor: CSSColorValue? = null,
    supportingTextFont: String? = null,
    supportingTextLineHeight: CSSLengthOrPercentageNumericValue? = null,
    supportingTextSize: CSSLengthOrPercentageNumericValue? = null,
    supportingTextWeight: FontWeight? = null,
    topSpace: CSSLengthOrPercentageNumericValue? = null,
    trailingIconColor: CSSColorValue? = null,
    trailingIconSize: CSSLengthOrPercentageNumericValue? = null,
    trailingSpace: CSSLengthOrPercentageNumericValue? = null,
) {
    val tagName = "outlined-text-field"
    MdTextFieldTagElement(
        tagName = tagName,
        value = value,
        onInput = onInput,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        hasLeadingIcon = hasLeadingIcon,
        hasTrailingIcon = hasTrailingIcon,
        type = type,
        autoComplete = autoComplete,
        required = required,
        disabled = disabled,
        error = error,
        errorText = errorText,
        prefixText = prefixText,
        suffixText = suffixText,
        supportingText = supportingText,
        textDirection = textDirection,
        rows = rows,
        cols = cols,
        max = max,
        maxLength = maxLength,
        min = min,
        minLength = minLength,
        pattern = pattern,
        readOnly = readOnly,
        multiple = multiple,
        step = step,
        modifier = modifier.styleModifier {
            bottomSpace?.let { property("--md-$tagName-bottom-space", it.toString()) }
            caretColor?.let { property("--md-$tagName-caret-color", it.toString()) }
            containerShape?.let { property("--md-$tagName-container-shape", it.toString()) }
            containerShapeEndEnd?.let { property("--md-$tagName-container-shape-end-end", it.toString()) }
            containerShapeEndStart?.let { property("--md-$tagName-container-shape-end-start", it.toString()) }
            containerShapeStartEnd?.let { property("--md-$tagName-container-shape-start-end", it.toString()) }
            containerShapeStartStart?.let { property("--md-$tagName-container-shape-start-start", it.toString()) }
            disabledInputTextColor?.let { property("--md-$tagName-disabled-input-text-color", it.toString()) }
            disabledInputTextOpacity?.let { property("--md-$tagName-disabled-input-text-opacity", it.toString()) }
            disabledLabelTextColor?.let { property("--md-$tagName-disabled-label-text-color", it.toString()) }
            disabledLabelTextOpacity?.let { property("--md-$tagName-disabled-label-text-opacity", it.toString()) }
            disabledLeadingIconColor?.let { property("--md-$tagName-disabled-leading-icon-color", it.toString()) }
            disabledLeadingIconOpacity?.let { property("--md-$tagName-disabled-leading-icon-opacity", it.toString()) }
            disabledOutlineColor?.let { property("--md-$tagName-disabled-outline-color", it.toString()) }
            disabledOutlineOpacity?.let { property("--md-$tagName-disabled-outline-opacity", it.toString()) }
            disabledOutlineWidth?.let { property("--md-$tagName-disabled-outline-width", it.toString()) }
            disabledSupportingTextColor?.let { property("--md-$tagName-disabled-supporting-text-color", it.toString()) }
            disabledSupportingTextOpacity?.let {
                property(
                    "--md-$tagName-disabled-supporting-text-opacity",
                    it.toString()
                )
            }
            disabledTrailingIconColor?.let { property("--md-$tagName-disabled-trailing-icon-color", it.toString()) }
            disabledTrailingIconOpacity?.let { property("--md-$tagName-disabled-trailing-icon-opacity", it.toString()) }
            errorFocusCaretColor?.let { property("--md-$tagName-error-focus-caret-color", it.toString()) }
            errorFocusInputTextColor?.let { property("--md-$tagName-error-focus-input-text-color", it.toString()) }
            errorFocusLabelTextColor?.let { property("--md-$tagName-error-focus-label-text-color", it.toString()) }
            errorFocusLeadingIconColor?.let { property("--md-$tagName-error-focus-leading-icon-color", it.toString()) }
            errorFocusOutlineColor?.let { property("--md-$tagName-error-focus-outline-color", it.toString()) }
            errorFocusSupportingTextColor?.let {
                property(
                    "--md-$tagName-error-focus-supporting-text-color",
                    it.toString()
                )
            }
            errorFocusTrailingIconColor?.let {
                property(
                    "--md-$tagName-error-focus-trailing-icon-color",
                    it.toString()
                )
            }
            errorHoverInputTextColor?.let { property("--md-$tagName-error-hover-input-text-color", it.toString()) }
            errorHoverLabelTextColor?.let { property("--md-$tagName-error-hover-label-text-color", it.toString()) }
            errorHoverLeadingIconColor?.let { property("--md-$tagName-error-hover-leading-icon-color", it.toString()) }
            errorHoverOutlineColor?.let { property("--md-$tagName-error-hover-outline-color", it.toString()) }
            errorHoverSupportingTextColor?.let {
                property(
                    "--md-$tagName-error-hover-supporting-text-color",
                    it.toString()
                )
            }
            errorHoverTrailingIconColor?.let {
                property(
                    "--md-$tagName-error-hover-trailing-icon-color",
                    it.toString()
                )
            }
            errorInputTextColor?.let { property("--md-$tagName-error-input-text-color", it.toString()) }
            errorLabelTextColor?.let { property("--md-$tagName-error-label-text-color", it.toString()) }
            errorLeadingIconColor?.let { property("--md-$tagName-error-leading-icon-color", it.toString()) }
            errorOutlineColor?.let { property("--md-$tagName-error-outline-color", it.toString()) }
            errorSupportingTextColor?.let { property("--md-$tagName-error-supporting-text-color", it.toString()) }
            errorTrailingIconColor?.let { property("--md-$tagName-error-trailing-icon-color", it.toString()) }
            focusCaretColor?.let { property("--md-$tagName-focus-caret-color", it.toString()) }
            focusInputTextColor?.let { property("--md-$tagName-focus-input-text-color", it.toString()) }
            focusLabelTextColor?.let { property("--md-$tagName-focus-label-text-color", it.toString()) }
            focusLeadingIconColor?.let { property("--md-$tagName-focus-leading-icon-color", it.toString()) }
            focusOutlineColor?.let { property("--md-$tagName-focus-outline-color", it.toString()) }
            focusOutlineWidth?.let { property("--md-$tagName-focus-outline-width", it.toString()) }
            focusSupportingTextColor?.let { property("--md-$tagName-focus-supporting-text-color", it.toString()) }
            focusTrailingIconColor?.let { property("--md-$tagName-focus-trailing-icon-color", it.toString()) }
            hoverInputTextColor?.let { property("--md-$tagName-hover-input-text-color", it.toString()) }
            hoverLabelTextColor?.let { property("--md-$tagName-hover-label-text-color", it.toString()) }
            hoverLeadingIconColor?.let { property("--md-$tagName-hover-leading-icon-color", it.toString()) }
            hoverOutlineColor?.let { property("--md-$tagName-hover-outline-color", it.toString()) }
            hoverOutlineWidth?.let { property("--md-$tagName-hover-outline-width", it.toString()) }
            hoverSupportingTextColor?.let { property("--md-$tagName-hover-supporting-text-color", it.toString()) }
            hoverTrailingIconColor?.let { property("--md-$tagName-hover-trailing-icon-color", it.toString()) }
            inputTextColor?.let { property("--md-$tagName-input-text-color", it.toString()) }
            inputTextFont?.let { property("--md-$tagName-input-text-font", it) }
            inputTextLineHeight?.let { property("--md-$tagName-input-text-line-height", it.toString()) }
            inputTextPlaceholderColor?.let { property("--md-$tagName-input-text-placeholder-color", it.toString()) }
            inputTextPrefixColor?.let { property("--md-$tagName-input-text-prefix-color", it.toString()) }
            inputTextPrefixTrailingSpace?.let {
                property(
                    "--md-$tagName-input-text-prefix-trailing-space",
                    it.toString()
                )
            }
            inputTextSize?.let { property("--md-$tagName-input-text-size", it.toString()) }
            inputTextSuffixColor?.let { property("--md-$tagName-input-text-suffix-color", it.toString()) }
            inputTextSuffixLeadingSpace?.let {
                property(
                    "--md-$tagName-input-text-suffix-leading-space",
                    it.toString()
                )
            }
            inputTextWeight?.let { property("--md-$tagName-input-text-weight", it.toString()) }
            labelTextColor?.let { property("--md-$tagName-label-text-color", it.toString()) }
            labelTextFont?.let { property("--md-$tagName-label-text-font", it) }
            labelTextLineHeight?.let { property("--md-$tagName-label-text-line-height", it.toString()) }
            labelTextPopulatedLineHeight?.let {
                property(
                    "--md-$tagName-label-text-populated-line-height",
                    it.toString()
                )
            }
            labelTextPopulatedSize?.let { property("--md-$tagName-label-text-populated-size", it.toString()) }
            labelTextSize?.let { property("--md-$tagName-label-text-size", it.toString()) }
            labelTextWeight?.let { property("--md-$tagName-label-text-weight", it.toString()) }
            leadingIconColor?.let { property("--md-$tagName-leading-icon-color", it.toString()) }
            leadingIconSize?.let { property("--md-$tagName-leading-icon-size", it.toString()) }
            leadingSpace?.let { property("--md-$tagName-leading-space", it.toString()) }
            outlineColor?.let { property("--md-$tagName-outline-color", it.toString()) }
            outlineWidth?.let { property("--md-$tagName-outline-width", it.toString()) }
            supportingTextColor?.let { property("--md-$tagName-supporting-text-color", it.toString()) }
            supportingTextFont?.let { property("--md-$tagName-supporting-text-font", it) }
            supportingTextLineHeight?.let { property("--md-$tagName-supporting-text-line-height", it.toString()) }
            supportingTextSize?.let { property("--md-$tagName-supporting-text-size", it.toString()) }
            supportingTextWeight?.let { property("--md-$tagName-supporting-text-weight", it.toString()) }
            topSpace?.let { property("--md-$tagName-top-space", it.toString()) }
            trailingIconColor?.let { property("--md-$tagName-trailing-icon-color", it.toString()) }
            trailingIconSize?.let { property("--md-$tagName-trailing-icon-size", it.toString()) }
            trailingSpace?.let { property("--md-$tagName-trailing-space", it.toString()) }
        }
    )
}
