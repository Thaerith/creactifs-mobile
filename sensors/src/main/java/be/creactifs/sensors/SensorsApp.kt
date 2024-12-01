package be.creactifs.sensors

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import be.creactifs.sensors.ui.navigation.Router
import be.creactifs.sensors.ui.theme.SensorsTheme

@Composable
fun SensorsApp() {
    val navController = rememberNavController()

    SensorsTheme {
        Router(navController = navController)
    }
}