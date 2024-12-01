package be.creactifs.bluetooth.shared

import android.bluetooth.BluetoothSocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

fun BluetoothSocket.send(
    message: String,
    onError: (message: String, exception: Exception) -> Unit = { _, _ -> },
    onSent: (message: String) -> Unit = {},
) {
    try {
        outputStream.write(message.toByteArray())
        onSent(message)
    } catch (e: IOException) {
        onError(message, e)
    }
}

suspend fun BluetoothSocket.readMessages(
    onError: (exception: Exception) -> Unit = {},
    onNewMessage: (message: String) -> Unit = {},
) = withContext(Dispatchers.IO) {
    val buffer = ByteArray(1024)
    var bytes: Int

    while (true) {
        try {
            bytes = inputStream?.read(buffer) ?: -1
            if (bytes > 0) {
                val message = String(buffer, 0, bytes)
                onNewMessage(message)
            }
        } catch (e: IOException) {
            onError(e)
        }
    }
}