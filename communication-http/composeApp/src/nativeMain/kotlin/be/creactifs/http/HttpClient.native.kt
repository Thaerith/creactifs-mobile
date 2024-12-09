package be.creactifs.http

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig

actual fun createHttpClient(configuration: HttpClientConfig<*>.() -> Unit): HttpClient {
    TODO()
}