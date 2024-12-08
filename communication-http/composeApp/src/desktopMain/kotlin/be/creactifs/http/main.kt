package be.creactifs.http

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "CommunicationHTTP",
    ) {
        App()
    }
}