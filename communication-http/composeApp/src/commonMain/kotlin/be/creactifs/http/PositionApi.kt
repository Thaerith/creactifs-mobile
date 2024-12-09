package be.creactifs.http

import be.creactifs.http.api.PostPosition
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant

class PositionApi(private val httpClient: HttpClient) {
    suspend fun getPositions(): List<Position> = withContext(Dispatchers.Default) {
        return@withContext httpClient.get("positions").body()
    }

    suspend fun postPosition(
        date: Instant,
        latitude: Float,
        longitude: Float
    ): Position = withContext(Dispatchers.Default) {
        return@withContext httpClient.post("positions") {
            contentType(ContentType.Application.Json)
            setBody(
                PostPosition(
                    date = date,
                    latitude = latitude,
                    longitude = longitude
                )
            )
        }.body()
    }
}

val positionApi = PositionApi(
    httpClient
)