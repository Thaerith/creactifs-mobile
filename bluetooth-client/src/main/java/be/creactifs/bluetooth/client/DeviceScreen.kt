package be.creactifs.bluetooth.client

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@SuppressLint("MissingPermission")
@Composable
fun DeviceScreen(viewModel: BluetoothClientViewModel) {
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
                    headlineContent = { Text("Device's name") },
                    trailingContent = {
                        Text(text = viewModel.device?.name ?: "Unknown")
                    },
                )
                ListItem(
                    headlineContent = { Text("Device's address") },
                    trailingContent = {
                        Text(text = viewModel.device?.address ?: "Unknown")
                    },
                )
                ListItem(
                    headlineContent = { Text("Device's class") },
                    trailingContent = {
                        Text(
                            text = viewModel.device?.bluetoothClass?.deviceClass?.toString()
                                ?: "Unknown"
                        )
                    },
                )

                ListItem(
                    headlineContent = { Text("Connection") },
                    trailingContent = {
                        when {
                            viewModel.connecting -> Text("Connecting")
                            viewModel.connected -> Text("Connected")

                        }
                    },
                )
            }
            if (viewModel.connected) {
                Text("Message:")
                Card(modifier = Modifier.fillMaxWidth(1f)) {
                    LazyColumn(contentPadding = PaddingValues(8.dp)) {
                        viewModel.messages.forEach {
                            item {
                                Text(it)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = viewModel::onDisconnect,
                enabled = viewModel.connected,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = "Disconnect")
            }
        }
    }
}