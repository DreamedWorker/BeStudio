package icu.bluedream.bestudio

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint
import icu.bluedream.bestudio.backend.preference.keys.MainAct
import icu.bluedream.bestudio.backend.preference.prefGetValue
import icu.bluedream.bestudio.frontend.screen.NavGraphs
import icu.bluedream.bestudio.frontend.screen.WizardScreen
import icu.bluedream.bestudio.frontend.theme.BeStudioTheme
import kotlinx.coroutines.flow.first

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setOnExitAnimationListener { splashViewProvider ->
                val view = splashViewProvider.view
                with(ObjectAnimator.ofFloat(
                    view, View.ALPHA, 1f, 0f
                )) {
                    duration = 1000L
                    doOnEnd { splashViewProvider.remove() }
                    start()
                }
            }
        }
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        host = this
        setContent {
            val context = LocalContext.current
            var showMainBusiness by remember { mutableStateOf("0.0.0") }
            BeStudioTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (showMainBusiness == "0.0.1") {
                        DestinationsNavHost(
                            navGraph = NavGraphs.routerGraph,
                            modifier = Modifier.systemBarsPadding()
                        )
                    } else {
                        WizardScreen()
                    }
                }
                LaunchedEffect(Unit) {
                    showMainBusiness = context.prefGetValue(MainAct.LAST_USED_APP_VERSION, "0.0.0")
                        .first()
                }
            }
        }
    }

    companion object {
        lateinit var host: MainActivity
    }
}