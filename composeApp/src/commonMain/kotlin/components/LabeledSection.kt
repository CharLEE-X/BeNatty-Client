package components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import component.localization.Strings
import component.localization.getString

@Composable
internal fun LabeledSection(
    title: String,
    bgColor: Color = MaterialTheme.colors.background,
    verticalPadding: Dp = 16.dp,
    extras: @Composable RowScope.() -> Unit = {},
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val borderColor by animateColorAsState(
        if (MaterialTheme.colors.isLight) Color.LightGray else Color.DarkGray,
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .animateContentSize(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                color = MaterialTheme.colors.onBackground,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(
                        horizontal = 16.dp,
                        vertical = 8.dp,
                    )
                    .weight(1f),
            )
            extras()
        }
        Surface(
            color = bgColor,
            shape = RoundedCornerShape(30.dp),
            border = BorderStroke(
                width = 1.dp,
                color = borderColor,
            ),
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = verticalPadding,
                    ),
            ) {
                content()
            }
        }
    }
}

@Composable
private fun LabeledSectionItemLayout(
    modifier: Modifier = Modifier,
    title: String,
    icon: @Composable (() -> Unit)? = null,
    color: Color = MaterialTheme.colors.onBackground,
    fontSize: TextUnit = 16.sp,
    content: @Composable RowScope.() -> Unit,
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
        ) {
            icon?.let { icon() }
            Text(
                text = title,
                color = color,
                fontSize = fontSize,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .weight(1f),
            )
            content()
        }
    }
}

@Composable
internal fun SwitchSectionItem(
    modifier: Modifier = Modifier,
    title: String,
    value: Boolean,
    onValueChange: (Boolean) -> Unit,
    textColor: Color = MaterialTheme.colors.onBackground,
    fontSize: TextUnit = 16.sp,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }

    LabeledSectionItemLayout(
        modifier = modifier,
        title = title,
        color = textColor,
        fontSize = fontSize,
    ) {
        Switch(
            checked = value,
            onCheckedChange = onValueChange,
            enabled = enabled,
            interactionSource = interactionSource,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colors.background,
                checkedTrackColor = MaterialTheme.colors.onBackground,
                uncheckedThumbColor = MaterialTheme.colors.background,
                uncheckedTrackColor = MaterialTheme.colors.onBackground.copy(alpha = 0.2f),
            ),
        )
    }
}

@Composable
internal fun CheckBoxSectionItem(
    title: String,
    value: Boolean,
    onValueChange: (Boolean) -> Unit,
    icon: @Composable (() -> Unit)? = null,
    color: Color = MaterialTheme.colors.onBackground,
    fontSize: TextUnit = 16.sp,
) {
    LabeledSectionItemLayout(
        title = title,
        icon = icon,
        color = color,
        fontSize = fontSize,
    ) {
        Checkbox(
            checked = value,
            onCheckedChange = onValueChange,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colors.primary,
                uncheckedColor = MaterialTheme.colors.onSurface.copy(alpha = 0.2f),
                checkmarkColor = MaterialTheme.colors.background,
            ),
        )
    }
}

@Composable
internal fun SingleChoiceCheckBoxSectionItem(
    title: String,
    keys: List<String>,
    selected: String,
    onSelected: (String) -> Unit,
    icon: @Composable (() -> Unit)? = null,
    color: Color = MaterialTheme.colors.onBackground,
    fontSize: TextUnit = 16.sp,
) {
    LabeledSectionItemLayout(
        title = title,
        icon = icon,
        color = color,
        fontSize = fontSize,
    ) {
        keys.forEach { key ->
            CheckBoxSectionItem(
                title = key,
                value = key == selected,
                onValueChange = { onSelected(key) },
            )
        }
    }
}

@Composable
internal fun RangeSliderSectionItem(
    title: String,
    valueRange: ClosedFloatingPointRange<Float>,
    value: ClosedFloatingPointRange<Float>,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
    textColor: Color = MaterialTheme.colors.onBackground,
    fontSize: TextUnit = 16.sp,
) {
    Column {
        LabeledSectionItemLayout(
            title = title,
            color = textColor,
            fontSize = fontSize,
        ) { }
        RangeSlider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            colors = SliderDefaults.colors(
                activeTickColor = MaterialTheme.colors.onBackground,
                activeTrackColor = MaterialTheme.colors.onBackground,
                thumbColor = MaterialTheme.colors.onBackground,
                inactiveTickColor = MaterialTheme.colors.onBackground.copy(alpha = 0.2f),
                inactiveTrackColor = MaterialTheme.colors.onBackground.copy(alpha = 0.2f),
            ),
            modifier = Modifier
                .fillMaxWidth(),
        )
    }
}

@Composable
internal fun SliderSectionItem(
    title: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    textColor: Color = MaterialTheme.colors.onBackground,
    fontSize: TextUnit = 16.sp,
    enabled: Boolean = true,
) {
    Column {
        LabeledSectionItemLayout(
            title = title,
            color = textColor,
            fontSize = fontSize,
        ) { }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 0f..200f,
            enabled = enabled,
            colors = SliderDefaults.colors(
                activeTickColor = MaterialTheme.colors.onBackground,
                activeTrackColor = MaterialTheme.colors.onBackground,
                thumbColor = MaterialTheme.colors.onBackground,
                inactiveTickColor = MaterialTheme.colors.onBackground.copy(alpha = 0.2f),
                inactiveTrackColor = MaterialTheme.colors.onBackground.copy(alpha = 0.2f),
            ),
            modifier = Modifier
                .fillMaxWidth(),
        )
    }
}

@Composable
internal fun ColumnScope.ShowMoreSectionItem(
    title: String,
    textColor: Color = MaterialTheme.colors.onBackground,
    fontSize: TextUnit = 16.sp,
    content: @Composable ColumnScope.() -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    var isExpanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = spring(),
    )

    LabeledSectionItemLayout(
        title = title,
        color = textColor,
        fontSize = fontSize,
    ) {
        Icon(
            imageVector = Icons.Default.ExpandMore,
            contentDescription = getString(Strings.ShowMore),
            tint = MaterialTheme.colors.onBackground,
            modifier = Modifier
                .rotate(rotation)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = { isExpanded = !isExpanded },
                ),
        )
    }
    if (isExpanded) {
        content()
    }
}

@Composable
internal fun ColumnScope.AddFilterSectionItem(
    isAdded: Boolean,
    onClick: () -> Unit,
    icon: ImageVector?,
    textColor: Color = MaterialTheme.colors.onBackground,
    fontSize: TextUnit = 16.sp,
    content: @Composable ColumnScope.() -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val moreIcon = if (isAdded) Icons.Default.Remove else Icons.Default.Add
    val title = if (isAdded) {
        getString(Strings.RemoveFilter)
    } else {
        getString(Strings.AddFilter)
    }

    LabeledSectionItemLayout(
        title = title,
        icon = {
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = title,
                    tint = MaterialTheme.colors.onBackground,
                    modifier = Modifier.padding(end = 8.dp),
                )
            }
        },
        color = textColor,
        fontSize = fontSize,
    ) {
        Icon(
            imageVector = moreIcon,
            contentDescription = title,
            tint = MaterialTheme.colors.onBackground,
            modifier = Modifier
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick,
                ),
        )
    }
    if (isAdded) {
        Spacer(modifier = Modifier.size(8.dp))
        content()
    }
}
