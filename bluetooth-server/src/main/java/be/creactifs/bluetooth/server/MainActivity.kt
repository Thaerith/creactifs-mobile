package be.creactifs.bluetooth.server

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import be.creactifs.bluetooth.shared.CreactifsBluetoothTheme

class MainActivity : ComponentActivity() {
    private val bluetoothServerViewModel: BluetoothServerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CreactifsBluetoothTheme {
                ServerScreen(viewModel = bluetoothServerViewModel)
            }
        }
    }
}
