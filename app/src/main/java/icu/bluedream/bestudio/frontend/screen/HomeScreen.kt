package icu.bluedream.bestudio.frontend.screen

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import icu.bluedream.bestudio.backend.RouterGraph

@Composable
@Destination
@RouterGraph(start = true)
fun HomeScreen() {
    Button({throw Exception("Test error catcher.")}) {
        Text("Test for catcher")
    }
}