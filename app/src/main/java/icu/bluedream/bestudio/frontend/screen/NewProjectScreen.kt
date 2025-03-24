package icu.bluedream.bestudio.frontend.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Javascript
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import icu.bluedream.bestudio.R
import icu.bluedream.bestudio.backend.RouterGraph
import icu.bluedream.bestudio.backend.state.canCreateProject
import icu.bluedream.bestudio.frontend.viewmodel.NewProjectViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@RouterGraph
@Composable
fun NewProjectScreen(
    navigator: DestinationsNavigator,
    viewModel: NewProjectViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = SnackbarHostState()
    fun showSnack(msg: String, canGoNext: Boolean = false) {
        scope.launch {
            if (canGoNext) {
                when(snackbarHostState.showSnackbar("All done", actionLabel = "Go")) {
                    SnackbarResult.Dismissed -> {}
                    SnackbarResult.ActionPerformed -> {
                    }
                }
            } else {
                snackbarHostState.showSnackbar(msg)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.new_proj_title)) },
                navigationIcon = {
                    IconButton({ navigator.popBackStack() }) {
                        Icon(Icons.Default.Close, "go previous")
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            if (viewModel.uiState.canCreateProject()) {
                FloatingActionButton({
                    viewModel.createProject(
                        context,
                        reportError = { showSnack(it) },
                        success = {
                            showSnack("", canGoNext = true)
                        }
                    )
                }) {
                    Icon(Icons.Default.Check, "save button")
                }
            }
        }
    ) { pd ->
        Column(
            modifier = Modifier
                .padding(pd)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            ListItem(
                headlineContent = { Text(stringResource(R.string.new_proj_what_is_it)) },
                leadingContent = { Icon(Icons.Default.Info, "tip icon") },
                supportingContent = { Text(stringResource(R.string.new_proj_what_is_it_exp)) }
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            TextField(
                viewModel.uiState.projectName,
                { neo -> viewModel.setProjectName(neo) },
                leadingIcon = { Icon(Icons.Default.TextFields, "project name input-box") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                singleLine = true,
                placeholder = { Text(stringResource(R.string.new_proj_name)) }
            )
            TextField(
                viewModel.uiState.projectDescription,
                { neo -> viewModel.setProjectDesc(neo) },
                leadingIcon = { Icon(Icons.Default.Description, "project description input-box") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                placeholder = { Text(stringResource(R.string.new_proj_desc)) }
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            ListItem(
                headlineContent = { Text(stringResource(R.string.new_proj_sapi_title)) },
                leadingContent = { Icon(Icons.Default.Javascript, "SAPI icon") },
                supportingContent = {
                    val desc = buildAnnotatedString {
                        withLink(
                            LinkAnnotation.Url(
                                "https://learn.microsoft.com/en-us/minecraft/creator/scriptapi/?view=minecraft-bedrock-stable",
                                TextLinkStyles(
                                    style = SpanStyle(
                                        color = MaterialTheme.colorScheme.primary,
                                        textDecoration = TextDecoration.Underline
                                    )
                                )
                            )
                        ) {
                            append(context.resources.getString(R.string.new_proj_sapi_exp1))
                        }
                        append(" ")
                        append(context.resources.getString(R.string.new_proj_sapi_exp2))
                    }
                    Text(desc)
                },
                modifier = Modifier.padding(top = 4.dp)
            )
            ListItem(
                headlineContent = { Text(stringResource(R.string.new_proj_sapi_use)) },
                leadingContent = { Icon(Icons.Default.Code, "SAPI icon") },
                trailingContent = {
                    Switch(
                        viewModel.uiState.useSapi,
                        { _ -> viewModel.setUseSapi() }
                    )
                }
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Column(modifier = Modifier.padding(top = 4.dp)) {
                Icon(Icons.Outlined.Info, "additional tip icon")
                Text(
                    stringResource(R.string.new_proj_final_tip),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}