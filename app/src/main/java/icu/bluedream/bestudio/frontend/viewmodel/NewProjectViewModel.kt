package icu.bluedream.bestudio.frontend.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import icu.bluedream.bestudio.backend.filesystem.CommonFileApiHelper
import icu.bluedream.bestudio.backend.model.DreamManifest
import icu.bluedream.bestudio.backend.state.NewProjectUiState
import icu.bluedream.bestudio.backend.state.useDefaultDesc
import javax.inject.Inject
import icu.bluedream.bestudio.R
import icu.bluedream.bestudio.backend.filesystem.project.makeProjectPath
import icu.bluedream.bestudio.backend.filesystem.project.writeDreamManifest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

@HiltViewModel
class NewProjectViewModel @Inject constructor() : ViewModel() {
    var uiState by mutableStateOf(NewProjectUiState())
        private set

    fun setProjectName(neoWord: String) {
        uiState = uiState.copy(projectName = neoWord)
    }

    fun setProjectDesc(neoWord: String) {
        uiState = uiState.copy(projectDescription = neoWord)
    }

    fun setUseSapi() {
        uiState = uiState.copy(useSapi = uiState.useSapi.not())
    }

    fun createProject(context: Context, reportError: (String) -> Unit, success: () -> Unit) {
        viewModelScope.launch(Dispatchers.Main) {
            val configuration = uiState
            val rootPath = "${context.getExternalFilesDir("workspace")}"
            CommonFileApiHelper.checkWorklistDir(rootPath)
            val folderName = "proj${System.currentTimeMillis()}"
            val manifest = DreamManifest(
                projectName = configuration.projectName,
                projectDesc = if (configuration.useDefaultDesc())
                    "Made by ${context.resources.getString(R.string.app_name)}"
                else configuration.projectDescription,
                useSapi = configuration.useSapi,
                bpUUID = UUID.randomUUID().toString().lowercase(),
                rpUUID = UUID.randomUUID().toString().lowercase(),
                minEngineVersion = listOf(1, 20, 60),
                version = listOf(0, 0, 1)
            )
            val jobDone = launch(Dispatchers.IO) {
                CommonFileApiHelper.makeProjectPath("$rootPath/$folderName")
                    .onSuccess {
                        File("$rootPath/$folderName/bsProject").mkdir()
                        File("$rootPath/$folderName/sources").mkdir()
                        CommonFileApiHelper.writeDreamManifest(
                            "$rootPath/$folderName/bsProject/manifest.json",
                            manifest
                        )
                        File("$rootPath/$folderName/sources/bp").mkdir()
                        File("$rootPath/$folderName/sources/rp").mkdir()
                        withContext(Dispatchers.Main) {
                            success()
                        }
                    }
                    .onFailure { t ->
                        withContext(Dispatchers.Main) {
                            reportError(
                                t.message ?: "Unknown error while creating project folder"
                            )
                        }
                    }
            }
            jobDone.join()
        }
    }
}