package be.creactifs.bluetooth.client

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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

class BluetoothClientViewModel(application: Application) : AndroidViewModel(application) {
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var socket: BluetoothSocket? = null

    var scanning by mutableStateOf(false)
        private set
    var devices by mutableStateOf(emptySet<BluetoothDevice>())
        private set
    var connected: Boolean by mutableStateOf(false)
        private set
    var connecting: Boolean by mutableStateOf(false)
        private set
    var device by mutableStateOf<BluetoothDevice?>(null)
        private set
    var messages: List<String> by mutableStateOf(emptyList())
        private set

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    device?.let {
                        devices = devices + it
                    }
                }
            }
        }
    }

    init {
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        getApplication<Application>().registerReceiver(receiver, filter)
    }

    fun onScan() {
        if (scanning) {
            stopScan()
        } else {
            startScan()
        }
    }

    fun onConnect(device: BluetoothDevice) {
        if (connecting) return
        this.device = device
        stopScan()
        connectToServer(device.address)
    }

    fun onDisconnect() {
        connected = false
        connecting = false
        socket?.close()
        socket = null
    }

    @SuppressLint("MissingPermission")
    private fun startScan() {
        if (scanning) return

        devices = emptySet()
        scanning = true
        bluetoothAdapter?.startDiscovery()
    }

    @SuppressLint("MissingPermission")
    private fun stopScan() {
        if (!scanning) return
        scanning = false
        bluetoothAdapter?.cancelDiscovery()
    }

    private fun connectToServer(deviceAddress: String) {
        connecting = true
        Log.d(SERVICE_NAME, "Connecting to server at $deviceAddress")
        if (ActivityCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            throw Error("Bluetooth permission not granted")
        }

        val device: BluetoothDevice = bluetoothAdapter?.getRemoteDevice(deviceAddress)
            ?: throw Error("Device not found")

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val socket = requireNotNull(device.createRfcommSocketToServiceRecord(SERVICE_UUID))
                this@BluetoothClientViewModel.socket = socket
                socket.connect()
                manageConnectedSocket(socket)
            } catch (e: IOException) {
                Log.e(SERVICE_NAME, "Could not connect to server", e)
                try {
                    connecting = false
                    socket?.close()
                    socket = null
                } catch (closeException: IOException) {
                    Log.e(SERVICE_NAME, "Could not close the client socket", closeException)
                }
            }
        }
    }

    private fun manageConnectedSocket(socket: BluetoothSocket) {
        Log.d(SERVICE_NAME, "Connected to server")
        connected = true
        connecting = false
        viewModelScope.launch(Dispatchers.IO) {
            socket.awaitIncomingMessages()
        }
        socket.send("Ping")
    }

    private fun BluetoothSocket.send(message: String) {
        this.send(
            message,
            onError = { message, exception ->
                Log.e(SERVICE_NAME, "Could not send message", exception)
                messages = messages + "< ERROR: $message"
            },
        ) {
            messages = messages + "< $message"
        }
    }

    private suspend fun BluetoothSocket.awaitIncomingMessages() {
        readMessages(
            onError = { exception ->
                Log.e(SERVICE_NAME, "Could not read message", exception)
            },
        ) {
            messages = messages + "> $it"
        }
    }

    override fun onCleared() {
        super.onCleared()
        socket?.close()
        socket = null
    }
}