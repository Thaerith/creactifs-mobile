package be.creactifs.googlemaps

import android.annotation.SuppressLint
import android.os.Looper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapsScreen() {
    val context = LocalContext.current
    val locationServices = remember(context) {
        LocationServices.getFusedLocationProviderClient(context)
    }
    val locationPermission = rememberMultiplePermissionsState(permissions)
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        permissions.forEach { permission ->
            println("$permission is granted: ${results[permission]}")
        }
    }
    var properties by remember {
        mutableStateOf(MapProperties(mapType = MapType.SATELLITE))
    }
    val currentPositionMarkerState = rememberMarkerState()

    LaunchedEffect(locationPermission) {
        if (locationPermission.permissions.all { it.status.isGranted }) {
            locationServices.requestLocationUpdates(
                createLocationRequest(),
                { location ->
                    println(location)
                    currentPositionMarkerState.position =
                        LatLng(location.latitude, location.longitude)
                },
                Looper.getMainLooper()
            )
        } else {
            requestPermissionLauncher.launch(permissions.toTypedArray())
        }
    }

    Box(Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            properties = properties,
        ) {
            Marker(
                state = currentPositionMarkerState,
                title = "Position actuelle",
                snippet = "Votre position"
            )
        }
        Switch(
            checked = properties.mapType == MapType.SATELLITE,
            onCheckedChange = { checked ->
                properties = properties.copy(
                    mapType = if (checked) {
                        MapType.SATELLITE
                    } else {
                        MapType.NORMAL
                    }
                )
            },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        )
    }
}

private fun createLocationRequest(): LocationRequest {
    return LocationRequest.Builder(1000).build()
}

private val permissions = listOf(
    android.Manifest.permission.ACCESS_COARSE_LOCATION,
    android.Manifest.permission.ACCESS_FINE_LOCATION
)