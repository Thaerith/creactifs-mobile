package be.creactifs.bluetooth.client

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@SuppressLint("MissingPermission")
@Composable
fun ScanningScreen(viewModel: BluetoothClientViewModel) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text("Status:")
            Card {
                ListItem(
                    headlineContent = { Text("Scanning") },
                    trailingContent = { Text(viewModel.scanning.toString()) },
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Devices found (${viewModel.devices.size}):")
            Card {
                if (viewModel.devices.isEmpty()) {
                    Text(
                        "No devices found",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        textAlign = TextAlign.Center
                    )
                } else {
                    viewModel.devices.forEach { device ->
                        ListItem(
                            headlineContent = { Text(device.name ?: "Unknown") },
                            supportingContent = { Text(device.address ?: "Unknown") },
                            modifier = Modifier.clickable(enabled = !viewModel.connecting && !viewModel.connected) {
                                viewModel.onConnect(
                                    device
                                )
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = viewModel::onScan,
                enabled = !viewModel.connecting && !viewModel.connected,
                modifier = Modifier.align(Alignment.End)
            ) {
                if (viewModel.scanning) {
                    Text(text = "Stop scanning")
                } else {
                    Text(text = "Start scanning")
                }
            }
        }
    }
}