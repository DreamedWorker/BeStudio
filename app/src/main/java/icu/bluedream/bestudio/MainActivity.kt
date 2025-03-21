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
import androidx.compose.ui.Modifier
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.ramcosta.composedestinations.DestinationsNavHost
import icu.bluedream.bestudio.frontend.screen.NavGraphs
import icu.bluedream.bestudio.frontend.theme.BeStudioTheme

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
        setContent {
            BeStudioTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DestinationsNavHost(
                        navGraph = NavGraphs.routerGraph,
                        modifier = Modifier.systemBarsPadding()
                    )
                }
            }
        }
    }
}