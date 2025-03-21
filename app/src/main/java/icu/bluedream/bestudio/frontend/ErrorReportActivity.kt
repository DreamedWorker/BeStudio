package icu.bluedream.bestudio.frontend

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.os.Process
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import icu.bluedream.bestudio.R
import icu.bluedream.bestudio.backend.preference.keys.NeverShowAgainKeys
import icu.bluedream.bestudio.backend.preference.prefGetValue
import icu.bluedream.bestudio.backend.preference.prefSetValue
import icu.bluedream.bestudio.frontend.theme.BeStudioTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ErrorReportActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val errorMessage = intent.getStringExtra("ERROR_MESSAGE") ?: "Unknown error"
        setContent {
            BeStudioTheme {
                val context = LocalContext.current
                val scope = rememberCoroutineScope()
                val snackbarHostState = SnackbarHostState()
                var showWhatIsItDialog by remember { mutableStateOf(false) }
                Surface {
                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize()
                            .systemBarsPadding(),
                        snackbarHost = { SnackbarHost(snackbarHostState) },
                        topBar = {
                            CenterAlignedTopAppBar(
                                title = {
                                    Text(stringResource(R.string.era_title))
                                },
                                navigationIcon = {
                                    IconButton({
                                        finishAffinity()
                                        Process.killProcess(Process.myPid())
                                    }) {
                                        Icon(Icons.Default.Close, "exit")
                                    }
                                },
                                actions = {
                                    IconButton({
                                        val pasteBoard =
                                            getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                                        val data =
                                            ClipData.newPlainText("error_message", errorMessage)
                                        pasteBoard.setPrimaryClip(data)
                                        scope.launch {
                                            snackbarHostState.showSnackbar(
                                                context.resources.getString(
                                                    R.string.era_label_logCopied
                                                )
                                            )
                                        }
                                    }) {
                                        Icon(Icons.Default.ContentCopy, "copy to clipboard")
                                    }
                                }
                            )
                        }
                    ) { ip ->
                        Column(
                            modifier = Modifier
                                .padding(ip)
                                .verticalScroll(rememberScrollState())
                        ) {
                            Text(errorMessage)
                        }
                    }
                    LaunchedEffect(Unit) {
                        showWhatIsItDialog = context.prefGetValue(
                            NeverShowAgainKeys.ERROR_REPORTER_WHAT_IS_IT,
                            true
                        ).first()
                    }
                    if (showWhatIsItDialog) {
                        AlertDialog(
                            onDismissRequest = { showWhatIsItDialog = !showWhatIsItDialog },
                            confirmButton = {
                                TextButton({ showWhatIsItDialog = !showWhatIsItDialog }) {
                                    Text(stringResource(R.string.app_confirm))
                                }
                            },
                            title = { Text(stringResource(R.string.era_dialog_title)) },
                            text = {
                                Column {
                                    Text(stringResource(R.string.era_dialog_msg))
                                    Spacer(Modifier.size(8.dp))
                                    Row(
                                        horizontalArrangement = Arrangement.End,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        TextButton({
                                            scope.launch {
                                                context.prefSetValue(
                                                    NeverShowAgainKeys.ERROR_REPORTER_WHAT_IS_IT,
                                                    false
                                                )
                                                showWhatIsItDialog = !showWhatIsItDialog
                                            }
                                        }) {
                                            Text(stringResource(R.string.app_never_show))
                                        }
                                    }
                                }
                            },
                            properties = DialogProperties(
                                dismissOnBackPress = false,
                                dismissOnClickOutside = false
                            )
                        )
                    }
                }
            }
        }
    }
}