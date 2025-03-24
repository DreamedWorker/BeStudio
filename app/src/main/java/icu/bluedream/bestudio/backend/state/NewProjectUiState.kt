package icu.bluedream.bestudio.backend.state

data class NewProjectUiState(
    val projectName: String = "",
    val projectDescription: String = "",
    val useSapi: Boolean = false
)

fun NewProjectUiState.canCreateProject(): Boolean = this.projectName.isNotBlank()
fun NewProjectUiState.useDefaultDesc(): Boolean = this.projectDescription.isBlank()
