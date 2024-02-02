package web.components.layouts

import androidx.compose.runtime.Composable
import web.components.widgets.PageHeader
import web.pages.admin.users.DeleteButtonWithConfirmation

@Composable
fun DetailPageLayout(
    title: String,
    showDelete: Boolean,
    deleteText: String,
    cancelText: String,
    onDeleteClick: () -> Unit,
    content: @Composable () -> Unit
) {
    PageHeader(
        title = title,
    ) {
        if (showDelete) {
            DeleteButtonWithConfirmation(
                deleteText = deleteText,
                cancelText = cancelText,
                onDelete = onDeleteClick,
            )
        }
    }
    content()
}
