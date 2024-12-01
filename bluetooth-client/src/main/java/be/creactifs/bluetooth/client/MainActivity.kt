package be.creactifs.bluetooth.client

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import be.creactifs.bluetooth.shared.CreactifsBluetoothTheme

class MainActivity : ComponentActivity() {
    private val bluetoothClientViewModel: BluetoothClientViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CreactifsBluetoothTheme {
                if (bluetoothClientViewModel.connected || bluetoothClientViewModel.connecting) {
                    DeviceScreen(bluetoothClientViewModel)
                } else {
                    ScanningScreen(bluetoothClientViewModel)
                }
            }
        }
    }
}