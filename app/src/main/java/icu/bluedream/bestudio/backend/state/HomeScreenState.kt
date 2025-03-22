package icu.bluedream.bestudio.backend.state

data class HomeScreenUiState(
    val projectItems: List<ProjectItemUiState> = listOf<ProjectItemUiState>()
)

data class ProjectItemUiState(
    val name: String,
    val path: String
)