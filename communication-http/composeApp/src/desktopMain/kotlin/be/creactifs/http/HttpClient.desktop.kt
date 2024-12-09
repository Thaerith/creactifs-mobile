package be.creactifs.http

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp

actual fun createHttpClient(configuration: HttpClientConfig<*>.() -> Unit): HttpClient {
    return HttpClient(OkHttp, configuration)
}