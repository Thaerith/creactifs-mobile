package be.creactifs.sensors.ui.composables

import android.hardware.Sensor
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SensorNavigationItem(
    sensor: Sensor,
    onClick: (Sensor) -> Unit,
) {
    ListItem(
        headlineContent = { Text(text = sensor.name) },
        supportingContent = { Text(text = "Version: ${sensor.version}") },
        overlineContent = { Text(text = sensor.stringType) },
        trailingContent = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null
            )
        },
        modifier = Modifier.clickable { onClick(sensor) }
    )
}