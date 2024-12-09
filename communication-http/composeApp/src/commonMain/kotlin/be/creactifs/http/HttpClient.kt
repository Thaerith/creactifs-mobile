package be.creactifs.http

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json

expect fun createHttpClient(configuration: HttpClientConfig<*>.() -> Unit): HttpClient

val httpClient = createHttpClient {
    defaultRequest {
        host = SERVER_HOST
        port = SERVER_PORT
    }
    install(ContentNegotiation) {
        json()
    }
}