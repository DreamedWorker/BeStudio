package icu.bluedream.bestudio.frontend.screen

import android.content.Intent
import android.os.Process
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Adb
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import icu.bluedream.bestudio.MainActivity
import icu.bluedream.bestudio.R
import icu.bluedream.bestudio.backend.preference.keys.MainAct
import icu.bluedream.bestudio.backend.preference.prefSetValue
import kotlinx.coroutines.launch

@Composable
fun WizardScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { pd ->
        Column(
            modifier = Modifier
                .padding(pd)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) { // Head
                Image(
                    painterResource(R.drawable.app_logo), "app_logo",
                    modifier = Modifier.size(92.dp)
                )
                Text(
                    stringResource(R.string.wizard_title),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black)
                )
                Text(stringResource(R.string.wizard_subtitle))
            }
            Column(
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(
                    stringResource(R.string.wizard_necessary_step),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ListItem(
                        headlineContent = { Text(stringResource(R.string.wizard_step_policy)) },
                        supportingContent = { Text(stringResource(R.string.wizard_step_policy_exp)) },
                        leadingContent = { Icon(Icons.Default.Policy, "policy reading step icon") },
                        trailingContent = { Icon(Icons.Default.Check, "go reading icon") },
                        modifier = Modifier.clickable {
                            val page = "https://bluedream.icu/TravellersBag/normative/user_service_agreement.html".toUri()
                            CustomTabsIntent.Builder()
                                .build()
                                .launchUrl(context, page)
                        },
                        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                    )
                }
            }
            Spacer(Modifier.size(16.dp))
            Column(
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(
                    stringResource(R.string.wizard_optional_steps),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ListItem(
                        headlineContent = { Text(stringResource(R.string.wizard_step_shizuku)) },
                        supportingContent = { Text(stringResource(R.string.wizard_step_shizuku_exp)) },
                        leadingContent = { Icon(Icons.Default.Adb, "install shizuku step icon") },
                        trailingContent = { Icon(Icons.AutoMirrored.Default.ArrowForward, "go installation") },
                        modifier = Modifier.clickable {
                            val page = "https://shizuku.rikka.app/".toUri()
                            CustomTabsIntent.Builder()
                                .build()
                                .launchUrl(context, page)
                        },
                        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
                    )
                }
            }
            Spacer(Modifier.weight(1f))
            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(Modifier.weight(1f))
                OutlinedButton({
                    MainActivity.host.finishAffinity()
                    Process.killProcess(Process.myPid())
                }) {
                    Text(stringResource(R.string.wizard_disagree))
                }
                Spacer(Modifier.width(8.dp))
                Button({
                    scope.launch {
                        context.prefSetValue(MainAct.LAST_USED_APP_VERSION, "0.0.1")
                        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
                        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        MainActivity.host.startActivity(intent)
                        Process.killProcess(Process.myPid())
                    }
                }) {
                    Text(stringResource(R.string.wizard_agree))
                }
            }
        }
    }
}
