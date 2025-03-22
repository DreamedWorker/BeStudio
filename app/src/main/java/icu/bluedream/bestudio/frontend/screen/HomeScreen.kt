package icu.bluedream.bestudio.frontend.screen

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import icu.bluedream.bestudio.backend.RouterGraph
import icu.bluedream.bestudio.frontend.viewmodel.HomeViewModel
import jakarta.inject.Inject

@Composable
@Destination
@RouterGraph(start = true)
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    Button({throw Exception("Test error catcher.")}) {
        Text("Test for catcher")
    }
}