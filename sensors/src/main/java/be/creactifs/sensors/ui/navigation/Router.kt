package be.creactifs.sensors.ui.navigation

import android.content.Context
import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import be.creactifs.sensors.ui.screens.HomeScreen
import be.creactifs.sensors.ui.screens.SensorScreen

@Composable
fun Router(navController: NavHostController) {
    val navigate = remember(navController) {
        Navigate(
            toSensor = { sensor ->
                navController.navigate(Routes.SENSOR.name + "/${sensor.type}")
            },
            back = { navController.popBackStack() }
        )
    }

    ProvideNavigation(navigate = navigate) {
        NavHost(
            navController = navController,
            startDestination = Routes.HOME.name
        ) {
            composable(Routes.HOME.name) {
                HomeScreen()
            }
            composable(Routes.SENSOR.name + "/{sensorTypeId}",
                arguments = listOf(
                    navArgument("sensorTypeId") {
                        type = NavType.IntType
                        nullable = false
                    }
                )
            ) { entry ->
                val sensorTypeId = requireNotNull(entry.arguments?.getInt("sensorTypeId"))
                val sensorManager =
                    LocalContext.current.getSystemService(Context.SENSOR_SERVICE) as SensorManager
                val sensor = requireNotNull(sensorManager.getDefaultSensor(sensorTypeId))
                SensorScreen(sensor = sensor)
            }
        }
    }
}
