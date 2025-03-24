package icu.bluedream.bestudio.frontend.screen

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import icu.bluedream.bestudio.R
import icu.bluedream.bestudio.backend.RouterGraph
import icu.bluedream.bestudio.backend.filesystem.CommonFileApiHelper
import icu.bluedream.bestudio.frontend.screen.destinations.NewProjectScreenDestination
import icu.bluedream.bestudio.frontend.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Destination
@RouterGraph(start = true)
fun HomeScreen(
    navigator: DestinationsNavigator,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    CommonFileApiHelper.checkWorklistDir("${context.getExternalFilesDir("workspace")}")
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    var searchExpand by remember { mutableStateOf(false) }
    var inputWords by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val searchPadding by animateDpAsState(
        targetValue = if (searchExpand) 0.dp else 16.dp
    )
    ModalNavigationDrawer(
        modifier = Modifier.fillMaxSize(),
        drawerContent = {
            ModalDrawerSheet(drawerState = drawerState) {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState())
                        .systemBarsPadding().padding(16.dp)
                ) {
                    Row {
                        Spacer(Modifier.weight(1f))
                        IconButton({}) {
                            Icon(Icons.Default.Settings, "software settings button")
                        }
                    }
                }
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                SearchBar(
                    inputField = {
                        SearchBarDefaults.InputField(
                            query = inputWords,
                            onQueryChange = { neoWords -> inputWords = neoWords },
                            onSearch = { words -> println(words); searchExpand = false; inputWords = "" },
                            expanded = searchExpand,
                            onExpandedChange = { isExpanded -> searchExpand = isExpanded },
                            trailingIcon = {
                                IconButton({
                                    if (searchExpand) {
                                        searchExpand = false; inputWords = ""
                                    }
                                }) {
                                    if (searchExpand) {
                                        Icon(Icons.Default.Clear, "clear and close")
                                    } else {
                                        Icon(Icons.Default.Search, "search icon")
                                    }
                                }
                            },
                            placeholder = { Text(stringResource(R.string.home_search_placeolder)) },
                            leadingIcon = {
                                IconButton({
                                    scope.launch {
                                        drawerState.apply {
                                            if (isClosed) {
                                                open()
                                            }
                                        }
                                    }
                                }) {
                                    Icon(Icons.Default.Menu, "menu switch")
                                }
                            },
                            modifier = Modifier.focusable(enabled = searchExpand)
                        )
                    },
                    expanded = searchExpand,
                    onExpandedChange = { isExpanded -> searchExpand = isExpanded },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = searchPadding)
                ) { }
            },
            floatingActionButton = {
                FloatingActionButton({ navigator.navigate(NewProjectScreenDestination)}) {
                    Icon(Icons.Default.Add, "create projects button")
                }
            }
        ) { pd ->
            Column(modifier = Modifier.padding(pd)) {  }
        }
    }
}