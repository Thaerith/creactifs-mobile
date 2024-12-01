package be.creactifs.sensors.ui.screens

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.SensorManager.SENSOR_DELAY_NORMAL
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import be.creactifs.sensors.ui.navigation.LocalNavigation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SensorScreen(sensor: Sensor) {
    var running by remember { mutableStateOf(false) }
    var values by remember { mutableStateOf(emptyArray<Float>()) }

    val sensorManager = LocalContext.current
        .getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val navigate = LocalNavigation.current
    val sensorListener: SensorEventListener = remember {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                values = event?.values?.toTypedArray() ?: emptyArray<Float>()
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                println("$sensor $accuracy")
            }
        }
    }

    fun start() {
        sensorManager.registerListener(sensorListener, sensor, SENSOR_DELAY_NORMAL)
        running = true
    }

    fun stop() {
        sensorManager.unregisterListener(sensorListener)
        running = false
    }

    LaunchedEffect(Unit) {
        start()
    }

    DisposableEffect(Unit) {
        onDispose {
            stop()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(sensor.name) },
                navigationIcon = {
                    IconButton(onClick = navigate.back) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (running) {
                            stop()
                        } else {
                            start()
                        }
                    }) {
                        if (running) {
                            Icon(imageVector = Icons.Default.Pause, contentDescription = "Pause")
                        } else {
                            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Play")
                        }
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Card(
                modifier = Modifier.padding(horizontal = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            ) {
                ListItem(
                    headlineContent = { Text("Name") },
                    supportingContent = { Text(sensor.name) },
                )
                ListItem(
                    headlineContent = { Text("Type") },
                    supportingContent = { Text(sensor.stringType) },
                )
                ListItem(
                    headlineContent = { Text("Vendor") },
                    supportingContent = { Text(sensor.vendor) },
                )
            }

            Card(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            ) {
                if (values.isEmpty()) {
                    Text(
                        "No Data",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                } else {
                    values.forEachIndexed { index, value ->
                        ListItem(
                            headlineContent = { Text("Axe $index") },
                            supportingContent = { Text(value.toString()) }
                        )
                    }
                }
            }
        }
    }
}