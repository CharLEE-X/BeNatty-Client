import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ComposeUIViewController
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import feature.RootContent
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import org.koin.compose.KoinContext
import org.koin.dsl.module
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow
import theme.AppTheme
import theme.safePaddingValues
import util.LocalWindow
import util.WindowInfo

@OptIn(ExperimentalForeignApi::class)
@Suppress("unused", "FunctionNaming", "FunctionName")
fun MainViewController(window: UIWindow): UIViewController {
    val koinApplication = initKoin(
        additionalModules = listOf(
            module {
                single { NotifierManager.initialize(NotificationPlatformConfiguration.Ios) }
            },
        ),
    )

    val uiViewController = ComposeUIViewController {
        LaunchedEffect(window.safeAreaInsets) {
            window.safeAreaInsets.useContents {
                safePaddingValues = PaddingValues(
                    top = this.top.dp,
                    bottom = this.bottom.dp,
                    start = this.left.dp,
                    end = this.right.dp,
                )
            }
        }

        val rememberedWindowInfo by remember(window) {
            val windowInfo = window.frame.useContents {
                WindowInfo(this.size.width.dp, this.size.height.dp)
            }
            mutableStateOf(windowInfo)
        }

        KoinContext(koinApplication.koin) {
            CompositionLocalProvider(
                LocalWindow provides rememberedWindowInfo,
            ) {
                AppTheme {
                    RootContent()
                }
            }
        }
    }

    koinApplication.koin.loadModules(
        listOf(
            module { single<() -> UIViewController> { { uiViewController } } },
        ),
    )

    return uiViewController
}
