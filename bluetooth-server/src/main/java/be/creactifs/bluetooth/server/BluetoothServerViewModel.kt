package be.creactifs.bluetooth.server

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import be.creactifs.bluetooth.shared.SERVICE_NAME
import be.creactifs.bluetooth.shared.SERVICE_UUID
import be.creactifs.bluetooth.shared.readMessages
import be.creactifs.bluetooth.shared.send
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

@SuppressLint("MissingPermission")
class BluetoothServerViewModel(application: Application) : AndroidViewModel(application) {

    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var serverSocket: BluetoothServerSocket? = null

    var name by mutableStateOf(bluetoothAdapter?.name ?: "Unknown")
        private set
    var address by mutableStateOf(bluetoothAdapter?.address ?: "Unknown")
        private set
    var running: Boolean by mutableStateOf(false)
        private set

    fun onClick() {
        if (running) {
            stopServer()
        } else {
            startServer()
        }
    }

    private fun startServer() {
        if (running) return
        if (ActivityCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            throw Error("Bluetooth permission not granted")
        }

        running = true
        serverSocket =
            bluetoothAdapter?.listenUsingRfcommWithServiceRecord(SERVICE_NAME, SERVICE_UUID)

        viewModelScope.launch(Dispatchers.IO) {
            var socket: BluetoothSocket?
            while (running) {
                socket = try {
                    serverSocket?.accept()
                } catch (e: IOException) {
                    Log.e(SERVICE_NAME, "Socket's accept() method failed", e)
                    break
                }
                if (socket == null) {
                    continue
                }
                manageConnectedSocket(socket)
                serverSocket?.close()
                break
            }
        }
    }

    private fun stopServer() {
        running = false
        serverSocket?.close()
    }

    private fun manageConnectedSocket(socket: BluetoothSocket) {
        viewModelScope.launch(Dispatchers.IO) {
            socket.readMessages { message ->
                if (message == "Ping") {
                    socket.send("Pong")
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        serverSocket?.close()
    }
}