package feature.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import component.localization.Strings
import component.localization.getString
import components.PasswordVisibilityIcon
import theme.AppTheme
import util.autofill

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun LoginContent(
    onError: suspend (String) -> Unit,
    onAuthenticated: () -> Unit,
    gotoPrivacyPolicy: () -> Unit,
    gotoTnC: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        LoginViewModel(
            scope = scope,
            onAuthenticated = onAuthenticated,
            onError = onError,
            goToPrivacyPolicy = gotoPrivacyPolicy,
            goToTnC = gotoTnC,
        )
    }
    val state by vm.observeStates().collectAsState()
    val focusManager = LocalFocusManager.current
    val focusRequester = FocusRequester()
    val keyboardController = LocalSoftwareKeyboardController.current

    var focusState by remember { mutableStateOf(false) }
    var wasAutoFilled by remember { mutableStateOf(false) }

    val buttonText = when (state.screenState) {
        LoginContract.ScreenState.LOGIN -> getString(Strings.Auth.LoginButton)
        LoginContract.ScreenState.REGISTER -> getString(Strings.Auth.RegisterButton)
        LoginContract.ScreenState.FORGOT_PASSWORD -> getString(Strings.Auth.SendButton)
    }

    AppTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .padding(32.dp)
                .clickable {
                    keyboardController?.hide()
                },
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = getString(Strings.Auth.WelcomeMessage),
                style = MaterialTheme.typography.h4,
                color = MaterialTheme.colors.onBackground,
            )
            Text(
                text = getString(Strings.AppName),
                style = MaterialTheme.typography.h3,
                color = MaterialTheme.colors.onBackground,
            )
            Spacer(modifier = Modifier.weight(1f))
            TextField(
                value = state.email,
                onValueChange = { vm.trySend(LoginContract.Inputs.SetEmail(it)) },
                label = {
                    Text(
                        text = getString(Strings.Auth.EmailLabel),
                        color = MaterialTheme.colors.background.copy(alpha = 0.3f),
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = MaterialTheme.colors.onBackground,
                    focusedLabelColor = MaterialTheme.colors.onBackground,
                    unfocusedLabelColor = MaterialTheme.colors.onBackground.copy(alpha = 0.3f),
                    placeholderColor = MaterialTheme.colors.onBackground.copy(alpha = 0.3f),
                ),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = false,
                    keyboardType = KeyboardType.Email,
                    imeAction = when (state.screenState) {
                        LoginContract.ScreenState.LOGIN,
                        LoginContract.ScreenState.REGISTER,
                        -> ImeAction.Next

                        LoginContract.ScreenState.FORGOT_PASSWORD -> ImeAction.Send
                    },
                ),
                keyboardActions = KeyboardActions(
                    onSend = { vm.trySend(LoginContract.Inputs.OnLoginClick) },
                    onNext = { focusManager.moveFocus(FocusDirection.Down) },
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .onFocusChanged { focusState = it.isFocused }
                    .autofill(
                        autofillTypes = listOf(AutofillType.EmailAddress),
                        onFill = {
                            vm.trySend(LoginContract.Inputs.SetEmail(it))
                            wasAutoFilled = true
                            focusManager.moveFocus(FocusDirection.Down)
                        },
                    )
                    .consumeWindowInsets(WindowInsets.ime),
            )
            AnimatedVisibility(visible = state.screenState != LoginContract.ScreenState.FORGOT_PASSWORD) {
                TextField(
                    value = state.password,
                    onValueChange = { vm.trySend(LoginContract.Inputs.SetPassword(it)) },
                    label = {
                        Text(
                            text = getString(Strings.Auth.PasswordLabel),
                            color = MaterialTheme.colors.background.copy(alpha = 0.3f),
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        autoCorrect = false,
                        keyboardType = KeyboardType.Password,
                        imeAction = when (state.screenState) {
                            LoginContract.ScreenState.LOGIN -> ImeAction.Send
                            else -> ImeAction.Next
                        },
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = MaterialTheme.colors.onBackground,
                        focusedLabelColor = MaterialTheme.colors.onBackground,
                        unfocusedLabelColor = MaterialTheme.colors.onBackground.copy(alpha = 0.3f),
                    ),
                    keyboardActions = KeyboardActions(
                        onSend = { vm.trySend(LoginContract.Inputs.OnLoginClick) },
                        onNext = { focusManager.moveFocus(FocusDirection.Down) },
                    ),
                    visualTransformation = if (state.isPasswordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Password,
                            contentDescription = null,
                        )
                    },
                    trailingIcon = {
                        PasswordVisibilityIcon(
                            isPasswordVisible = state.isPasswordVisible,
                            onClick = { vm.trySend(LoginContract.Inputs.TogglePasswordVisibility) },
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .onFocusChanged { focusState = it.isFocused }
                        .autofill(
                            autofillTypes = listOf(AutofillType.Password, AutofillType.NewPassword),
                            onFill = {
                                vm.trySend(LoginContract.Inputs.SetPassword(it))
                                wasAutoFilled = true
                                focusManager.moveFocus(FocusDirection.Down)
                            },
                        )
                        .consumeWindowInsets(WindowInsets.ime),
                )
            }
            AnimatedVisibility(visible = state.screenState == LoginContract.ScreenState.REGISTER) {
                TextField(
                    value = state.repeatPassword,
                    onValueChange = { vm.trySend(LoginContract.Inputs.SetRepeatPassword(it)) },
                    label = {
                        Text(
                            text = getString(Strings.Auth.ConfirmPasswordLabel),
                            color = MaterialTheme.colors.background.copy(alpha = 0.3f),
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = MaterialTheme.colors.onBackground,
                        focusedLabelColor = MaterialTheme.colors.onBackground,
                        unfocusedLabelColor = MaterialTheme.colors.onBackground.copy(alpha = 0.3f),
                    ),
                    keyboardActions = KeyboardActions(
                        onSend = { vm.trySend(LoginContract.Inputs.OnLoginClick) },
                    ),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        autoCorrect = false,
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Send,
                    ),
                    visualTransformation = if (state.isRepeatPasswordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Key,
                            contentDescription = null,
                        )
                    },
                    trailingIcon = {
                        PasswordVisibilityIcon(
                            isPasswordVisible = state.isRepeatPasswordVisible,
                            onClick = { vm.trySend(LoginContract.Inputs.ToggleRepeatPasswordVisibility) },
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .consumeWindowInsets(WindowInsets.ime),
                )
            }
            Row {
                Row(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    AnimatedVisibility(state.screenState == LoginContract.ScreenState.LOGIN) {
                        Text(
                            text = getString(Strings.Auth.ForgotPasswordLabel),
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier
                                .clickable {
                                    vm.trySend(
                                        LoginContract.Inputs.SetScreenState(
                                            LoginContract.ScreenState.FORGOT_PASSWORD,
                                        ),
                                    )
                                },
                        )
                    }
                }
                Button(
                    onClick = { vm.trySend(LoginContract.Inputs.OnLoginClick) },
                    shape = AbsoluteRoundedCornerShape(100),
                    modifier = Modifier
                        .height(48.dp)
                        .weight(1f),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(buttonText)
                        Spacer(Modifier.size(16.dp))
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = null,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(2f))
            Row {
                AnimatedVisibility(visible = state.screenState == LoginContract.ScreenState.LOGIN) {
                    Text(
                        text = getString(Strings.Auth.DontHaveAnAccount),
                        color = MaterialTheme.colors.onBackground,
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                            .clickable {
                                vm.trySend(LoginContract.Inputs.SetScreenState(LoginContract.ScreenState.REGISTER))
                            },
                    )
                }
                AnimatedVisibility(visible = state.screenState != LoginContract.ScreenState.LOGIN) {
                    Text(
                        text = getString(Strings.Auth.GoBackToLoginButton),
                        color = MaterialTheme.colors.onBackground,
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                            .clickable {
                                vm.trySend(LoginContract.Inputs.SetScreenState(LoginContract.ScreenState.LOGIN))
                            },
                    )
                }
            }
        }
    }
}
