import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DirectionsScreen(viewModel: DirectionsViewModel = viewModel()) {
    val directionsText by viewModel.directionsText // Observe the state

    // UI to display the directions
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = directionsText,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.fetchDirections() }) {
            Text("Fetch Directions")
        }
    }
}
