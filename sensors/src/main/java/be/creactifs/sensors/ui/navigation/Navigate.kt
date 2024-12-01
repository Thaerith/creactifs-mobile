package be.creactifs.sensors.ui.navigation

import android.hardware.Sensor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf

data class Navigate(
    val toSensor: (Sensor) -> Unit = {},
    val back: () -> Unit = {}
)

@Composable
fun ProvideNavigation(navigate: Navigate, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalNavigation provides navigate) {
        content()
    }
}

val LocalNavigation = compositionLocalOf { Navigate() }