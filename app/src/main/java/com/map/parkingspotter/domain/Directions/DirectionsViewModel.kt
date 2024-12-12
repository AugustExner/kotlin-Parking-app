import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.map.parkingspotter.domain.Directions.RetrofitInstance
import kotlinx.coroutines.launch

class DirectionsViewModel : ViewModel() {
    var directionsText = mutableStateOf("Fetching directions...")
        private set

    fun fetchDirections() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getDirections(
                    origin = "Aarhus",
                    destination = "Vejle Rådhus",
                    apiKey = "AIzaSyDgORILdn4tqoGRbvGsH3eKXix5LGPldi8"
                )
                // Assuming you want to display the distance and duration of the first route's first leg
                val leg = response.routes.firstOrNull()?.legs?.firstOrNull()
                directionsText.value = if (leg != null) {
                    "Distance: ${leg.distance.text}, Duration: ${leg.duration.text}"
                } else {
                    "No directions found."
                }
            } catch (e: Exception) {
                directionsText.value = "Failed to fetch directions: ${e.message}"
            }
        }
    }
}
