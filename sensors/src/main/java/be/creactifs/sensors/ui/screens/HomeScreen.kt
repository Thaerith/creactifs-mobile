package be.creactifs.sensors.ui.screens

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import be.creactifs.sensors.ui.composables.SensorNavigationItem
import be.creactifs.sensors.ui.navigation.LocalNavigation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val sensors = getSensors()
    val navigate = LocalNavigation.current

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Available Sensors") })
        },
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(items = sensors) { sensor ->
                SensorNavigationItem(sensor, onClick = navigate.toSensor)
            }
        }
    }
}

@Composable
private fun getSensors(): List<Sensor> {
    var sensors: List<Sensor> by remember { mutableStateOf(emptyList()) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensors = sensorManager.getSensorList(Sensor.TYPE_ALL)
    }

    return sensors
}