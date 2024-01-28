package web.pages.admin.users

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.silk.components.icons.mdi.MdiAdd
import com.varabyte.kobweb.silk.components.text.SpanText
import feature.admin.user.list.AdminUserListViewModel
import org.jetbrains.compose.web.css.em
import theme.MaterialTheme
import theme.roleStyle
import web.components.widgets.PageHeader
import web.compose.material3.component.Divider
import web.compose.material3.component.FilledButton

@Composable
fun AdminUsersPage(
    onError: suspend (String) -> Unit,
    onUserClick: (String) -> Unit,
    goToCreateUser: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        AdminUserListViewModel(
            scope = scope,
            onError = onError,
        )
    }
    val state by vm.observeStates().collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .gap(1.em)
    ) {
        PageHeader(state.strings.users) {
            FilledButton(
                onClick = { goToCreateUser() },
                leadingIcon = { MdiAdd() },
            ) {
                SpanText(text = state.strings.newUser)
            }
        }
        state.users.forEachIndexed { index, user ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                SpanText(
                    text = user.email,
                    modifier = Modifier
                        .roleStyle(MaterialTheme.typography.bodyLarge)
                        .onClick { onUserClick(user.id.toString()) }
                        .cursor(Cursor.Pointer)
                )
                Divider(modifier = Modifier.margin(topBottom = 0.5.em))
            }
        }
    }
}
