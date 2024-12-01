package be.creactifs.bluetooth.server

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ServerScreen(viewModel: BluetoothServerViewModel) {
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
                    headlineContent = { Text("Advertisement name") },
                    supportingContent = { Text(viewModel.name) },
                )
                ListItem(
                    headlineContent = { Text("Address") },
                    supportingContent = { Text("viewModel.address") },
                )
                ListItem(
                    headlineContent = { Text("Server") },
                    supportingContent = {
                        if (viewModel.running) {
                            Text("Running")
                        } else {
                            Text("Stopped")
                        }
                    },
                )
            }
            Spacer(Modifier.weight(1f))
            Button(
                onClick = viewModel::onClick,
                modifier = Modifier.align(Alignment.End)
            ) {
                if (viewModel.running) {
                    Text("Stop")
                } else {
                    Text("Start")
                }
            }
        }
    }
}