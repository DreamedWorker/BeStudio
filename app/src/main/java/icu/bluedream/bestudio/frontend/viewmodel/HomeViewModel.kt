package icu.bluedream.bestudio.frontend.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import icu.bluedream.bestudio.backend.state.HomeScreenUiState
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(): ViewModel() {
    var uiState by mutableStateOf(HomeScreenUiState())
        private set
}