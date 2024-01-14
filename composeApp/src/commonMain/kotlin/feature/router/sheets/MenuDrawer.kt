package feature.router.sheets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import component.localization.Strings
import component.localization.getString
import feature.debug.DebugContract
import feature.debug.DebugViewModel
import theme.Dimensions
import theme.safePaddingValues
import util.rememberBitmapFromBytes

@Composable
internal fun MenuDrawer(
    snackbarHostState: SnackbarHostState,
    onCloseClicked: () -> Unit,
    onLogOut: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        DebugViewModel(
            scope = scope,
            onError = { snackbarHostState.showSnackbar(it) },
            onGenerateUsersSuccess = { snackbarHostState.showSnackbar(it) },
            onDeleteUsersSuccess = {
                val result = snackbarHostState.showSnackbar(
                    message = it,
                    actionLabel = "Logout",
                )
                when (result) {
                    SnackbarResult.Dismissed -> {}
                    SnackbarResult.ActionPerformed -> onLogOut()
                }
            },
        )
    }
    val state by vm.observeStates().collectAsState()

    Surface(
        color = MaterialTheme.colors.surface,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = safePaddingValues.calculateTopPadding(),
                    bottom = safePaddingValues.calculateBottomPadding(),
                ),
        ) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(
                    bottom = Dimensions.BOTTOM_BAR_HEIGHT.dp,
                    top = 16.dp,
                    start = 16.dp,
                    end = 16.dp,
                ),
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                item { TopBarSection(onCloseClicked) }
                item { ClearCacheAndLogOutSection(vm, onLogOut) }
                item { GenerateUsersSection(vm, state) }
                item { DeleteUsersSection(vm) }
                item { UserFlagsSection(vm, state) }
                item { LocationSection(vm, state) }
                item { NotificationSection(vm, state) }
                item { PicturesSection(vm, state) }
            }
            AnimatedVisibility(
                visible = state.isLoading,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.onBackground.copy(alpha = 0.2f)),
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
            }
        }
    }
}

@Composable
private fun TopBarSection(onCloseClicked: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close",
            tint = MaterialTheme.colors.onBackground,
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.CenterStart)
                .clickable { onCloseClicked() },
        )
        Text(
            text = "Debug",
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

@Composable
private fun ClearCacheAndLogOutSection(vm: DebugViewModel, onLogOut: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Button(
                onClick = { vm.trySend(DebugContract.Inputs.OnClearCacheClick) },
                modifier = Modifier.weight(1f),
            ) {
                Text(text = "Clear cache")
            }
            Button(
                onClick = onLogOut,
                modifier = Modifier.weight(1f),
            ) {
                Text(text = getString(Strings.Login.LogoutButton))
            }
        }
    }
}

@Composable
private fun GenerateUsersSection(vm: DebugViewModel, state: DebugContract.State) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        OutlinedTextField(
            value = state.generateUsersTarget.toString(),
            onValueChange = { vm.trySend(DebugContract.Inputs.SetGenerateUsersTarget(it)) },
            label = { Text(text = "Users Count") },
            interactionSource = interactionSource,
            modifier = Modifier.weight(1f),
        )
        Button(
            onClick = { vm.trySend(DebugContract.Inputs.GenerateUsers) },
            modifier = Modifier.weight(1f),
        ) {
            Text(text = "Generate")
        }
    }
}

@Composable
internal fun DeleteUsersSection(vm: DebugViewModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Button(
            onClick = { vm.trySend(DebugContract.Inputs.DeleteAllUsers) },
            modifier = Modifier.weight(1f),
        ) {
            Text(text = "Delete All Users")
        }
        Button(
            onClick = { vm.trySend(DebugContract.Inputs.DeleteGeneratedUsers) },
            modifier = Modifier.weight(1f),
        ) {
            Text(text = "Delete Generated")
        }
    }
}

@Composable
internal fun UserFlagsSection(vm: DebugViewModel, state: DebugContract.State) {
//    SwitchSectionItem(
//        title = "Premium",
//        value = state.isPremium,
//        onValueChange = { vm.trySend(DebugContract.Inputs.ToggleIsPremium) },
//        modifier = Modifier.fillMaxWidth(),
//    )
}

@Composable
private fun LocationSection(vm: DebugViewModel, state: DebugContract.State) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "Location foreground:",
                color = MaterialTheme.colors.onBackground,
            )
            Text(
                text = state.locationPermissionStatus.name,
                color = MaterialTheme.colors.onBackground,
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            AnimatedVisibility(
                state.locationPermissionStatus.isNotGranted(),
                modifier = Modifier.weight(1f),
            ) {
                Button(
                    onClick = { vm.trySend(DebugContract.Inputs.RequestLocationPermission) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = "Request Location")
                }
            }
            Button(
                onClick = { vm.trySend(DebugContract.Inputs.OpenAppSettings) },
                modifier = Modifier.weight(1f),
            ) {
                Text(text = "Open settings")
            }
        }
        state.latLng?.let {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "Current Location:",
                    color = MaterialTheme.colors.onBackground,
                )
                Text(
                    text = "${it.latitude}, ${it.longitude}",
                    color = MaterialTheme.colors.onBackground,
                )
            }
        }
        state.location?.let {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "Place info: ",
                    color = MaterialTheme.colors.onBackground,
                )
                Text(
                    text = "City: ${it.city}\nState: ${it.state}\nCountry: ${it.country}",
                    color = MaterialTheme.colors.onBackground,
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Button(
                onClick = { vm.trySend(DebugContract.Inputs.GetLatLng) },
                modifier = Modifier.weight(1f),
            ) {
                Text(text = "Get position lat lng")
            }
            Button(
                onClick = { vm.trySend(DebugContract.Inputs.GetLocation) },
                modifier = Modifier.weight(1f),
            ) {
                Text(text = "Get Place info")
            }
        }
    }
}

@Composable
private fun NotificationSection(vm: DebugViewModel, state: DebugContract.State) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "Local notification:",
                color = MaterialTheme.colors.onBackground,
            )
            Text(
                text = state.notificationPermissionStatus.name,
                color = MaterialTheme.colors.onBackground,
            )
        }
        AnimatedVisibility(
            state.notificationPermissionStatus.isNotGranted(),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Button(
                onClick = { vm.trySend(DebugContract.Inputs.RequestNotificationPermission) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = "Request")
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            OutlinedTextField(
                value = state.notificationText,
                onValueChange = { vm.trySend(DebugContract.Inputs.SetNotificationText(it)) },
                label = { Text(text = "Notification text") },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    autoCorrect = true,
                    imeAction = ImeAction.Send,
                ),
                keyboardActions = KeyboardActions(
                    onSend = { vm.trySend(DebugContract.Inputs.SendNotificationNow) },
                ),
                modifier = Modifier.consumeWindowInsets(WindowInsets.ime),
            )
            Button(
                onClick = { vm.trySend(DebugContract.Inputs.SendNotificationNow) },
                enabled = state.notificationText.isNotEmpty(),
                modifier = Modifier.weight(1f),
            ) {
                Text(text = "Send")
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PicturesSection(vm: DebugViewModel, state: DebugContract.State) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "Camera permission:",
                color = MaterialTheme.colors.onBackground,
            )
            Text(
                text = state.cameraPermissionStatus.name,
                color = MaterialTheme.colors.onBackground,
            )
        }
        AnimatedVisibility(
            state.cameraPermissionStatus.isNotGranted(),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Button(
                onClick = { vm.trySend(DebugContract.Inputs.RequestCameraPermission) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = "Request")
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "Gallery permission:",
                color = MaterialTheme.colors.onBackground,
            )
            Text(
                text = state.galleryPermissionStatus.name,
                color = MaterialTheme.colors.onBackground,
            )
        }
        AnimatedVisibility(
            state.galleryPermissionStatus.isNotGranted(),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Button(
                onClick = { vm.trySend(DebugContract.Inputs.RequestGalleryPermission) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = "Request")
            }
        }
        AnimatedVisibility(state.galleryPermissionStatus.isGranted()) {
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Button(
                        onClick = { vm.trySend(DebugContract.Inputs.PickImageFromGallery) },
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(text = "Pic from gallery")
                    }
                    // FIXME: Camera crashes the app after picking the image
                    Button(
                        onClick = { vm.trySend(DebugContract.Inputs.PickImageFromCamera) },
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(text = "Pic from camera")
                    }
                }
                FlowRow(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    state.pickedBitmaps.forEachIndexed { index, bitmap ->
                        rememberBitmapFromBytes(bitmap.toByteArray())?.let { imageBitmap ->
                            Surface(
                                shape = RoundedCornerShape(30.dp),
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(8.dp)
                                    .clickable { vm.trySend(DebugContract.Inputs.ClearPickedBitmaps) },
                            ) {
                                Image(
                                    bitmap = imageBitmap,
                                    contentDescription = "Picked image",
                                    contentScale = ContentScale.Crop,
                                    alignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize(),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
