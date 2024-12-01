package be.creactifs.bluetooth.shared

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun CreactifsBluetoothTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            surface = Color.LightGray,
            surfaceVariant = Color.LightGray,
        )
    ) {
        content()
    }
}