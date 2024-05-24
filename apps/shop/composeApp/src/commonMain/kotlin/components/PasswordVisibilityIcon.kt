package components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun PasswordVisibilityIcon(
    isPasswordVisible: Boolean,
    onClick: (() -> Unit) = {},
) {
    val icon = if (isPasswordVisible) Icons.Rounded.Visibility else Icons.Filled.VisibilityOff

    Icon(
        imageVector = icon,
        contentDescription = "Password visibility toggle",
        tint = MaterialTheme.colors.onPrimary,
        modifier = Modifier
            .size(24.dp)
            .clickable { onClick() },
    )
}
