package com.charleex.benatty

import android.app.Activity
import android.os.Bundle
import android.os.Debug
import android.provider.Settings
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.core.view.WindowCompat
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import component.pictures.CAMERA_PERMISSION_LAUNCHER
import component.pictures.ON_CAMERA_PERMISSION_GRANTED
import feature.RootContent
import org.jetbrains.compose.components.resources.BuildConfig
import org.koin.compose.KoinContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import org.koin.dsl.module
import theme.AppTheme
import util.SetColors

class MainActivity : ComponentActivity(), KoinComponent {
    private lateinit var requestCameraPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val onCameraPermissionGranted: (() -> Unit) by inject(named(ON_CAMERA_PERMISSION_GRANTED))
        setCameraPermissionLauncher(onCameraPermissionGranted)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        loadKoin()

        setContent {
            val configuration = LocalConfiguration.current

            KoinContext(getKoin()) {
                AppTheme {
                    Box(
                        modifier = Modifier
                            .windowInsetsPadding(
                                WindowInsets.systemBars.only(
                                    WindowInsetsSides.Horizontal +
                                        WindowInsetsSides.Top +
                                        WindowInsetsSides.Bottom,
                                ),
                            ),
                    ) {
                        window?.SetColors()
                        RootContent()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        keepScreenOn()
    }

    private fun loadKoin() {
        getKoin().loadModules(
            listOf(
                module {
                    single<Activity> { this@MainActivity }
                    single<ComponentActivity> { this@MainActivity }
                    single(named(CAMERA_PERMISSION_LAUNCHER)) { requestCameraPermissionLauncher }
                    single {
                        NotifierManager.initialize(
                            configuration = NotificationPlatformConfiguration.Android(
                                notificationIconResId = androidx.core.R.drawable.notification_bg, // TODO: Choose right icon
                            ),
                        )
                    }
                },
            ),
        )
    }

    private fun keepScreenOn() {
        Log.d(TAG, "BUILD_TYPE: ${BuildConfig.BUILD_TYPE}.")
        // FIXME: the BUILD_TYPE returns release even if debug is selected in the build variant.
//        if (BuildConfig.DEBUG) {

        val isDebuggerConnected = Debug.isDebuggerConnected()
        Log.d(TAG, "isDebuggerConnected: $isDebuggerConnected")

        val isAdbEnabled = Settings.Global.getInt(contentResolver, Settings.Global.ADB_ENABLED, 0) == 1
        Log.d(TAG, "isAdbEnabled: $isAdbEnabled")

        if (isDebuggerConnected || isAdbEnabled) {
            Log.d(TAG, "Keeping screen on for debugging.")
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            Log.d(TAG, "Keeping screen on for debugging is now deactivated.")
        }
//        }
    }

    private fun setCameraPermissionLauncher(onGranted: () -> Unit) {
        requestCameraPermissionLauncher = registerForActivityResult(RequestPermission()) { isGranted ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your app.
                onGranted()
            } else {
                // Explain to the user that the feature is unavailable because the
                // features requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
                println("Permission denied")
            }
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
